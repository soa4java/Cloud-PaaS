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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.primeton.paas.cep.api.ConnectionFactory;
import com.primeton.paas.cep.spi.TableId;
import com.primeton.paas.cep.spi.TableIdDao;
import com.primeton.paas.cep.util.DBUtil;
import com.primeton.paas.cep.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class TableIdDaoImpl implements TableIdDao {
	
	private static final String TABLE_NAME = "cep_id_table";
	private String insertSQL = "INSERT INTO " + TABLE_NAME + "(table_name, begin_id, increment, next_id) VALUES (?, ?, ?, ?)";
	private String updateSQL = "UPDATE " + TABLE_NAME + " SET begin_id = ?, increment = ?, next_id = ? WHERE table_name = ?";
	private String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE table_name = ?";
	private String selectSQL = "SELECT table_name, begin_id, increment, next_id FROM " + TABLE_NAME + " WHERE table_name = ?";
	private String selectAllSQL = "SELECT table_name, begin_id, increment, next_id FROM " + TABLE_NAME;
	
	private Map<String, TableId> buffer = new ConcurrentHashMap<String, TableId>();

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.TableIdDao#add(com.primeton.paas.cep.spi.TableId)
	 */
	public void add(TableId tableId) throws SQLException {
		if (tableId == null || StringUtil.isEmpty(tableId.getTableName())) {
			return;
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(insertSQL);
			stmt.setString(1, tableId.getTableName());
			stmt.setLong(2, tableId.getBeginId());
			stmt.setInt(3, tableId.getIncrement());
			stmt.setLong(4, tableId.getNextId());
			stmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(conn, stmt, null);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.TableIdDao#update(com.primeton.paas.cep.spi.TableId)
	 */
	public void update(TableId tableId) throws SQLException {
		if (tableId == null || StringUtil.isEmpty(tableId.getTableName())) {
			return;
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(updateSQL);
			stmt.setLong(1, tableId.getBeginId());
			stmt.setInt(2, tableId.getIncrement());
			stmt.setLong(3, tableId.getNextId());
			stmt.setString(4, tableId.getTableName());
			stmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(conn, stmt, null);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.TableIdDao#delete(java.lang.String)
	 */
	public void delete(String tableName) throws SQLException {
		if (StringUtil.isEmpty(tableName)) {
			return;
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(deleteSQL);
			stmt.setString(1, tableName);
			stmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(conn, stmt, null);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.TableIdDao#get(java.lang.String)
	 */
	public TableId get(String tableName) throws SQLException {
		if (StringUtil.isEmpty(tableName)) {
			return null;
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(selectSQL);
			stmt.setString(1, tableName);
			rs = stmt.executeQuery();
			if (rs.next()) {
				TableId tableId = new TableId();
				tableId.setTableName(tableName);
				tableId.setBeginId(rs.getLong("begin_id"));
				tableId.setIncrement(rs.getInt("increment"));
				tableId.setNextId(rs.getLong("next_id"));
				return tableId;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.TableIdDao#getAll()
	 */
	public List<TableId> getAll() throws SQLException {
		List<TableId> list = new ArrayList<TableId>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(selectAllSQL);
			rs = stmt.executeQuery();
			while (rs.next()) {
				TableId tableId = new TableId();
				tableId.setTableName(rs.getString("table_name"));
				tableId.setBeginId(rs.getLong("begin_id"));
				tableId.setIncrement(rs.getInt("increment"));
				tableId.setNextId(rs.getLong("next_id"));
				list.add(tableId);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.TableIdDao#getNewId()
	 */
	public synchronized long getNewId(String tableName) throws SQLException {
		if (StringUtil.isEmpty(tableName)) {
			return -1;
		}
		TableId tableId = buffer.get(tableName);
		if (tableId == null) {
			tableId = get(tableName);
			if (tableId == null) { // Database not init
				tableId = new TableId(tableName, 10000L, 10, 10000L);
				add(tableId); // Init Database
			}
			buffer.put(tableName, tableId);
			
			// DB
			TableId _tableId = get(tableName);
			_tableId.setBeginId(_tableId.getBeginId() + _tableId.getIncrement());
			_tableId.setNextId(_tableId.getBeginId());
			update(_tableId);
		}
		long id = tableId.getNextId();
		if (id >= tableId.getBeginId() + tableId.getIncrement()) {
			tableId = get(tableName);
			buffer.put(tableName, tableId);
			// DB
			TableId _tableId = get(tableName);
			_tableId.setBeginId(_tableId.getBeginId() + _tableId.getIncrement());
			_tableId.setNextId(_tableId.getBeginId());
			update(_tableId);
		}
		tableId.setNextId(tableId.getNextId() + 1); // MEM
		return id;
	}

}
