/**
 * 
 */
package com.primeton.paas.manage.api.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MetaData {
	
	// CPU [ top -bcisSH -n 1 | grep Cpu ]
	public static final String CPU_US = "cpu_us";
	public static final String CPU_SY = "cpu_sy";
	public static final String CPU_NI = "cpu_ni";
	public static final String CPU_ID = "cpu_id";
	public static final String CPU_WA = "cpu_wa";
	public static final String CPU_HI = "cpu_hi";
	public static final String CPU_SI = "cpu_si";
	public static final String CPU_ST = "cpu_st";
	
	// Mem [ top -bcisSH -n 1 | grep Mem ]
	public static final String MEM_TOTAL = "mem_total";
	public static final String MEM_USED = "mem_used";
	public static final String MEM_FREE = "mem_free";
	public static final String MEM_BUFFERS = "mem_buffers";
	
	// CPU Load [ top -bcisSH -n 1 | grep 'load average:' ]
	public static final String CPU_ONELOAD = "cpu_oneload";
	public static final String CPU_FIVELOAD = "cpu_fiveload";
	public static final String CPU_FIFTEENLOAD = "cpu_fifteenload";
	
	// others
	public static final String MEM_US = "mem_us";
	public static final String IP = "ip";
	public static final String OCCUR_TIME = "occur_time";
	public static final String ID = "id";
	
	// IO [ vmstat -S K ]
	public static final String IO_SI = "io_si";
	public static final String IO_SO = "io_so";
	public static final String IO_BI = "io_bi";
	public static final String IO_BO = "io_bo";
	
	// STO
	public static final String STO_FILESYSTEM = "sto_filesystem";
	public static final String STO_SIZE = "sto_size";
	public static final String STO_USED = "sto_used";
	public static final String STO_AVAIL = "sto_avail";
	public static final String STO_USE = "sto_use";
	public static final String STO_MOUNTED  = "sto_mounted";
	
	private Map<String, Object> metadata = new HashMap<String, Object>();
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Object> T get(String key) {
		return (T) metadata.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected void set(String key, Object value) {
		if (key != null) {
			metadata.put(key, value);
		}
	}
	
	public String getId() {
		return get(ID);
	}
	
	public void setId(String id) {
		metadata.put(ID, id);
	}
	
	public String getIp() {
		return get(IP);
	}
	
	public void setIp(String ip) {
		metadata.put(IP, ip);
	}
	
	public Long getTime() {
		return get(OCCUR_TIME);
	}
	
	public void setTime(Long time) {
		metadata.put(OCCUR_TIME, time);
	}
	
	public Float getCpuUs() {
		return get(CPU_US);
	}
	
	public void setCpuUs(Float cpuUs) {
		metadata.put(CPU_US, cpuUs);
	}
	
	public Float getCpuSy() {
		return get(CPU_SY);
	}
	
	public void setCpuSy(Float cpuSy) {
		metadata.put(CPU_SY, cpuSy);
	}
	
	public Float getCpuNi() {
		return get(CPU_NI);
	}
	
	public void setCpuNi(Float cpuNi) {
		metadata.put(CPU_NI, cpuNi);
	}
	
	public Float getCpuId() {
		return get(CPU_ID);
	}
	
	public void setCpuId(Float cpuId) {
		metadata.put(CPU_ID, cpuId);
	}
	
	public Float getCpuWa() {
		return get(CPU_WA);
	}
	
	public void setCpuWa(Float cpuWa) {
		metadata.put(CPU_WA, cpuWa);
	}
	
	public Float getCpuHi() {
		return get(CPU_HI);
	}
	
	public void setCpuHi(Float cpuHi) {
		metadata.put(CPU_HI, cpuHi);
	}
	
	public Float getCpuSi() {
		return get(CPU_SI);
	}
	
	public void setCpuSi(Float cpuSi) {
		metadata.put(CPU_SI, cpuSi);
	}
	
	public Float getCpuSt() {
		return get(CPU_ST);
	}
	
	public void setCpuSt(Float cpuSt) {
		metadata.put(CPU_ST, cpuSt);
	}
	
	public Long getMemTotal() {
		return get(MEM_TOTAL);
	}
	
	public void setMemTotal(Long memTotal) {
		metadata.put(MEM_TOTAL, memTotal);
	}
	
	public Long getMemUsed() {
		return get(MEM_USED);
	}
	
	public void setMemUsed(Long memUsed) {
		metadata.put(MEM_USED, memUsed);
	}
	
	public Long getMemFree() {
		return get(MEM_FREE);
	}
	
	public void setMemFree(Long memFree) {
		metadata.put(MEM_FREE, memFree);
	}
	
	public Long getMemBuffers() {
		return get(MEM_BUFFERS);
	}
	
	public void setMemBuffers(Long memBuffers) {
		metadata.put(MEM_BUFFERS, memBuffers);
	}
	
	public Float getMemUs() {
		return get(MEM_US);
	}
	
	public void setMemUs(Float memUs) {
		metadata.put(MEM_US, memUs);
	}
	
	public Float getCpuOneLoad() {
		return get(CPU_ONELOAD);
	}
	
	public void setCpuOneLoad(Float cpuOneLoad) {
		metadata.put(CPU_ONELOAD, cpuOneLoad);
	}
	
	public Float getCpuFiveLoad() {
		return get(CPU_FIVELOAD);
	}
	
	public void setCpuFiveLoad(Float cpuFiveLoad) {
		metadata.put(CPU_FIVELOAD, cpuFiveLoad);
	}
	
	public Float getCpuFifteenLoad() {
		return get(CPU_FIFTEENLOAD);
	}
	
	public void setCpuFifteenLoad(Float cpuFifteenLoad) {
		metadata.put(CPU_FIFTEENLOAD, cpuFifteenLoad);
	}
	
	public Long getIoSi() {
		return get(IO_SI);
	}
	
	public void setIoSi(Long ioSi) {
		metadata.put(IO_SI, ioSi);
	}
	
	public Long getIoSo() {
		return get(IO_SO);
	}
	
	public void setIoSo(Long ioSo) {
		metadata.put(IO_SO, ioSo);
	}
	
	public Long getIoBi() {
		return get(IO_BI);
	}
	
	public void setIoBi(Long ioBi) {
		metadata.put(IO_BI, ioBi);
	}
	
	public Long getIoBo() {
		return get(IO_BO);
	}
	
	public void setIoBo(Long ioBo) {
		metadata.put(IO_BO, ioBo);
	}
	
	public Map<String, Object> getMetaData() {
		return metadata;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer(super.toString())
			.append(":").append(metadata)
			.toString();
	}
	
}
