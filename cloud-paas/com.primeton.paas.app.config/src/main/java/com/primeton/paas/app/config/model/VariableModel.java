/**
 * 
 */
package com.primeton.paas.app.config.model;

import com.primeton.paas.app.config.IConfigModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class VariableModel implements IConfigModel {

	private static final long serialVersionUID = -2657880136630597540L;

	public static final String DATA_TYPE_STRING = "String";
	public static final String DATA_TYPE_BOOLEAN = "boolean";
	public static final String DATA_TYPE_INT = "int";
	public static final String DATA_TYPE_LONG = "long";
	public static final String DATA_TYPE_DOUBLE = "double";
	public static final String DATA_TYPE_FLOAT = "float";
	public static final String DATA_TYPE_BYTE = "byte";
	public static final String DATA_TYPE_DATE = "Date";
	public static final String DATA_TYPE_DEFAULT = DATA_TYPE_STRING;
	
	private String name;
	private String value;
	private String valueType;
	private String desc;
	
	/**
	 * Default. <br>
	 */
	public VariableModel() {
		super();
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public VariableModel(String name, String value) {
		super();
		this.name = name;
		this.value = value;
		this.valueType = DATA_TYPE_DEFAULT;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @param valueType
	 */
	public VariableModel(String name, String value, String valueType) {
		super();
		this.name = name;
		this.value = value;
		this.valueType = valueType;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @param valueType
	 * @param desc
	 */
	public VariableModel(String name, String value, String valueType, String desc) {
		super();
		this.name = name;
		this.value = value;
		this.valueType = valueType;
		this.desc = desc;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the valueType
	 */
	public String getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 
	 * @param defaultValue
	 * @return
	 */
	public byte getByteValue(byte defaultValue) {
		if (this.value != null) {
			try {
				byte val = Byte.parseByte(value);
				return val;
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param defaultValue
	 * @return
	 */
	public long getLongValue(long defaultValue) {
		if (this.value != null) {
			try {
				long val = Long.parseLong(value);
				return val;
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param defaultValue
	 * @return
	 */
	public int getIntValue(int defaultValue) {
		if (this.value != null) {
			try {
				return Integer.parseInt(this.value);
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param defaultValue
	 * @return
	 */
	public float getFloatValue(float defaultValue) {
		if (this.value != null) {
			try {
				float val = Float.parseFloat(value);
				return val;
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param defaultValue
	 * @return
	 */
	public double getDoubleValue(double defaultValue) {
		if (this.value != null) {
			try {
				double val = Double.parseDouble(value);
				return val;
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param defaultValue
	 * @return
	 */
	public boolean getBooleanValue(boolean defaultValue) {
		if (this.value != null) {
			return Boolean.parseBoolean(this.value);
		}
		return defaultValue;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer(super.toString()).append(" { name:").append(this.name)
			.append(", value:").append(this.value)
			.append(", valueType:").append(this.valueType)
			.append(", desc:").append(this.desc)
			.append(" }").toString();
	}
	
}
