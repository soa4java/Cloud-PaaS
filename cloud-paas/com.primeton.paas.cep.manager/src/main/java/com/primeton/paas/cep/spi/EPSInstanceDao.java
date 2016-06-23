/**
 * 
 */
package com.primeton.paas.cep.spi;

import java.sql.SQLException;
import java.util.List;

import com.primeton.paas.cep.model.EPSInstance;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface EPSInstanceDao {
	
	/**
	 * 
	 * @param instance
	 * @throws SQLException
	 */
	void add(EPSInstance instance) throws SQLException;
	
	/**
	 * 
	 * @param id
	 * @throws SQLException
	 */
	void delete(String id) throws SQLException;
	
	/**
	 * 
	 * @param instance
	 * @throws SQLException
	 */
	void update(EPSInstance instance) throws SQLException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	EPSInstance get(String id) throws SQLException;
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	List<EPSInstance> getAll() throws SQLException;

}
