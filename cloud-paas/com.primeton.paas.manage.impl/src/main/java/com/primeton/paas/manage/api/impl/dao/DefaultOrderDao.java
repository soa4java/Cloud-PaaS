/**
 *
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.RandomUtil;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 *
 * @author liyanping(mailto:liyp@primeton.com)
 */
public class DefaultOrderDao {

	private final static String USER_SQL_MAP = "orderSqlMap";

	private static DefaultOrderDao instance = new DefaultOrderDao();

	private static BaseDao baseDao = BaseDao.getInstance();;

	private DefaultOrderDao() {
	}

	public static DefaultOrderDao getInstance() {
		return instance;
	}

	private String getSqlMap(String sqlId) {
		return USER_SQL_MAP + "." + sqlId;
	}

	public void addOrder(Order order) throws DaoException {
		if (null == order) 
			return;
		baseDao.insert(getSqlMap("addOrder"), order); //$NON-NLS-1$
	}

	public void addOrderItems(List<OrderItem> items) throws DaoException {
		if (items == null || items.isEmpty()) {
			return;
		}
		baseDao.insert(getSqlMap("addOrderItems"), items);//$NON-NLS-1$
	}

	public void addItemAttrs(List<OrderItemAttr> attrs) throws DaoException {
		if (attrs == null || attrs.isEmpty()) {
			return;
		}
		baseDao.insert(getSqlMap("addItemAttrs"), attrs);//$NON-NLS-1$
	}

	public void delOrder(String orderId) throws DaoException {
		if (StringUtil.isEmpty(orderId)) {
			return;
		}
		baseDao.delete(getSqlMap("delOrder"), orderId);//$NON-NLS-1$
	}

	public void delOrderItems(String orderId) throws DaoException {
		if (StringUtil.isEmpty(orderId)) {
			return;
		}
		baseDao.delete(getSqlMap("delOrderItems"), orderId);//$NON-NLS-1$
	}

	public void delItemAttrs(String orderId) throws DaoException {
		if (StringUtil.isEmpty(orderId)) {
			return;
		}
		baseDao.delete(getSqlMap("delItemAttrs"), orderId);//$NON-NLS-1$
	}

	public void updateOrder(Order order) throws DaoException {
		if (null == order) {
			return;
		}
		baseDao.update(getSqlMap("updateOrder"), order);//$NON-NLS-1$
	}

	public Order getOrder(String orderId) throws DaoException {
		if (StringUtil.isEmpty(orderId)) {
			return null;
		}
		return baseDao.queryForObject(getSqlMap("getOrderById"), orderId); //$NON-NLS-1$
	}

	public List<OrderItem> getOrderItems(String orderId) throws DaoException {
		if (StringUtil.isEmpty(orderId)) {
			return new ArrayList<OrderItem>();
		}
		return baseDao.queryForList(getSqlMap("getItems"), orderId); //$NON-NLS-1$
	}

	public List<OrderItemAttr> getItemAttrs(String itemId) throws DaoException {
		if (StringUtil.isEmpty(itemId)) {
			return new ArrayList<OrderItemAttr>();
		}
		return baseDao.queryForList(getSqlMap("getItemAttrs"), itemId);//$NON-NLS-1$
	}

	public int getOrderCount(Map<String, Object> criteria) throws DaoException {
		if (criteria == null)
			criteria = new HashMap<String, Object>();
		Integer num = (Integer) baseDao.queryForObject(
				getSqlMap("getOrderCount"), criteria);//$NON-NLS-1$
		return null == num ? -1 : num;
	}

	public List<Order> getOrders(Map<String, Object> criteria)
			throws DaoException {
		if (null == criteria) {
			criteria = new HashMap<String, Object>();
		}
		return baseDao.queryForList(getSqlMap("getOrders"), criteria);//$NON-NLS-1$
	}

	public List<Order> getOrders(Map<String, Object> criteria, IPageCond page)
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getOrders")
				, null == criteria ? new HashMap<String, Object>() : criteria
				, null == page ? new PageCond() : page);//$NON-NLS-1$
	}

	public OrderItem getOrderItem(String itemId) throws DaoException {
		if (StringUtil.isEmpty(itemId)) {
			return null;
		}
		return baseDao.queryForObject(getSqlMap("getOrderItem"), itemId);//$NON-NLS-1$
	}

	public void updateItemStatus(OrderItem item) throws DaoException {
		if (null == item) {
			return;
		}
		baseDao.update(getSqlMap("updateItemStatus"), item);//$NON-NLS-1$
	}

	public void updateItem(OrderItem item) throws DaoException {
		if (null == item) {
			return;
		}
		baseDao.update(getSqlMap("updateItem"), item);//$NON-NLS-1$
	}

	public OrderItem getItemByType(String orderId, String itemType)
			throws DaoException {
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(itemType)) {
			return null;
		}
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put("orderId", orderId); //$NON-NLS-1$
		criteria.put("itemType", itemType); //$NON-NLS-1$
		List<OrderItem> itemList = baseDao.queryForList(
				getSqlMap("getItemByType"), criteria);
		return itemList != null && !itemList.isEmpty() ? itemList.get(0) : null;
	}

	public OrderItemAttr getAttrByName(String orderId, String itemType,
			String attrName) throws DaoException {
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(itemType)
				|| StringUtil.isEmpty(attrName)) {
			return null;
		}
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put("orderId", orderId); //$NON-NLS-1$
		criteria.put("itemType", itemType); //$NON-NLS-1$
		criteria.put("attrName", attrName); //$NON-NLS-1$
		List<OrderItemAttr> attrList = baseDao.queryForList(
				getSqlMap("getAttrByName"), criteria); //$NON-NLS-1$
		return attrList != null && !attrList.isEmpty() ? attrList.get(0) : null;
	}

	/**
	 * 
	 * @param attrList
	 * @throws DaoException
	 */
	public void updateItemAttrs(List<OrderItemAttr> attrList)
			throws DaoException {
		if (null == attrList || attrList.isEmpty()) {
			return;
		}
		for (OrderItemAttr attr : attrList) {
			String attrId = attr.getId();
			if (StringUtil.isEmpty(attrId)) {
				// get & set order_item_attribute_Id
				attrId = RandomUtil.generateId();
				attr.setId(attrId);
			}
			// update
			int result = baseDao.update(getSqlMap("updateItemAttr"), attr); //$NON-NLS-1$
			if (result < 1) {
				baseDao.insert(getSqlMap("insertItemAttr"), attr); //$NON-NLS-1$
			}
		}
	}
	
}
