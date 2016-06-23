/**
 * 
 */
package com.primeton.paas.cep.model;

import java.util.HashMap;
import java.util.Map;

import com.primeton.paas.cep.util.StringUtil;

/**
 * Event process statement <br>
 * 
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-11-12
 * 
 * <pre>
 * 	<h3>Example:</h3>
 * 	<code>
 * 	// Usage like java.sql.PreparedStatement
 * 
 * 	PreparedEPS eps = new PreparedEPS("select avg(price) from priceEvent where phone = ? having avg(price) > ?");
 * 	eps.setString(1, "iPhone");
 * 	eps.setInt(2, 3000);
 * 	String statement = eps.exportStatement();
 * 	System.out.println(statement);
 * 	</code>
 * </pre>
 *
 */
public class PreparedEPS implements EPS {
	
	private String statement;
	private Map<Integer, Object> arguments = new HashMap<Integer, Object>();
	
	/**
	 * 
	 */
	public PreparedEPS() {
		super();
	}

	/**
	 * 
	 * @param statement
	 */
	public PreparedEPS(String statement) {
		super();
		this.statement = statement;
	}

	/**
	 * @return the statement
	 */
	public String getStatement() {
		return statement;
	}

	/**
	 * @param statement the statement to set
	 */
	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.cep.model.EPS#setByte(int, byte)
	 */
	public void setByte(int index, byte value) {
		setValue(index, value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.cep.model.EPS#setInt(int, int)
	 */
	public void setInt(int index, int value) {
		setValue(index, value);
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.model.EPS#setLong(int, long)
	 */
	public void setLong(int index, long value) {
		setValue(index, value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.cep.model.EPS#setFloat(int, float)
	 */
	public void setFloat(int index, float value) {
		setValue(index, value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.cep.model.EPS#setDouble(int, double)
	 */
	public void setDouble(int index, double value) {
		setValue(index, value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.cep.model.EPS#setString(int, java.lang.String)
	 */
	public void setString(int index, String value) {
		setValue(index, value);
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.model.EPS#setChar(int, char)
	 */
	public void setChar(int index, char value) {
		setValue(index, value);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.model.EPS#setObject(int, java.lang.Object)
	 */
	public void setObject(int index, Object value) {
		setValue(index, value);
	}

	/**
	 * 
	 * @param index
	 * @param value
	 */
	private void setValue(int index, Object value) {
		int size = getArgSize();
		if (index <= 0 || index > size) {
			throw new IllegalArgumentException("No argument index [" + index + "] for statement [" + statement + "].");
		}
		arguments.put(index, value);
	}
	
	/**
	 * 
	 * @return
	 */
	private int getArgSize() {
		if (StringUtil.isEmpty(statement)) {
			return 0;
		}
		char[] array = statement.toCharArray();
		int count = 0;
		if (array != null && array.length > 0) {
			for (char c : array) {
				if (c == '?') {
					count ++;
				}
			}
		}
		return count;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.cep.model.EPS#exportStatement()
	 */
	public String exportStatement() {
		if (StringUtil.isEmpty(statement)) {
			throw new RuntimeException("Statement is null or blank.");
		}
		int size = getArgSize();
		if (size == 0) {
			return statement;
		}
		String[] array = statement.trim().split("\\?");
		StringBuffer stringBuffer = new StringBuffer(array[0].trim()); // (length = argSize + 1) [not end with ?] else (length = argSize)
		
		for (int i=1; i<=size; i++) {
			Object value = arguments.remove(i);
			if (value == null) {
				throw new RuntimeException("Statement [" + statement + "] index [" + i + "] argument value not set.");
			}
			if (String.class.getName().equals(value.getClass().getName())
					|| Character.class.getName().equals(value.getClass().getName())) {
				stringBuffer.append(" ").append("\"" + value + "\""); // "${value}"
			} else { // number & Object
				stringBuffer.append(" ").append(value);
			}
			if (i<size || !statement.endsWith("?")) {
				stringBuffer.append(" ").append(array[i].trim());
			}
		}
		return stringBuffer.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "PreparedEPS [statement=" + statement + "]";
	}
	
}
