/**
 * 
 */
package com.primeton.paas.cep.spi;

import java.sql.SQLException;
import java.util.List;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-11-12
 *
 */
public interface TableIdDao {
	
	/**
	 * 
	 * @param tableId
	 * @throws SQLException
	 */
	void add(TableId tableId) throws SQLException;
	
	/**
	 * 
	 * @param tableId
	 * @throws SQLException
	 */
	void update(TableId tableId) throws SQLException;
	
	/**
	 * 
	 * @param tableName
	 * @throws SQLException
	 */
	void delete(String tableName) throws SQLException;
	
	/**
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	TableId get(String tableName) throws SQLException;
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	List<TableId> getAll() throws SQLException;
	
	/**
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	long getNewId(String tableName) throws SQLException;

}
