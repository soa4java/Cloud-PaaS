/**
 * 
 */
package com.primeton.paas.sms.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class SmsResult implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 717479095985026385L;
	
	private boolean sendResult; 
	
	private String messageID; 
	
	private int packetTotal;  
	
	private int packetNumber; 
	
	private int registeredDelivery; 
	
	private int priority; 
	
	private String serviceCode; 
	
	private String payer; 
	
	private int esmClass; 
	
	private int protocolID;  
	
	private int dataCoding; 

	private String sourceAddr; 
	
	private String scheduleTime; 
	
	private String validityPeriod; 
	
	private int result;
	
	private ArrayList destAddr; 
	
	private byte[] content; 
	
	private String linkID; 
	
	private String messageSource;
	
	private int feeType;
	
	private int feeCode;
	
	private ArrayList contents = new ArrayList();
	
	private Object tag;
	
	public boolean isSendResult() {
		return sendResult;
	}

	public void setSendResult(boolean sendResult) {
		this.sendResult = sendResult;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public ArrayList getContents() {
		return contents;
	}

	public void setContents(ArrayList contents) {
		this.contents = contents;
	}

	public int getDataCoding() {
		return dataCoding;
	}

	public void setDataCoding(int dataCoding) {
		this.dataCoding = dataCoding;
	}

	public ArrayList getDestAddr() {
		return destAddr;
	}

	public void setDestAddr(ArrayList destAddr) {
		this.destAddr = destAddr;
	}

	public int getEsmClass() {
		return esmClass;
	}

	public void setEsmClass(int esmClass) {
		this.esmClass = esmClass;
	}

	public int getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(int feeCode) {
		this.feeCode = feeCode;
	}

	public int getFeeType() {
		return feeType;
	}

	public void setFeeType(int feeType) {
		this.feeType = feeType;
	}

	public String getLinkID() {
		return linkID;
	}

	public void setLinkID(String linkID) {
		this.linkID = linkID;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(String messageSource) {
		this.messageSource = messageSource;
	}

	public int getPacketNumber() {
		return packetNumber;
	}

	public void setPacketNumber(int packetNumber) {
		this.packetNumber = packetNumber;
	}

	public int getPacketTotal() {
		return packetTotal;
	}

	public void setPacketTotal(int packetTotal) {
		this.packetTotal = packetTotal;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getProtocolID() {
		return protocolID;
	}

	public void setProtocolID(int protocolID) {
		this.protocolID = protocolID;
	}

	public int getRegisteredDelivery() {
		return registeredDelivery;
	}

	public void setRegisteredDelivery(int registeredDelivery) {
		this.registeredDelivery = registeredDelivery;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getSourceAddr() {
		return sourceAddr;
	}

	public void setSourceAddr(String sourceAddr) {
		this.sourceAddr = sourceAddr;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public String getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	
}