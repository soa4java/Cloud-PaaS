/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import java.util.HashMap;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.mqclient.api.Command;
import org.gocom.cloud.cesium.mqclient.api.CommandMessage;
import org.gocom.cloud.cesium.mqclient.api.CommandResultMessage;
import org.gocom.cloud.cesium.mqclient.api.Message;
import org.gocom.cloud.cesium.mqclient.api.MessageProducer;
import org.gocom.cloud.cesium.mqclient.api.ServiceCommandMessage;
import org.gocom.cloud.cesium.mqclient.api.StringMessage;
import org.gocom.cloud.cesium.mqclient.api.WriteFileMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.RuntimeManagerFactory;
import com.primeton.paas.manage.api.manager.IRuntimeManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Send message to rabbitmq_server. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class SendMessageUtil {

	private static final String SERVICE_ID = "id";
	private static final String SERVICE_IP = "ip";
	private static final String SERVICE_TYPE = "type";
	private static final String SERVICE_PORT = "port";
	
	/**
	 * agent-${ip}.
	 */
	public static final String AGENT_QUEUE_PREFIX = "agent-";
	
	private static int RETRY_TIMES = SystemVariables.getIntValue("send_message_retry_times", 3); //$NON-NLS-1$
	
	public static final String EXECUTE_SCRIPT_TIMEOUT = "execute_script_timeout";
	
	private static IRuntimeManager runtimeManager = RuntimeManagerFactory.getManager();
	
	private static ILogger logger = ManageLoggerFactory.getLogger(SendMessageUtil.class);
	
	/**
	 * Default. <br>
	 */
	private SendMessageUtil() {
		super();
	}

	/**
	 * 
	 * @param service
	 * @param scriptName
	 * @param otherArgs
	 * @param isResponse
	 * @return
	 * @throws MessageException
	 */
	public static CommandResultMessage sendMessage(IService service,
			String scriptName, Map<String, String> otherArgs, boolean isResponse)
			throws MessageException {
		return sendMessage(service, scriptName, otherArgs, null, -1, isResponse);
	}
	
	/**
	 * 
	 * @param service
	 * @param scriptName
	 * @param otherArgs
	 * @param isResponse
	 * @param executeTimeout
	 * @return
	 * @throws MessageException
	 */
	public static CommandResultMessage sendMessage(IService service,
			String scriptName, Map<String, String> otherArgs,
			boolean isResponse, long executeTimeout) throws MessageException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(EXECUTE_SCRIPT_TIMEOUT, String.valueOf(executeTimeout));
		return sendMessage(service, scriptName, otherArgs, headers,
				executeTimeout + 10000L, isResponse);
	}

	/**
	 * 
	 * @param service
	 * @param scriptName
	 * @param otherArgs
	 * @param headers
	 * @param timeout
	 * @param isResponse
	 * @return
	 * @throws MessageException
	 */
	public static CommandResultMessage sendMessage(IService service,
			String scriptName, Map<String, String> otherArgs,
			Map<String, String> headers, long timeout, boolean isResponse)
			throws MessageException {
		if (service == null || StringUtil.isEmpty(service.getType())
				|| StringUtil.isEmpty(scriptName)) {
			return null;
		}
		
		String agent = service.getIp();
		String agentQueue = getAgentQueue(agent);
		
		boolean isRunning = runtimeManager.agentIsOnline(agent);
		if (!isRunning) {
			throw new MessageException("NodeAgent [" + agent + "] is not running.");
		}
		
		Map<String, String> args = new HashMap<String,String>();
		args.putAll(service.getAttributes());
		
		args.put(SERVICE_ID, service.getId());
		args.put(SERVICE_IP, service.getIp());
		args.put(SERVICE_TYPE, service.getType());
		args.put(SERVICE_PORT, String.valueOf(service.getPort()));
		if (otherArgs != null && otherArgs.size() > 0) {
			args.putAll(otherArgs);
		}
		
		Command body = new Command();
		body.setScriptPath(SystemVariables.getScriptPath(service.getType(), scriptName));
		body.setArgs(args);
		
		CommandMessage message = new CommandMessage();
		message.setBody(body);
		message.putHeader("targetAgent", agent); //$NON-NLS-1$
		if (headers != null && headers.size() > 0) {
			message.getHeaders().putAll(headers);
		}
		message.setNeedResponse(isResponse);
		
		timeout = timeout > 0 ? timeout : SystemVariables.getMaxWaitMessageTime();
		
		int count = 0;
		while (true) {
			MessageProducer messageProducer = CesiumFactory.getMessageProducer();
			Message<?> result = messageProducer.sendMessage(agentQueue, message, timeout);
			if (! isResponse) {
				return null;
			}
			if (isResponse && result instanceof CommandResultMessage) {
				return (CommandResultMessage)result;
			}
			if (count > RETRY_TIMES)  {
				logger.error("Send message to agent [" + agent + "] failure."); //$NON-NLS-1$
				break;
			} else {
				count ++;
				logger.warn("Send message to agent [" + agent + "] timeout, try again count [ " + count + " ]. Retry after 5 seconds."); //$NON-NLS-1$
			}
			ThreadUtil.sleep(5000L);
		}
		
		return null;
	}

	
	/**
	 * 
	 * @param service
	 * @param scriptName
	 * @param isResponse
	 * @return
	 * @throws MessageException
	 */
	public static CommandResultMessage sendMessage(IService service,
			String scriptName, boolean isResponse) throws MessageException {
		return sendMessage(service, scriptName, null, isResponse);
	}
	
	/**
	 * 
	 * @param ip
	 * @param scriptName
	 * @param args
	 * @param isResponse
	 * @return
	 * @throws MessageException
	 */
	public static CommandResultMessage sendMessage(String ip,
			String scriptName, Map<String, String> args, boolean isResponse)
			throws MessageException {
		return sendMessage(ip, scriptName, args, null, -1, isResponse);
	}
	
	/**
	 * 
	 * @param ip
	 * @param scriptName
	 * @param args
	 * @param isResponse
	 * @param executeTimeout
	 * @return
	 * @throws MessageException
	 */
	public static CommandResultMessage sendMessage(String ip,
			String scriptName, Map<String, String> args, boolean isResponse,
			long executeTimeout) throws MessageException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(EXECUTE_SCRIPT_TIMEOUT, String.valueOf(executeTimeout));
		return sendMessage(ip, scriptName, args, headers,
				executeTimeout + 10000L, isResponse);
	}
	
	/**
	 * 
	 * @param ip
	 * @param scriptName
	 * @param args
	 * @param headers
	 * @param timeout
	 * @param isResponse
	 * @return
	 * @throws MessageException
	 */
	public static CommandResultMessage sendMessage(String ip,
			String scriptName, Map<String, String> args,
			Map<String, String> headers, long timeout, boolean isResponse)
			throws MessageException {
		if (StringUtil.isEmpty(ip) || StringUtil.isEmpty(scriptName)) {
			return null;
		}
		boolean isRunning = runtimeManager.agentIsOnline(ip);
		if (!isRunning) {
			throw new MessageException("NodeAgent [" + ip //$NON-NLS-1$
					+ "] is not running."); //$NON-NLS-1$
		}
		
		Command body = new Command();
		body.setScriptPath(scriptName);
		body.setArgs(args);
		
		CommandMessage message = new CommandMessage();
		message.setBody(body);
		message.putHeader("targetAgent", ip); //$NON-NLS-1$
		if (headers != null && headers.size() > 0) {
			message.getHeaders().putAll(headers);
		}
		message.setNeedResponse(isResponse);
		
		timeout = timeout > 0 ? timeout : SystemVariables.getMaxWaitMessageTime();
		int count = 0;
		while (true) {
			MessageProducer messageProducer = CesiumFactory.getMessageProducer();
			Message<?> result = messageProducer.sendMessage(getAgentQueue(ip), message, timeout);
			if (! isResponse) {
				return null;
			}
			if (result != null && result instanceof CommandResultMessage) {
				return (CommandResultMessage)result;
			}
			if (count > RETRY_TIMES)  {
				logger.error("Send message to agent [" + ip + "] failure."); //$NON-NLS-1$
				break;
			} else {
				count++;
				logger.warn("Send message to agent [" + ip + "] timeout, try again count [ " + count + " ]. Retry after 5 seconds."); //$NON-NLS-1$
			}
			ThreadUtil.sleep(5000L);
		}
		return null;
	}
	
	/**
	 * 
	 * @param ip
	 * @param cmd
	 */
	public static void sendMessage(String ip, String cmd) {
		if (StringUtil.isEmpty(ip) || StringUtil.isEmpty(cmd)) {
			if (logger.isWarnEnabled()) {
				logger.warn("Can not send command, ip is empty or cmd is empty"); //$NON-NLS-1$
			}
			return;
		}
		Command body = new Command();
		body.setScriptPath(cmd);
		CommandMessage message = new CommandMessage();
		message.setBody(body);
		message.putHeader("targetAgent", ip); //$NON-NLS-1$
		
		if (logger.isInfoEnabled()) {
			logger.info("Will execute remote host [" + ip + "] cmd [" + cmd + "]."); //$NON-NLS-1$
		}
	}
	
	/**
	 * 
	 * @param agentID
	 * @param message
	 * @param timeout
	 * @return
	 */
	public static CommandResultMessage sendMessage(
			ServiceCommandMessage message, long timeout)
			throws MessageException {
		MessageProducer messageProducer = CesiumFactory
				.getMessageProducer();
		Message<?> result = messageProducer.sendMessage(
				getAgentQueue(message.getIp()), message, timeout);
		return (result != null && result instanceof CommandResultMessage) ? (CommandResultMessage) result
				: null;
	}
	
	/**
	 * 
	 * @param message
	 * @param targetAgent
	 * @param timeout
	 * @return
	 * @throws MessageException
	 */
	public static StringMessage sendMessage(WriteFileMessage message,
			String targetAgent, long timeout) throws MessageException {
		MessageProducer producer = CesiumFactory.getMessageProducer();
		Message<?> result = producer.sendMessage(getAgentQueue(targetAgent),
				message, timeout);
		return null != result && result instanceof StringMessage ? (StringMessage) result
				: null;
	}
	
	/**
	 * 
	 * @param ip ip
	 * @return
	 */
	public static String getAgentQueue(String ip) {
		return AGENT_QUEUE_PREFIX + ip;
	}
	
}
