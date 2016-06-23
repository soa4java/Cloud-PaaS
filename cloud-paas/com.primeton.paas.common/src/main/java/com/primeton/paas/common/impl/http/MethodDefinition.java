/**
 * 
 */
package com.primeton.paas.common.impl.http;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MethodDefinition implements java.io.Serializable {
	
	private static final long serialVersionUID = 1759700366262944793L;
	
	private String objectName = null;
	
	private String className = null;
	
	private Object target = null;
	
	private String methodName = null;
	
	private Object[] args = new Object[0];
	
	private String[] paramClassNames = new String[0];
	
	private boolean isNeedResponse = true;
	
	public MethodDefinition() {
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String[] getParamClassNames() {
		return paramClassNames;
	}

	public void setParamClassNames(String[] paramClassNames) {
		this.paramClassNames = paramClassNames;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public boolean isNeedResponse() {
		return isNeedResponse;
	}

	public void setNeedResponse(boolean isNeedResponse) {
		this.isNeedResponse = isNeedResponse;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
}