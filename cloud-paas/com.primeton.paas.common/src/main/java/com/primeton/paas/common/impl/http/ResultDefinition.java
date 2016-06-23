/**
 * 
 */
package com.primeton.paas.common.impl.http;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ResultDefinition implements java.io.Serializable {
	
	private static final long serialVersionUID = 3381731770524810651L;

	private Object result = null;
	
	private Throwable exception = null;

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	@SuppressWarnings("unchecked")
	public <T> T getResult() {
		return (T)result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
}