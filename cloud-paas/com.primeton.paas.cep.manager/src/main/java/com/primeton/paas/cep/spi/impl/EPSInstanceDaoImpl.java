/**
 * 
 */
package com.primeton.paas.cep.spi.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.primeton.paas.cep.api.ConnectionFactory;
import com.primeton.paas.cep.model.EPSInstance;
import com.primeton.paas.cep.spi.EPSInstanceDao;
import com.primeton.paas.cep.util.DBUtil;
import com.primeton.paas.cep.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EPSInstanceDaoImpl implements EPSInstanceDao {
	
	private static final String TABLE_NAME = "cep_eps_instance";
	private String insertSQL = "INSERT INTO " + TABLE_NAME + " (id, enable, eps, event_name, listeners, create_time, start_time) VALUES(?, ?, ?, ?, ?, ?, ?)";
	private String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
	private String updateSQL = "UPDATE " + TABLE_NAME + " SET enable = ?, eps = ?, event_name = ?, listeners = ?, create_time = ?, start_time = ? WHERE id = ?";
	private String selectOneSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
	private String selectAllSQL = "SELECT * FROM " + TABLE_NAME;

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.EPSInstanceDao#add(com.primeton.paas.cep.model.EPSInstance)
	 */
	public void add(EPSInstance instance) throws SQLException {
		if (instance == null) {
			return;
		}
		if (StringUtil.isEmpty(instance.getId())) {
			instance.setId(getNewId());
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(insertSQL);
			stmt.setString(1, instance.getId());
			stmt.setString(2, instance.getEnable());
			stmt.setString(3, instance.getEps());
			stmt.setString(4, instance.getEventName());
			stmt.setString(5, instance.getListeners());
			stmt.setLong(6, instance.getCreateTime());
			stmt.setLong(7, instance.getStartTime());
			stmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(conn, stmt, null);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.EPSInstanceDao#delete(java.lang.String)
	 */
	public void delete(String id) throws SQLException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(deleteSQL);
			stmt.setString(1, id);
			stmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(conn, stmt, null);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.EPSInstanceDao#update(com.primeton.paas.cep.model.EPSInstance)
	 */
	public void update(EPSInstance instance) throws SQLException {
		if (instance == null) {
			return;
		}
		if (StringUtil.isEmpty(instance.getId())) {
			return;
		}
		
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(updateSQL);
			stmt.setString(1, instance.getEnable());
			stmt.setString(2, instance.getEps());
			stmt.setString(3, instance.getEventName());
			stmt.setString(4, instance.getListeners());
			stmt.setLong(5, instance.getCreateTime());
			stmt.setLong(6, instance.getStartTime());
			stmt.setString(7, instance.getId());
			stmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(conn, stmt, null);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.EPSInstanceDao#get(java.lang.String)
	 */
	public EPSInstance get(String id) throws SQLException {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EPSInstance instance;
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(selectOneSQL);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			instance = null;
			if (rs.next()) {
				instance = new EPSInstance();
				instance.setId(id);
				instance.setEnable(rs.getString("enable"));
				instance.setEps(rs.getString("eps"));
				instance.setEventName(rs.getString("event_name"));
				instance.setListeners(rs.getString("listeners"));
				instance.setCreateTime(rs.getLong("create_time"));
				instance.setStartTime(rs.getLong("start_time"));
				return instance;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.EPSInstanceDao#getAll()
	 */
	public List<EPSInstance> getAll() throws SQLException {
		List<EPSInstance> instances = new ArrayList<EPSInstance>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(selectAllSQL);
			rs = stmt.executeQuery();
			while (rs.next()) {
				EPSInstance instance = new EPSInstance();
				instance.setId(rs.getString("id"));
				instance.setEnable(rs.getString("enable"));
				instance.setEps(rs.getString("eps"));
				instance.setEventName(rs.getString("event_name"));
				instance.setListeners(rs.getString("listeners"));
				instance.setCreateTime(rs.getLong("create_time"));
				instance.setStartTime(rs.getLong("start_time"));
				instances.add(instance);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return instances;
	}
	
	private String getNewId() {
		return UUID.randomUUID().toString();
	}

}
