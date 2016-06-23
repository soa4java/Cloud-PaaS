package com.primeton.paas.cep.event;

import java.util.HashMap;
import java.util.Map;

import com.primeton.paas.cep.engine.Constants;
import com.primeton.paas.cep.model.EventType;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceMonitorEvent implements EventType, Constants {

	public static final String NAME = "serviceMonitorEvent";
	
	private static Map<String, Object> type = new HashMap<String, Object>();
	
	public String getName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.model.EventType#getType()
	 */
	public Map<String, Object> getType() {
		if (type == null) {
			type = new HashMap<String, Object>();
		}
		if (type.isEmpty()) {
			// Common
			//type.put(IP, String.class);
			type.put(CLUSTER_ID, String.class);
			type.put(OCCUR_TIME, Long.class);

			// CPU
			type.put(CPU_US, Double.class);
			type.put(CPU_SY, Double.class);
			type.put(CPU_NI, Double.class);
			type.put(CPU_ID, Double.class);
			type.put(CPU_WA, Double.class);
			type.put(CPU_HI, Double.class);
			type.put(CPU_SI, Double.class);
			type.put(CPU_ST, Double.class);
			type.put(CPU_ONELOAD, Double.class);
			type.put(CPU_FIVELOAD, Double.class);
			type.put(CPU_FIFTEENLOAD, Double.class);

			// MEM
			type.put(MEM_TOTAL, Long.class); // KB
			type.put(MEM_USED, Long.class); // KB
			type.put(MEM_FREE, Long.class); // KB
			type.put(MEM_BUFFERS, Long.class); // KB
			type.put(MEM_US, Double.class); // KB

			// IO
			type.put(IO_SI, Long.class); // KB
			type.put(IO_SO, Long.class); // KB
			type.put(IO_BI, Long.class); // KB
			type.put(IO_BO, Long.class); // KB
		}
		return type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer(super.toString()).append(": {")
				.append("eventName:").append(getName())
				.append(", type:").append(type)
				.append("}").toString();
	}

}
