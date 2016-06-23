/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.List;

import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.MySQLService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Mysql数据库服务管理类
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class MySqlServiceMgrUtil {

	private static IServiceQuery srvQueryMgr = ServiceManagerFactory.getServiceQuery();

	/**
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static MySQLService[] queryMysqlServices(MySQLService criteria,
			PageCond page) {
		List<MySQLService> serviceList = srvQueryMgr.getByOwner("",
				MySQLService.TYPE, page, MySQLService.class);
		return serviceList.toArray(new MySQLService[serviceList.size()]);
	}

	/**
	 * 根据集群标识，获取集群内Mysql服务列表
	 * 
	 * @param clusterId
	 * @return
	 */
	public static List<MySQLService> getServicesByCluster(String clusterId) {
		List<MySQLService> servicelist = srvQueryMgr.getByCluster(clusterId,
				MySQLService.class);
		for (MySQLService mysqlService : servicelist) {
			String url = getMySQLURL(mysqlService.getIp());
			mysqlService.getAttributes().put("phpmyadminUrl", url);
		}
		return servicelist;
	}

	/**
	 * 获取登陆phpMyAdmin的URL <br>
	 * 
	 * @param ip
	 *            mysql主机ip
	 * @return
	 */
	private static String getMySQLURL(String ip) {
		String url = SystemVariable.getPhpMyAdminURL();
		if (StringUtil.isEmpty(url)) {
			return null;
		}
		if (ip == null)
			return url;
		String[] array = ip.split("\\.");
		if (array.length != 4)
			return url;
		// 192.168.100.30 => index = 192 * 1000000000 + 168 * 1000000 + 100 *
		// 1000 + 30 => index = 192168100030
		long index = Long.parseLong(array[0]) * 1000000000L
				+ Long.parseLong(array[1]) * 1000000L
				+ Long.parseLong(array[2]) * 1000L + Long.parseLong(array[3]);
		url += "?server=" + index; //$NON-NLS-1$
		return url;
	}

}