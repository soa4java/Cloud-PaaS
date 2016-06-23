/**
 * 
 */
package com.primeton.paas.cep.spi;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-11-12
 *
 */
public class TableId {
	
	private String tableName;
	private long beginId;
	private int increment = 10;
	private long nextId;
	
	/**
	 * 
	 */
	public TableId() {
		super();
	}

	/**
	 * @param tableName
	 * @param beginId
	 * @param increment
	 * @param nextId
	 */
	public TableId(String tableName, long beginId, int increment, long nextId) {
		super();
		this.tableName = tableName;
		this.beginId = beginId;
		this.increment = increment;
		this.nextId = nextId;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the beginId
	 */
	public long getBeginId() {
		return beginId;
	}

	/**
	 * @param beginId the beginId to set
	 */
	public void setBeginId(long beginId) {
		this.beginId = beginId;
	}

	/**
	 * @return the increment
	 */
	public int getIncrement() {
		return increment;
	}

	/**
	 * @param increment the increment to set
	 */
	public void setIncrement(int increment) {
		this.increment = increment;
	}

	/**
	 * @return the nextId
	 */
	public long getNextId() {
		return nextId;
	}

	/**
	 * @param nextId the nextId to set
	 */
	public void setNextId(long nextId) {
		this.nextId = nextId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "TableId [tableName=" + tableName + ", beginId=" + beginId
				+ ", increment=" + increment + ", nextId=" + nextId + "]";
	}

}
