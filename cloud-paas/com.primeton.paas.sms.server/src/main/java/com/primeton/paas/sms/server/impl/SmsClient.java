/*package com.primeton.paas.sms.server.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.cesium.common.api.util.StringUtil;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.momentek.smsapi.MessageClient;
import com.momentek.smsapi.MessageListener;
import com.momentek.smsapi.SmsDR;
import com.momentek.smsapi.SmsMO;
import com.momentek.smsapi.SmsMT;
import com.momentek.smsapi.SmsUtil;
import com.primeton.paas.sms.model.SmsConfig;
import com.primeton.paas.sms.model.SmsResult;
import com.primeton.paas.sms.server.conf.SmsConfUtil;
import com.primeton.paas.sms.spi.SmsConstants;
import com.primeton.paas.sms.spi.SmsRuntimeException;

*//**
 *
 * @author liyanping(liyp@primeton.com)
 *//*
public class SmsClient implements MessageListener {

	private ILogger log = LoggerFactory.getLogger(SmsClient.class);
	
	private MessageClient client = null;
	
	private static SmsConfig smsConf = null;

	private int drCount = 0;
	
	private int mtCount = 0;
	
	public String serviceCode = "";
	public String extendCode = "";
	public int pause=0;
	public int registeredDelivery=0;
	
	public SmsClient() {
		initialize();
	}
	
	public void initialize(){
		if (client != null && client.isBound()) {
			return;
		}
		smsConf = SmsConfUtil.getSmsConf();
		if(smsConf == null){
			throw new RuntimeException("Init SMS Conf from zookeeper failed.");
		}
		
		String protocol = StringUtil.isEmpty(smsConf.getProtocol()) ? SmsConstants.PROTOCOL_MMPP_2_0 :smsConf.getProtocol();
		try {
			client = MessageClient.createInstance(protocol, this);
		} catch (Exception e) {
			throw new RuntimeException("Create Message client intance failed. " + e.getMessage());
		} 
		
		client.setUsername(smsConf.getUsername());
		client.setPassword(smsConf.getPassword());
		
		client.setRemoteAddr(smsConf.getRemoteAddr());
		client.setRemotePort(smsConf.getRemotePort());
		
		int version = smsConf.getVersion() <=0 ? SmsConstants.DEFAULT_VERSION : smsConf.getVersion();
		client.setVersion(version);
		
		client.setSocketTimeout(smsConf.getSocketTimeout());
		
		
		serviceCode = smsConf.getServiceCode();
		extendCode = smsConf.getExtendCode();
		pause = smsConf.getPause();
		registeredDelivery = smsConf.getRegisteredDelivery();
		
		log.info("smsclient initialize success:{ serviceCode="+serviceCode +",extendCode="+extendCode + ",pause="+pause + ",registeredDelivery="+registeredDelivery);
		
		//try connection three times
		for (int i = 0; i < 3; i++) {
			if(client.connect()){
				return;
			}
		}
		throw new RuntimeException("Connection failed." );
	}
	
	public void onBindAck(int arg0) {
		log.info("onBound: " + arg0); 
	}

	public void onClosed(ArrayList arg0) {
		log.info("onClosed: Not Deliveried��" + arg0);
		if(arg0!=null&&arg0.size()!=0){
			Iterator iter = arg0.iterator();
			while(iter.hasNext()){
				SmsMT mt = (SmsMT) iter.next();
				log.info("Result    : "+mt.getResult());
				log.info("DestAddr  : "+mt.getDestAddr().toString());
				
			}
		}
	}

	public void onSmsDR(SmsDR dr) {
		drCount++;
		log.info("onSmsDR " + drCount);
		log.info("Source-Addr: " + dr.getSourceAddr()); 
		log.info("Message-ID : " + dr.getMessageID());  
		log.info("ErrorCode  : " + dr.getErrorCode());  
		log.info("Receipt    : " + dr.getReceipt()); 
		log.info("Status     : " + dr.getStatus());  
	}
	
	public void onSmsMO(SmsMO mo) {
		log.info("onSmsMO");
		log.info("Data-Coding  : " + mo.getDataCoding()); 
		log.info("Dest-Addr    : " + mo.getDestAddr());
		log.info("Content      : " + SmsUtil.getLetter(mo)); 
		log.info("Service-Code : " + mo.getServiceCode()); 
		log.info("Source-Addr  : " + mo.getSourceAddr()); 
	}

	public void onSmsMTAck(SmsMT ack) {
		mtCount++;
		log.info("onSmsMTAck " + mtCount);
		log.info("Message-ID: " + ack.getMessageID());  
		log.info("Result    : " + ack.getResult()); 

	}
	
	@SuppressWarnings("unchecked")
	public List<SmsResult> send(String number, String content,long timeout) throws SmsRuntimeException {
		if (number == null || number.isEmpty()) {
			log.error("Send Message error.{number is null.}");
			throw new SmsRuntimeException("Send Message error.{number is null.}");
		}
		log.info("in SmsClient class {number="+number+", content="+content + ", timeout="+timeout);
		if (timeout < 0 ) {
			timeout = 0 ;  
		}
		client.setMaxChineseChars(61);  
		client.setMaxEnglishChars(149); 
		
		ArrayList<SmsMT> list = client.send("", number, content, 0, registeredDelivery,	serviceCode, timeout, null, pause);
		List<SmsResult> restList = new ArrayList<SmsResult>();
		if (list != null  && !list.isEmpty()) {
			for(SmsMT m:list){
				SmsResult result = new SmsResult();
				log.info("MessageID   : " + m.getMessageID());
				log.info("PacketTotal : "+m.getPacketTotal());
				log.info("PacketNumber: " + m.getPacketNumber());
				log.info("Result      : " + m.getResult());
				log.info("DataCoding  : " + m.getDataCoding());
				log.info("msgLength   : " + m.getContent().length);
				
				try {
					if(m.getDataCoding() == SmsConstants.UTF){
						String msg = new String(m.getContent(), SmsConstants.UTF_CODE);
						log.info("msgContent  :  " + msg);
					}else if(m.getDataCoding() == SmsConstants.GB){
						String msg = new String(m.getContent(),SmsConstants.GB_CODE);
						log.info("msgContent  :  " + msg);
					}else{
						log.info("msgContent  :  " + new String(m.getContent()));
					}
					
				} catch (UnsupportedEncodingException e) {
					log.warn(e.getMessage());
				}
				
				result = covertMtToResult(m);//convert type
				restList.add(result);
			}
		}
		return restList;
		
		
		SmsMT mt = new SmsMT();

		mt.setPacketTotal(1);

		mt.setPacketNumber(1);

		mt.setRegisteredDelivery(1);

		mt.setPriority(0);

		mt.setServiceCode(serviceCode);
		
		// mt.setPayer(null);
		
		//mt.setEnglishMessage( content );

		//mt.setUnicodeMessage( content );
		
		mt.setGBKMessage(content);
		//mt.setEnglishMessage(content);
		
		//mt.setContent(content.getBytes());
		//mt.setDataCoding(15);
		
		// mt.setProtocolID( 0 );

		// mt.setValidityPeriod( null );

		// mt.setScheduleTime( null );

		// mt.setSourceAddr( null );

		String[] addresses = number.split(";");
		for (int i = 0; i < addresses.length; i++) {
			mt.addDestAddr( addresses[i] );
		}
		// mt.setLinkID( null );
		// mt.setEsmClass( 0 );
		boolean result = client.send(mt, timeout);
		if (result) {
			log.info(result); 
			log.info(mt.getResult()); 
			log.info(mt.getMessageID()); 
		} else {
			log.info(result);
		}
		
		List<SmsResult> retList = new ArrayList<SmsResult>();
		SmsResult smsRet = covertMtToResult(mt,result);//convert type
		retList.add(smsRet);
		return retList;
	}
	
	public void close() {
		client.close();
	}
	
	public SmsResult covertMtToResult(SmsMT mt, boolean result){
		if (mt ==null) {
			return null;
		}
		
		SmsResult ret = new SmsResult();
		ret.setSendResult(result);
		ret.setMessageID(mt.getMessageID());
		ret.setResult(mt.getResult());

		ret.setContent(mt.getContent());
		ret.setPacketNumber(mt.getPacketNumber());
		ret.setPacketTotal(mt.getPacketTotal());
		ret.setServiceCode(mt.getServiceCode());
		
		ret.setDataCoding(mt.getDataCoding());
		ret.setDestAddr(mt.getDestAddr());
		ret.setEsmClass(mt.getEsmClass());
		ret.setFeeCode(mt.getFeeCode());
		ret.setFeeType(mt.getFeeType());
		ret.setLinkID(mt.getLinkID());
		ret.setMessageSource(mt.getMessageSource());
		ret.setPayer(mt.getPayer());
		ret.setPriority(mt.getPriority());
		ret.setScheduleTime(mt.getScheduleTime());
		ret.setSourceAddr(mt.getSourceAddr());
		ret.setTag(mt.getTag());
		ret.setValidityPeriod(mt.getValidityPeriod());
		
		return ret;
	}
	
}
*/