/**
 * 
 */
package com.primeton.paas.openapi.engine;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import javax.servlet.AsyncContext;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.flowcontrol.FlowControlProviderManager;
import com.primeton.paas.openapi.flowcontrol.IFlowControlHandler;
import com.primeton.paas.openapi.security.SecurityProviderManager;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class BizRequestQueueManager {
	
	private static ILogger log = LoggerFactory.getLogger(BizRequestQueueManager.class);

	private static int DEFAULT_SERVICE_GRADE_PRIORITY_RATE = 3;

	private static Queue<BizRequest> fastQueue = new ConcurrentLinkedQueue<BizRequest>();

	private static Queue<BizRequest> slowQueue = new ConcurrentLinkedQueue<BizRequest>();

	private static int _priorityRate = DEFAULT_SERVICE_GRADE_PRIORITY_RATE;

	private static BizRequestHandleThread handleThread = null;

	private static ExecutorService executor;

	private static long pollCnt = 0L; 

	public static void addFastRequest(BizRequest req) {
		fastQueue.add(req);
	}

	public static void addSlowRequest(BizRequest req) {
		slowQueue.add(req);
	}

	public static void setPriorityRate(int priorityRate) {
		_priorityRate = priorityRate;
		pollCnt = 0L;
	}

	public static void resetPriorityRate() {
		setPriorityRate(DEFAULT_SERVICE_GRADE_PRIORITY_RATE);
	}

	public static int getPriorityRate() {
		return _priorityRate;
	}

	public static int getFastQueueSize() {
		return fastQueue.size();
	}

	public static int getSlowQueueSize() {
		return slowQueue.size();
	}

	public static int getActiveThreadNum() {
		return ((ThreadPoolExecutor) executor).getActiveCount();
	}

	public static BizRequest pollRequest() {
		BizRequest ret = null;
		if ((pollCnt + 1) % (_priorityRate + 1) == 0) {
			ret = slowQueue.poll();
			if (ret != null)
				pollCnt++;
			else
				ret = fastQueue.poll();
		} else {
			ret = fastQueue.poll();
			if (ret != null)
				pollCnt++;
			else
				ret = slowQueue.poll();
		}
		return ret;
	}

	public static void startThreadPool() {
		if (log.isDebugEnabled())
			log.debug("Begin to start request thread pool");
		executor = Executors.newCachedThreadPool(new BizRequestThreadFactory());
		handleThread = new BizRequestHandleThread();
		handleThread.start();
		if (log.isDebugEnabled())
			log.debug("Finished to start request thread pool");
	}

	public static void stopThreadPool() {
		if (log.isDebugEnabled())
			log.debug("Begin to shutdown request thread pool");
		executor.shutdown();
		executor = null;
		handleThread = null;
		if (log.isDebugEnabled())
			log.debug("Finished to shutdown request thread pool");
	}

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private static class BizRequestHandleWorker implements Runnable {
		
		private BizRequest req;

		private IFlowControlHandler flowControlHandler;

		public BizRequestHandleWorker(BizRequest req, IFlowControlHandler flowControlHandler) {
			this.req = req;
			this.flowControlHandler = flowControlHandler;
		}

		public void run() {
			AsyncContext actx = req.getActx();
			if (this.flowControlHandler != null) {
				String custId = req.getCustId();
				String bizCode = req.getBizCode();
				if (!this.flowControlHandler.canInvoke(req.getTransactionId(), custId, bizCode)) {
					String errorMsg = "no enough quota for customer " + custId + " to invoke business " + bizCode;
					log.error(errorMsg);
					try {
						// TODO json
						actx.getResponse().getWriter().write("request custId=" + custId + ",bizCode=" + bizCode + " is rejected by FlowController because has not enough quota");
						actx.getResponse().getWriter().flush();
					} catch (IOException e) {
						log.error(e);
					} finally {
						actx.complete();
					}
					return;
				}
			}

			// BizRequestCache.getInstance().putBizRequst(req);
			String bizCode = req.getBizCode();

			IBizInvoker bizInvoker = BizInvokerManager.getInstance().getBizInvoker(bizCode);
			if (bizInvoker == null) {
				log.error("IBizInvoker for business code " + bizCode + " not found");
				try {
					actx.getResponse().getWriter().write("IBizInvoker for business code " + bizCode + " not found");
					actx.getResponse().getWriter().flush();
				} catch (IOException e) {
					log.error(e);
				} finally {
					actx.complete();
				}
				return;
			}
			bizInvoker.setSecurityHandler(SecurityProviderManager.getMsgReduceProvider().getSecurityHander());

			BizInvokeResult result = bizInvoker.invokeBiz(req);
			if (result.getCompletedStauts() == 0) {
				try {
					String retJsonStr = result.getResultBodyStr();
					actx.getResponse().getWriter().write(retJsonStr);
					long startTime = (Long) actx.getRequest().getAttribute("startTime");
					long endTime = System.currentTimeMillis();

					actx.getResponse().getWriter().write("\n<br>Finished request cost " + (endTime - startTime) + "ms");
					actx.getResponse().getWriter().flush();
					this.flowControlHandler.afterInvoke(req.getTransactionId(), req.getCustId(), req.getBizCode(), 0);
				} catch (IOException e) {
					log.error(e);
					this.flowControlHandler.afterInvoke(req.getTransactionId(), req.getCustId(), req.getBizCode(), 1);
				} finally {
					actx.complete();
					if (log.isDebugEnabled())
						log.debug(actx + " completed.");
				}
			} else {
				try {
					actx.getResponse().getWriter().write("{\"exception\":\"" + result.getException().getMessage() + "\"}");
					actx.getResponse().getWriter().flush();
				} catch (IOException e) {
					log.error(e);
				} finally {
					actx.complete();
					if (log.isDebugEnabled())
						log.debug(actx + " completed.");
					this.flowControlHandler.afterInvoke(req.getTransactionId(), req.getCustId(), req.getBizCode(), 1);
				}
			}
		}
	}

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private static class BizRequestHandleThread extends Thread {
		
		private IFlowControlHandler flowControlHandler = FlowControlProviderManager.getMsgReduceProvider().getFlowControlHandler();

		public BizRequestHandleThread() {
			this.setName("BizRequestHandleThread");
			this.setDaemon(true);
		}

		public void run() {
			while (true) {
				BizRequest req = null;
				while ((req = BizRequestQueueManager.pollRequest()) != null) {
					handleRequest(req);
				}
				LockSupport.parkNanos(300000L);
			}
		}

		private void handleRequest(BizRequest req) {
			BizRequestHandleWorker worker = new BizRequestHandleWorker(req, flowControlHandler);
			try {
				executor.submit(worker);
			} catch (Throwable e) {
				Thread t = new Thread(worker);
				t.setName("BizRequestHanderBackupThread");
				t.setDaemon(true);
				t.start();
			}
		}
	}

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private static class BizRequestThreadFactory implements ThreadFactory {

		private final AtomicInteger threadNumber = new AtomicInteger(1);

		private String namePrefix;

		public BizRequestThreadFactory() {
			namePrefix = "BizRequestHandleWorker #";
		}

		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, namePrefix + threadNumber.getAndIncrement());
			thread.setDaemon(false);
			return thread;
		}

	}
	
}
