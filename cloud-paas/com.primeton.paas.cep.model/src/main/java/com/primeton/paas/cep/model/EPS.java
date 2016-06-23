/**
 * 
 */
package com.primeton.paas.cep.model;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface EPS {
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	void setByte(int index, byte value);
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	void setInt(int index, int value);
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	void setLong(int index, long value);
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	void setFloat(int index, float value);
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	void setDouble(int index, double value);
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	void setString(int index, String value);
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	void setChar(int index, char value);
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	void setObject(int index, Object value);
	
	/**
	 * 
	 * @return
	 */
	String exportStatement();

}
