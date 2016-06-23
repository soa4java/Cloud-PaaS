/**
 * 
 */
package com.primeton.paas.console.common.monitor;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class IOInfo {
	
	private String inputs;
	private String outputs;
	private String times;
	
	public IOInfo() {
		super();
	}
	
	public IOInfo(String inputs, String outputs, String times) {
		super();
		this.inputs = inputs;
		this.outputs = outputs;
		this.times = times;
	}

	public String getInputs() {
		return inputs;
	}
	
	public void setInputs(String inputs) {
		this.inputs = inputs;
	}
	
	public String getOutputs() {
		return outputs;
	}
	
	public void setOutputs(String outputs) {
		this.outputs = outputs;
	}
	
	public String getTimes() {
		return times;
	}
	
	public void setTimes(String times) {
		this.times = times;
	}

	public String toString() {
		return "IOInfo [inputs=" + inputs + ", outputs=" + outputs + ", times="
				+ times + "]";
	}
	
}
