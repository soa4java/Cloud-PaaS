/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.app.IStretchStrategyManager;
import com.primeton.paas.manage.api.app.StrategyItem;
import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.app.StretchStrategyException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.factory.StretchStrategyManagerFactory;
import com.primeton.paas.manage.api.impl.manager.OrderHandler;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class StretchStrategyConfUtil {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(ServiceOpenUtil.class);

	private static IStretchStrategyManager stretchMgr = StretchStrategyManagerFactory
			.getManager();

	private static IOrderManager orderManager = OrderManagerFactory
			.getManager();
	
	/**
	 * Default. <br>
	 */
	private StretchStrategyConfUtil() {
		super();
	}

	/**
	 * 
	 * @param appName 
	 * @param incItem
	 * @throws StretchStrategyException 
	 */
	public static void setIncStrategy(String appName, OrderItem incItem)
			throws StretchStrategyException {
		if (incItem == null) {
			return;
		}
		StretchStrategy incStrategy = new StretchStrategy();
		//appName
		incStrategy.setAppName(appName);
		//strategy name
		String strategyName = OrderHandler.getItemStringValue(incItem, OrderItemAttr.ATTR_STRETCH_STRATEGY_NAME, "");
		incStrategy.setStrategyName(strategyName);
		//strategy type : increase
		incStrategy.setStrategyType(StretchStrategy.STRATEGY_INCREASE);//���족
		//isEnable
		String isEnable = OrderHandler.getItemStringValue(incItem, OrderItemAttr.ATTR_STRETCH_ISENABLE, "false");
		if (StringUtil.isNotEmpty(isEnable)) {
			incStrategy.setIsEnable(isEnable.equals("true"));
		}
		//stretch size
		int stretchSize = OrderHandler.getItemIntValue(incItem, OrderItemAttr.ATTR_STRETCH_SIZE, -1);
		if (stretchSize > 0) {
			incStrategy.setStretchSize(stretchSize);
		}
		//continued time
		String continuedTime = OrderHandler.getItemStringValue(incItem, OrderItemAttr.ATTR_STRETCH_STRATEGY_CONTINUED_TIME, "-1");
		if (StringUtil.isNotEmpty(continuedTime)) {
			incStrategy.setContinuedTime(Long.parseLong(continuedTime));
		}
		//ignore time
		String ignoreTime = OrderHandler.getItemStringValue(incItem, OrderItemAttr.ATTR_STRETCH_IGNORE_TIME, "-1");
		if (StringUtil.isNotEmpty(ignoreTime)) {
			incStrategy.setIgnoreTime(Long.parseLong(ignoreTime));
		}
		List<StrategyItem> items = new ArrayList<StrategyItem>();
		StrategyItem item = null;
		
		//items : cpu / memory / network /  io / lb
		String cpu = OrderHandler.getItemStringValue(incItem, OrderItemAttr.ATTR_STRETCH_CPU_THRESHOLD, "-1");
		if (StringUtil.isNotEmpty(cpu)) {
			item = new StrategyItem();
			item.setItemType(StrategyItem.TYPE_CPU);
			item.setThreshold(cpu);
			items.add(item);
		}
		
		String memory = OrderHandler.getItemStringValue(incItem, OrderItemAttr.ATTR_STRETCH_MEMORY_THRESHOLD, "-1");
		if (StringUtil.isNotEmpty(memory)){
			item = new StrategyItem();
			item.setItemType(StrategyItem.TYPE_MEMORY);
			item.setThreshold(memory);
			items.add(item);
		}
		/*
		String network = OrderHandler.getItemStringValue(incItem, OrderItemAttr.ATTR_STRETCH_NETWORK_THRESHOLD, "-1");
		if (StringUtil.isNotEmpty(network)) {
			item = new StrategyItem();
			item.setItemType(StrategyItem.TYPE_NETWORK);
			item.setThreshold(network);
			items.add(item);
		}
		
		String ioRate = OrderHandler.getItemStringValue(incItem, OrderItemAttr.ATTR_STRETCH_IO_THRESHOLD, "-1");
		if (StringUtil.isNotEmpty(ioRate)) {
			item = new StrategyItem();
			item.setItemType(StrategyItem.TYPE_IO);
			item.setThreshold(ioRate);
			items.add(item);
		}
		*/
		String lbRate = OrderHandler.getItemStringValue(incItem, OrderItemAttr.ATTR_STRETCH_LB_THRESHOLD, "-1");
		if (StringUtil.isNotEmpty(lbRate)) {
			item = new StrategyItem();
			item.setItemType(StrategyItem.TYPE_LB);
			item.setThreshold(lbRate);
			items.add(item);
		}
		
		incStrategy.setStrategyItems(items);
		
		logger.info("Begin save application increase strategy.{appName:"+appName + ",strategyName="+strategyName+"}.");
		stretchMgr.save(incStrategy);
	}
	
	/**
	 * 
	 * @param decItem
	 * @throws StretchStrategyException 
	 */
	public static void setDecStrategy(String appName,OrderItem decItem) throws StretchStrategyException {
		if (decItem == null) {
			return;
		}
		StretchStrategy decStrategy = new StretchStrategy();
		//appName
		decStrategy.setAppName(appName);
		//strategy name
		String strategyName = OrderHandler.getItemStringValue(decItem, OrderItemAttr.ATTR_STRETCH_STRATEGY_NAME, "");
		decStrategy.setStrategyName(strategyName);
		//strategy type : decrease
		decStrategy.setStrategyType(StretchStrategy.STRATEGY_DECREASE); 
		
		//isEnable
		String isEnable = OrderHandler.getItemStringValue(decItem, OrderItemAttr.ATTR_STRETCH_ISENABLE, "false");
		if (StringUtil.isNotEmpty(isEnable)) {
			decStrategy.setIsEnable(isEnable.equals("true"));
		}
		//stretch size
		int stretchSize = OrderHandler.getItemIntValue(decItem, OrderItemAttr.ATTR_STRETCH_SIZE, -1);
		if (stretchSize > 0) {
			decStrategy.setStretchSize(stretchSize);
		}
		//continued time
		String continuedTime = OrderHandler.getItemStringValue(decItem, OrderItemAttr.ATTR_STRETCH_STRATEGY_CONTINUED_TIME, "");
		if (StringUtil.isNotEmpty(continuedTime)) {
			decStrategy.setContinuedTime(Long.parseLong(continuedTime));
		}
		
		//ignore time
		String ignoreTime = OrderHandler.getItemStringValue(decItem, OrderItemAttr.ATTR_STRETCH_IGNORE_TIME, "");
		if (StringUtil.isNotEmpty(ignoreTime)) {
			decStrategy.setIgnoreTime(Long.parseLong(ignoreTime));
		}
		
		List<StrategyItem> items = new ArrayList<StrategyItem>();
		StrategyItem item = null;
		
		//items : cpu / memory / network /  io / lb
		String cpu = OrderHandler.getItemStringValue(decItem, OrderItemAttr.ATTR_STRETCH_CPU_THRESHOLD, "");
		if (StringUtil.isNotEmpty(cpu)) {
			item = new StrategyItem();
			item.setItemType(StrategyItem.TYPE_CPU);
			item.setThreshold(cpu);
			items.add(item);
		}
		
		String memory = OrderHandler.getItemStringValue(decItem, OrderItemAttr.ATTR_STRETCH_MEMORY_THRESHOLD, "");
		if (StringUtil.isNotEmpty(memory)){
			item = new StrategyItem();
			item.setItemType(StrategyItem.TYPE_MEMORY);
			item.setThreshold(memory);
			items.add(item);
		}
		/*
		String network = OrderHandler.getItemStringValue(decItem, OrderItemAttr.ATTR_STRETCH_NETWORK_THRESHOLD, "");
		if (StringUtil.isNotEmpty(network)) {
			item = new StrategyItem();
			item.setItemType(StrategyItem.TYPE_NETWORK);
			item.setThreshold(network);
			items.add(item);
		}
		
		String ioRate = OrderHandler.getItemStringValue(decItem, OrderItemAttr.ATTR_STRETCH_IO_THRESHOLD, "");
		if (StringUtil.isNotEmpty(ioRate)) {
			item = new StrategyItem();
			item.setItemType(StrategyItem.TYPE_IO);
			item.setThreshold(ioRate);
			items.add(item);
		}
		*/
		String lbRate = OrderHandler.getItemStringValue(decItem, OrderItemAttr.ATTR_STRETCH_LB_THRESHOLD, "");
		if (StringUtil.isNotEmpty(lbRate)) {
			item = new StrategyItem();
			item.setItemType(StrategyItem.TYPE_LB);
			item.setThreshold(lbRate);
			items.add(item);
		}
		decStrategy.setStrategyItems(items);

		//
		logger.info("Save application decrease strategy.{appName:"+appName + ",strategyName="+strategyName+"}.");
		stretchMgr.save(decStrategy);
	}
	
	/**
	 * 
	 * @param appName
	 * @param stretchTypeItem
	 * @throws StretchStrategyException
	 */
	public static void setGlobalStrategy(String appName,
			OrderItem stretchTypeItem) throws StretchStrategyException {
		if (StringUtil.isEmpty(appName)) {
			return;
		}
		StretchStrategy globalStrategy = new StretchStrategy();
		//appName
		globalStrategy.setAppName(appName);
		//strategy name
		globalStrategy.setStrategyName(StretchStrategy.GLOBAL_STRATEGY);
		//
		logger.info("Begin save application to global strategy.{appName:"+appName + "}.");
		stretchMgr.save(globalStrategy);
	}
	
	/**
	 * 
	 * @author liyanping(liyp@primeton.com)
	 *
	 */
	public static class IncStrategyConfThread implements Runnable {
		
		private String orderId;
		private OrderItem incItem;
		
		/**
		 * 
		 * @param orderId
		 * @param incItem
		 */
		public IncStrategyConfThread(String orderId, OrderItem incItem){
			this.orderId = orderId;
			this.incItem = incItem;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			if (incItem == null) {
				return;
			}
			String itemId = incItem.getItemId();
			String appName = OrderHandler.getItemStringValue(incItem, OrderItemAttr.ATTR_APP_NAME, "");
			
			try {
				setIncStrategy(appName,incItem);
			} catch (Exception e) {
				incItem.setFinishTime(new Date());
				incItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				orderManager.updateItem(incItem); //update item handle endtime
				
				OrderHandler.notifyListeners(orderId,itemId,null);
				
			}
			
			long end = System.currentTimeMillis();
			logger.info("End set increase stretch strategy.{appName="+ appName +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
		
	}
	
	/**
	 * 
	 * @author liyanping(liyp@primeton.com)
	 *
	 */
	public static class DecStrategyConfThread implements Runnable {
		
		private String orderId;
		private OrderItem decItem;
		
		/**
		 * 
		 * @param orderId
		 * @param decItem
		 */
		public DecStrategyConfThread(String orderId, OrderItem decItem){
			this.orderId = orderId;
			this.decItem = decItem;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			if (decItem == null) {
				return;
			}
			String itemId = decItem.getItemId();
			String appName = OrderHandler.getItemStringValue(decItem, OrderItemAttr.ATTR_APP_NAME, "");
			try {
				setDecStrategy(appName,decItem);
			} catch (Exception e) {
				decItem.setFinishTime(new Date());
				decItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				orderManager.updateItem(decItem); //update item handle endtime
				
				OrderHandler.notifyListeners(orderId,itemId,null);
			}
			long end = System.currentTimeMillis();
			logger.info("End set increase stretch strategy.{appName="+ appName +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
		
	}

}
