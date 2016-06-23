/*
 * Copyright 2013 Primeton.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.primeton.paas.app.example;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.api.cache.CacheServiceException;
import com.primeton.paas.app.api.cache.CacheServiceFactory;
import com.primeton.paas.app.api.cache.ICacheService;
import com.primeton.paas.app.api.jdbc.DataSourceFactory;
import com.primeton.paas.app.api.log.UserLoggerFactory;

/**
 * ExampleUtils. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ExampleUtils {
	
	private static ILogger logger = UserLoggerFactory.getLogger(ExampleUtils.class); 
	
	private ExampleUtils() {
		super();
	}

	/**
	 * 
	 * @param level
	 * @param message
	 */
	public static void log(String level, String message) {
		if ("info".equalsIgnoreCase(level)) {
			logger.info(message);
		} else if ("debug".equalsIgnoreCase(level)) {
			logger.debug(message);
		} else if ("error".equalsIgnoreCase(level)) {
			logger.error(message);
		} else if ("warn".equalsIgnoreCase(level)) {
			logger.warn(message);
		} else {
			logger.info(message);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Connection getConnection(String name) {
		DataSource dataSource = DataSourceFactory.getDataSource(null == name || name.trim().length() == 0 ? "default" : name);
		try {
			return null == dataSource ? null : dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param foo
	 */
	public static void addFoo(Foo foo) {
		if (null == foo || null == foo.getId()) {
			return;
		}
		if (null != getFoo(foo.getId())) {
			return;
		}
		Connection conn = getConnection("default"); //$NON-NLS-1$
		if (null == conn) {
			return;
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("insert into foo (id, name) values (?, ?)"); //$NON-NLS-1$
			stmt.setString(1, foo.getId());
			stmt.setString(2, foo.getName());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, stmt, conn);
		}
	}
	
	public static void removeFoo(String id) {
		if (null == id) {
			return;
		}
		init();
		Connection conn = getConnection("default"); //$NON-NLS-1$
		if (null == conn) {
			return;
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("delete from foo where id = ?");
			stmt.setString(1, id);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, stmt, conn);
		}
	}
	
	public static List<Foo> getFoos() {
		init();
		List<Foo> foos = new ArrayList<Foo>();
		Connection conn = getConnection("default"); //$NON-NLS-1$
		if (null == conn) {
			return foos;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("select id, name from foo"); //$NON-NLS-1$
			rs = stmt.executeQuery();
			while (rs.next()) {
				foos.add(new Foo(rs.getString("id"), rs.getString("name"))); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, stmt, conn);
		}
		return foos;
	}
	
	public static Foo getFoo(String id) {
		init();
		if (null == id || id.trim().length() == 0) {
			return null;
		}
		Connection conn = getConnection("default"); //$NON-NLS-1$
		if (null == conn) {
			return null;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("select id, name from foo where id = '" + id + "'"); //$NON-NLS-1$
			rs = stmt.executeQuery();
			while (rs.next()) {
				return new Foo(rs.getString("id"), rs.getString("name")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, stmt, conn);
		}
		return null;
	}
	
	private static void init() {
		Connection conn = getConnection("default"); //$NON-NLS-1$
		if (null == conn) {
			return;
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("select count(*) from foo"); //$NON-NLS-1$
			stmt.execute();
		} catch (SQLException e) {
			// e.printStackTrace();
			try {
				stmt = conn.prepareStatement("create table foo (id varchar(16), name varchar(128), primary key(id))"); // //$NON-NLS-1$
				stmt.execute();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			close(null, stmt, conn);
		}
		
	}
	
	public static void close(ResultSet resultSet, Statement statement, Connection connection) {
		if (null != resultSet) {
			try {
				resultSet.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (null != statement) {
			try {
				statement.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (null != connection) {
			try {
				connection.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void putCache(String key, Serializable value, int expireTime) {
		if (null == key || key.trim().length() == 0) {
			return;
		}
		try {
			ICacheService cacheService = CacheServiceFactory.getCacheService();
			if (expireTime <=0) {
				cacheService.put(key, value);
			} else {
				cacheService.put(key, value, expireTime);
			}
		} catch (CacheServiceException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static <T extends Serializable> T getCache(String key) {
		if (null == key || key.trim().length() == 0) {
			return null;
		}
		try {
			ICacheService cacheService = CacheServiceFactory.getCacheService();
			return cacheService.get(key);
		} catch (CacheServiceException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
