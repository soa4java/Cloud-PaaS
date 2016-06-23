/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.primeton.paas.manage.api.cluster.EsbCluster;
import com.primeton.paas.manage.api.service.CEPEngineService;
import com.primeton.paas.manage.api.service.CardBinService;
import com.primeton.paas.manage.api.service.CollectorService;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.service.HadoopService;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.JobCtrlService;
import com.primeton.paas.manage.api.service.KeepalivedService;
import com.primeton.paas.manage.api.service.MailService;
import com.primeton.paas.manage.api.service.MemcachedService;
import com.primeton.paas.manage.api.service.MsgQueueService;
import com.primeton.paas.manage.api.service.MySQLService;
import com.primeton.paas.manage.api.service.NginxService;
import com.primeton.paas.manage.api.service.OpenAPIService;
import com.primeton.paas.manage.api.service.RedisService;
import com.primeton.paas.manage.api.service.SVNRepositoryService;
import com.primeton.paas.manage.api.service.SVNService;
import com.primeton.paas.manage.api.service.SmsService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.service.WarService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
@SuppressWarnings("deprecation")
public class OrderItem implements Serializable {

	private static final long serialVersionUID = -9217053119578357775L;
	
	/** item type */
	// Define by LiYanping
	public static final String ITEM_TYPE_DEFAULT = "Default";
	public static final String ITEM_TYPE_APP = "App";
	public static final String ITEM_TYPE_JETTY = JettyService.TYPE;
	public static final String ITEM_TYPE_MEMCACHED = MemcachedService.TYPE;
	public static final String ITEM_TYPE_NGINX = NginxService.TYPE;
	public static final String ITEM_TYPE_HAPROXY = HaProxyService.TYPE;
	public static final String ITEM_TYPE_SVN = SVNService.TYPE;
	public static final String ITEM_TYPE_SVN_REPO = SVNRepositoryService.TYPE;
	public static final String ITEM_TYPE_MYSQL = MySQLService.TYPE;
	public static final String ITEM_TYPE_WAR = WarService.TYPE;
	public static final String ITEM_TYPE_OPENAPI = OpenAPIService.TYPE;
	@Deprecated
	public static final String ITEM_TYPE_SMS = SmsService.TYPE;
	@Deprecated
	public static final String ITEM_TYPE_CARDBIN = CardBinService.TYPE;
	public static final String ITEM_TYPE_MAIL = MailService.TYPE;
	public static final String ITEM_TYPE_KEEPALIVED = KeepalivedService.TYPE;
	public static final String ITEM_TYPE_CEPENGINE = CEPEngineService.TYPE;
	public static final String ITEM_TYPE_COLLECTOR = CollectorService.TYPE;
	public static final String ITEM_TYPE_SESSION = "Session";
	public static final String ITEM_TYPE_TOMCAT = TomcatService.TYPE;
	public static final String ITEM_TYPE_REDIS = RedisService.TYPE;
	public static final String ITEM_TYPE_HADOOP = HadoopService.TYPE;
	public static final String ITEM_TYPE_JOBCTRL = JobCtrlService.TYPE;
	public static final String ITEM_TYPE_ESB = EsbCluster.TYPE;
	public static final String ITEM_TYPE_MSGQUEUE = MsgQueueService.TYPE;
	
	/** app stretch strategy */
	public static final String ITEM_TYPE_STRETCH_INC_STRATEGY = "IncStrategy";
	public static final String ITEM_TYPE_STRETCH_DEC_STRATEGY = "DecStrategy";
	public static final String ITEM_TYPE_STRETCH_STRATEGY_NAME = "StrategyName";
	
	
	/** item status */
	public static final int ITEM_STATUS_INIT = -1;
	public static final int ITEM_STATUS_SUCCEED = 0;
	public static final int ITEM_STATUS_FAILED = 1;
	public static final int ITEM_STATUS_PROCESSING = 2;
	
	
	private String itemId;
	private String orderId;
	private String itemType;
	private int itemStatus;
	
	private Date handleTime;
	private Date finishTime;
	
	private String handlePeriod;
	
	private List<OrderItemAttr> attrList = new ArrayList<OrderItemAttr>();
	
	/**
	 * 
	 */
	public OrderItem() {
		super();
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return the itemStatus
	 */
	public int getItemStatus() {
		return itemStatus;
	}

	/**
	 * @return the handleTime
	 */
	public Date getHandleTime() {
		return handleTime;
	}

	/**
	 * @param handleTime the handleTime to set
	 */
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	/**
	 * @return the finishTime
	 */
	public Date getFinishTime() {
		return finishTime;
	}

	/**
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	
	/**
	 * @param itemStatus the itemStatus to set
	 */
	public void setItemStatus(int itemStatus) {
		this.itemStatus = itemStatus;
	}
	
	/**
	 * @return the attrList
	 */
	public List<OrderItemAttr> getAttrList() {
		return attrList;
	}

	/**
	 * @param attrList the attrList to set
	 */
	public void setAttrList(List<OrderItemAttr> attrList) {
		this.attrList = attrList;
	}

	public String getHandlePeriod() {
		return handlePeriod;
	}

	public void setHandlePeriod(String handlePeriod) {
		this.handlePeriod = handlePeriod;
	}
	
	/**
	 * add order item attribute
	 * @param attr
	 */
	public void addItemAttr(OrderItemAttr attr){
		if (attr == null) return;
		
		if (this.attrList == null) {
			this.attrList = new ArrayList<OrderItemAttr>();
		}
		this.attrList.add(attr);
	}

	public String toString() {
		return "OrderItem [itemId=" + itemId + ", orderId=" + orderId
				+ ", itemType=" + itemType + ", itemStatus=" + itemStatus
				+ ", handleTime=" + handleTime + ", finishTime=" + finishTime
				+ ", handlePeriod=" + handlePeriod + ", attrList=" + attrList
				+ "]";
	}
	
}
