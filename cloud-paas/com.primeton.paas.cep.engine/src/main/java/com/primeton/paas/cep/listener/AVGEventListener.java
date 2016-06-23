/**
 * 
 */
package com.primeton.paas.cep.listener;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.primeton.paas.cep.engine.Constants;
import com.primeton.paas.cep.engine.ServerContext;
import com.primeton.paas.cep.model.AbstractEventListener;
import com.primeton.paas.cep.model.EventMessage;
import com.primeton.paas.cep.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class AVGEventListener extends AbstractEventListener implements Constants {
	
	private static final String MSG_TYPE_KEY = "MSG_TYPE";
	private static final String MSG_TYPE_VALUE = "AVG_APP_MONITOR";
	
	private ServerContext context = ServerContext.getContext();
	private static final String EXCHANGE_NAME = "avgMonitorExchange";
	private boolean isCreated = false;
	
	private static ILogger logger = LoggerFactory.getLogger(AVGEventListener.class);
	
	public static final String EPS = "select appName, avg(cpu_us), avg(cpu_sy), avg(cpu_ni), avg(cpu_id), avg(cpu_wa), avg(cpu_hi), avg(cpu_si), avg(cpu_st), avg(cpu_oneload), avg(cpu_fiveload), avg(cpu_fifteenload), avg(mem_total), avg(mem_used), avg(mem_free), avg(mem_buffers), avg(mem_us), avg(io_si), avg(io_so), avg(io_bi), avg(io_bo) from appMonitorEvent(100-cpu_us>=0, 100-cpu_sy>=0, 100-cpu_ni>=0, 100-cpu_id>=0, 100-cpu_wa>=0, 100-cpu_hi>=0, 100-cpu_si>=0, 100-cpu_st>=0, 100-mem_us>=0).win:time_batch(? sec) where appName = ? group by appName";

	/* (non-Javadoc)
	 * @see com.espertech.esper.client.UpdateListener#update(com.espertech.esper.client.EventBean[], com.espertech.esper.client.EventBean[])
	 */
//	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		logger.debug("EventListener " + this + " has been accessed.");
		if (newEvents == null || newEvents.length == 0) {
			return;
		}
		EventBean e = newEvents[0];
		String appName = getValue(e, "appName");
		if (StringUtil.isEmpty(appName) || e.get("avg(cpu_us)") == null) {
			return;
		}
		long occur_time = System.currentTimeMillis();
		Double cpu_us = getValue(e, "avg(cpu_us)");
		Double cpu_sy = getValue(e, "avg(cpu_sy)");
		Double cpu_ni = getValue(e, "avg(cpu_ni)");
		Double cpu_id = getValue(e, "avg(cpu_id)");
		Double cpu_wa = getValue(e, "avg(cpu_wa)");
		Double cpu_hi = getValue(e, "avg(cpu_hi)");
		Double cpu_si = getValue(e, "avg(cpu_si)");
		Double cpu_st = getValue(e, "avg(cpu_st)");
		Double cpu_oneload = getValue(e, "avg(cpu_oneload)");
		Double cpu_fiveload = getValue(e, "avg(cpu_fiveload)");
		Double cpu_fifteenload = getValue(e, "avg(cpu_fifteenload)");
		
		Double mem_total = getValue(e, "avg(mem_total)");
		Double mem_used = getValue(e, "avg(mem_used)");
		Double mem_free = getValue(e, "avg(mem_free)");
		Double mem_buffers = getValue(e, "avg(mem_buffers)");
		Double mem_us = getValue(e, "avg(mem_us)");
		
		Double io_si = getValue(e, "avg(io_si)");
		Double io_so = getValue(e, "avg(io_so)");
		Double io_bi = getValue(e, "avg(io_bi)");
		Double io_bo = getValue(e, "avg(io_bo)");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(APP_NAME, appName);
		result.put(OCCUR_TIME, occur_time);
		
		result.put(CPU_US, parseDouble(cpu_us, 1));
		result.put(CPU_SY, parseDouble(cpu_sy, 1));
		result.put(CPU_NI, parseDouble(cpu_ni, 1));
		result.put(CPU_ID, parseDouble(cpu_id, 1));
		result.put(CPU_WA, parseDouble(cpu_wa, 1));
		result.put(CPU_HI, parseDouble(cpu_hi, 1));
		result.put(CPU_SI, parseDouble(cpu_si, 1));
		result.put(CPU_ST, parseDouble(cpu_st, 1));
		result.put(CPU_ONELOAD, parseDouble(cpu_oneload, 2));
		result.put(CPU_FIVELOAD, parseDouble(cpu_fiveload, 2));
		result.put(CPU_FIFTEENLOAD, parseDouble(cpu_fifteenload, 2));
		
		result.put(MEM_TOTAL, parseLong(mem_total));
		result.put(MEM_USED, parseLong(mem_used));
		result.put(MEM_FREE, parseLong(mem_free));
		result.put(MEM_BUFFERS, parseLong(mem_buffers));
		result.put(MEM_US, parseDouble(mem_us, 1));
		
		result.put(IO_SI, parseLong(io_si));
		result.put(IO_SO, parseLong(io_so));
		result.put(IO_BI, parseLong(io_bi));
		result.put(IO_BO, parseLong(io_bo));
		
		// wrap message
		EventMessage message = new EventMessage();
		message.putHeader(MSG_TYPE_KEY, MSG_TYPE_VALUE);
		message.putHeader(APP_NAME, appName);
		message.setBody(result);
		
		// Engine output
		send(message);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Object> T getValue(EventBean e, String key) {
		return (T)e.get(key);
	}

	private double parseDouble(double value, int len) {
		BigDecimal decimal = new BigDecimal(value);
		return decimal.setScale(len, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
	
	private long parseLong(double value) {
		BigDecimal decimal = new BigDecimal(value);
		return decimal.longValue();
	}
	
	private void send(EventMessage message) {
		try {
			MQClient mqClient = context.getMQClient();
			if (!isCreated) {
				mqClient.createExchange(EXCHANGE_NAME, "fanout", false);
				isCreated = true;
			}
			mqClient.sendMessage(EXCHANGE_NAME, "", message, false);
		} catch (Throwable t) {
			logger.error(t);
		}
	}
	
}
