/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author liyanping(mailto:liyp@primeton.com)
 */
public class Order implements Serializable {
	
	private static final long serialVersionUID = -2567091003475820565L;
	
	/** order status */
	public static final int ORDER_STATUS_SUBMITED = 1;
	public static final int ORDER_STATUS_REVOKED = 2;
	public static final int ORDER_STATUS_APPROVED = 3;
	public static final int ORDER_STATUS_REJECTED = 4;
	public static final int ORDER_STATUS_SUCCEED = 5;
	public static final int ORDER_STATUS_FAILED = 6;
	public static final int ORDER_STATUS_DELETED = 7;

	/** order type  */
	public static final String ORDER_TYPE_CREATE_APP = "CREATE_APP";
	
	public static final String ORDER_TYPE_STRETCH_STRATEGY = "STRETCH_STRATEGY_CONFIG";
	
	public static final String ORDER_TYPE_CREATE_SRV = "CREATE_SRV";
	public static final String ORDER_TYPE_DELETE_APP = "DELETE_APP";
	public static final String ORDER_TYPE_DELETE_SRV = "DELETE_SRV";
	public static final String ORDER_TYPE_UPDATE_APP = "UPDATE_APP";
	public static final String ORDER_TYPE_UPDATE_SRV = "UPDATE_SRV";
	
	private String orderId;
	private String orderType;
	private int orderStatus;

	private Date submitTime;
	private Date handleTime;
	private Date finishTime;
	
	private String handlePeriod; 

	private Date beginTime;
	private Date endTime;
	
	private String owner;
	private String handler;
	private String notes;
	
	private List<OrderItem> itemList = new ArrayList<OrderItem>();

	/**
	 * Default. <br>
	 */
	public Order() {
		super();
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
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the orderStatus
	 */
	public int getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the submitTime
	 */
	public Date getSubmitTime() {
		return submitTime;
	}

	/**
	 * @param submitTime the submitTime to set
	 */
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
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
	 * @return the beginTime
	 */
	public Date getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the handler
	 */
	public String getHandler() {
		return handler;
	}

	/**
	 * @param handler the handler to set
	 */
	public void setHandler(String handler) {
		this.handler = handler;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the itemList
	 */
	public List<OrderItem> getItemList() {
		return itemList;
	}

	/**
	 * @param itemList the itemList to set
	 */
	public void setItemList(List<OrderItem> itemList) {
		this.itemList = itemList;
	}
	
	/**
	 * 
	 * @param item
	 */
	public void addItem(OrderItem item) {
		if(item == null) return;
		if(this.itemList == null) {
			this.itemList = new ArrayList<OrderItem>();
		}
		this.itemList.add(item);
	}
	
	/**
	 * 
	 * @param items
	 */
	public void addItems(List<OrderItem> items) {
		if(items == null || items.size() == 0) return;
		if(this.itemList == null) {
			this.itemList = new ArrayList<OrderItem>();
		}
		this.itemList.addAll(items);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "{orderId=" + orderId
			 + " orderType=" + orderType
			 + " orderStatus= "+ orderStatus
			 + " owner=" + owner
			 + " handler=" + handler
			 + "}";
	}

	public String getHandlePeriod() {
		return handlePeriod;
	}

	public void setHandlePeriod(String handlePeriod) {
		this.handlePeriod = handlePeriod;
	}
	
}
