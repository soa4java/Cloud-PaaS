/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.app.config.eos;

import com.primeton.paas.app.config.IConfigModel;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DasModel implements IConfigModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -827990841443974786L;

	// 批量进行数据库操作的数据条数
	private int batchSize;
	
	// Statement每次从数据库中取出的记录数
	private int fetchSize;
	
	// 序列号生成器的缓冲池大小
	private int idGeneratorBuffer;
	
	// ResultSet的滚动功能是否可用
	private boolean rollEnable;
	
	// ResultSet记录条数限制
	private int resultLimits;
	
	// ResultSet记录条数限制,超出条数限制是否抛异常
	private boolean exceedLimitException;
	
	// ResultSet超过多少条记录记入系统日志
	private int resultLimitsWriteLog;
	
	// 存储LOB类型数据的临时目录
	private int lobDataTemp;

	/**
	 * Default. <br>
	 */
	public DasModel() {
		super();
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public int getIdGeneratorBuffer() {
		return idGeneratorBuffer;
	}

	public void setIdGeneratorBuffer(int idGeneratorBuffer) {
		this.idGeneratorBuffer = idGeneratorBuffer;
	}

	public boolean isRollEnable() {
		return rollEnable;
	}

	public void setRollEnable(boolean rollEnable) {
		this.rollEnable = rollEnable;
	}

	public int getResultLimits() {
		return resultLimits;
	}

	public void setResultLimits(int resultLimits) {
		this.resultLimits = resultLimits;
	}

	public boolean isExceedLimitException() {
		return exceedLimitException;
	}

	public void setExceedLimitException(boolean exceedLimitException) {
		this.exceedLimitException = exceedLimitException;
	}

	public int getResultLimitsWriteLog() {
		return resultLimitsWriteLog;
	}

	public void setResultLimitsWriteLog(int resultLimitsWriteLog) {
		this.resultLimitsWriteLog = resultLimitsWriteLog;
	}

	public int getLobDataTemp() {
		return lobDataTemp;
	}

	public void setLobDataTemp(int lobDataTemp) {
		this.lobDataTemp = lobDataTemp;
	}

	public String toString() {
		return "DasModel [batchSize=" + batchSize + ", fetchSize=" + fetchSize
				+ ", idGeneratorBuffer=" + idGeneratorBuffer + ", rollEnable="
				+ rollEnable + ", resultLimits=" + resultLimits
				+ ", exceedLimitException=" + exceedLimitException
				+ ", resultLimitsWriteLog=" + resultLimitsWriteLog
				+ ", lobDataTemp=" + lobDataTemp + "]";
	}
	
}
