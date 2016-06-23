package com.primeton.paas.mail.server.startup;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.cesium.mqclient.api.MQClientFactory;
import org.gocom.cloud.cesium.mqclient.api.MQServer;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.mail.server.config.ISystemConfig;
import com.primeton.paas.mail.server.config.Constants;
import com.primeton.paas.mail.server.config.SystemConfigFactory;

/**
 * �ʼ�ת�������߳� 
 *:�����ʼ���Ϣ�߳�������mailworker����
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class MailCoreThread extends Thread {
	private static final String NAME_MQCLIENT = "mail";
	
	private static ILogger logger = LoggerFactory.getLogger(MailCoreThread.class);
	
	private boolean flag = false; // �˳���ʶ
	private List<MailConsumer> mailConsumers = new ArrayList<MailConsumer>();
	private List<MailWorker> mailWorkers = new ArrayList<MailWorker>();
	
	private boolean isRunning = false;
	
	public MailCoreThread() {
		super();
	}
	
	private void init() {
		ISystemConfig systemConfig = SystemConfigFactory.getSystemConfig();
		try {
			MQServer server = systemConfig.getMQServer();
			MQClient mqClient = MQClientFactory.createMQClient(NAME_MQCLIENT, server);//TODO io exception
			String[] queues = systemConfig.getQueueGroup();
			
			if(mqClient != null && queues != null && queues.length > 0) {//����mq������Ϣ
				for (String queue : queues) {
					try {
						// ��������
						mqClient.createQueue(queue, true);
						// �½�mailConsumer
						MailConsumer mailConsumer = new MailConsumer(queue, mqClient);
						// ����mailConsumer
						mailConsumer.receiveMessage();
						mailConsumers.add(mailConsumer);
					} catch (Throwable t) {
						if(logger.isErrorEnabled()) {
							logger.error(t);
						}
					}
				}
			}
			//����mailworker�����ʼ�
			//TODO �½�mailworker
			for(int i=0;i<Integer.parseInt(SystemProperties.getProperty(Constants.MAX_MAILWORKER_NUM));i++){
				 mailWorkers.add(new MailWorker());
			}
			//����mailworker
			for(int i=0;i<mailWorkers.size();i++){
				mailWorkers.get(i).start();
			}
		} catch (Throwable t) {
			if(logger.isErrorEnabled()) {
				logger.error(t);
			}
		}
		
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
//	@Override
	public void run() {
		
		init();
		
		this.isRunning = true;
		
		// Add hook thread
		ShutdownCallBackThread hook = new ShutdownCallBackThread(this);
		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(hook);
		
		while(true) {
			
			if(this.flag) { // �ر�
				//���ιر�mailworker�߳� ֹͣ����
				for(int i=0;i<mailWorkers.size();i++){
					mailWorkers.get(i).close();
				}
				
				ISystemConfig systemConfig = SystemConfigFactory.getSystemConfig();
				try {
					MQClient client = MQClientFactory.getMQClient(NAME_MQCLIENT);
					String[] queues = systemConfig.getQueueGroup();
					if(client != null && queues != null) {
						for (String queueName : queues) {
							client.destroyQueue(queueName);//�رս�����Ϣ��ɾ�����
						}
					}
					if(client != null) {
						MQClientFactory.close();
					}
					
					
				} catch (Throwable t) {
					if(logger.isErrorEnabled()) {
						logger.error(t);
					}
				}
				this.isRunning = false;
				break;
			}
			waitfor(5000);//TODO
		}
		
	}
	
	private void waitfor(long time) {
		if(time > 0) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				if(logger.isErrorEnabled()) {
					logger.error(e);
				}
			}
		}
	}
	
	/**
	 * �ر��߳� <br>
	 */
	public void close() {
		this.flag = true;
	}
	
	/**
	 * 
	 * @author liuyi (mail-to:liu-yi@primeton.com)
	 */
	private static class ShutdownCallBackThread extends Thread {
		
		private MailCoreThread thread;
		
		/**
		 * @param thread consumer
		 */
		public ShutdownCallBackThread(MailCoreThread thread) {
			super();
			this.thread = thread;
		}

		@Override
		public void run() {
			if(logger.isInfoEnabled()) {
				logger.info("JVM will be shut down.");
			}
			if(this.thread != null) {
				this.thread.close();
			}
			long timeout = 60000L;
			long begin = System.currentTimeMillis();
			long end = 0L;
			
			// wait for consumer stop
			while(true) {
				if(this.thread.isRunning()) {
					waitfor(100L);
				} else {
					break;
				}
				
				end = System.currentTimeMillis();
				if(end - begin >= timeout) {
					break;
				}
			}
		}
		
		private void waitfor(long timeout) {
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				if(logger.isErrorEnabled()) {
					logger.error(e);
				}
			}
		}
		
	}
}
