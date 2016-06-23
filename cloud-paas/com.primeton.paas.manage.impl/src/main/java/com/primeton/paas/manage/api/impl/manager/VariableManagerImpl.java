/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultVariableDao;
import com.primeton.paas.manage.api.manager.IVariableManager;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.Variable;
import com.primeton.paas.manage.api.util.DateFormatUtil;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 *
 * @author liyanping(mailto:liyp@primeton.com)
 */
public class VariableManagerImpl implements IVariableManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(VariableManagerImpl.class);
	
	private static DefaultVariableDao varDao = DefaultVariableDao.getInstance();
	
	private Map<String, Variable> varCache = new HashMap<String, Variable>();
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#save(com.primeton.paas.manage.api.model.Variable)
	 */
	public void save(Variable var) {
		if (null == var || StringUtil.isEmpty(var.getVarKey())) {
			return;
		}
		try {
			Variable existsVar = varDao.get(var.getVarKey());
			if (existsVar == null) {
				varDao.addVar(var);
			} else {
				varDao.updateVar(var);
			}
			// update
			varCache.put(var.getVarKey(), var);
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#remove(java.lang.String)
	 */
	public void remove(String key) {
		if (StringUtil.isEmpty(key)) {
			return;
		}
		try{
			varDao.delVar(key);
			//update
			varCache.remove(key);
		} catch (Throwable t) {
			logger.error(t);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#clear()
	 */
	public void clear() {
		try{
			varDao.delAll();
			varCache.clear();
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#get(java.lang.String)
	 */
	public Variable get(String key) {
		if (StringUtil.isEmpty(key)) {
			return null;
		}
		Variable var = varCache.get(key);
		if (null != var) {
			return var;
		}
		try{
			var = varDao.get(key);
			// Buffer
			if (null != var) {
				varCache.put(key, var);
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return var;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#getStringValue(java.lang.String, java.lang.String)
	 */
	public String getStringValue(String key, String defaultValue) {
		if (StringUtil.isEmpty(key)) {
			return defaultValue;
		}
		Variable var = get(key);
		if (var == null || null == var.getVarValue()) {
			return defaultValue;
		}
		return var.getVarValue();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#getLongValue(java.lang.String, long)
	 */
	public long getLongValue(String key, long defaultValue) {
		Variable var = get(key);
		if (var == null || var.getVarValue() == null) {
			return defaultValue;
		}
		try {
			long retValue = Long.parseLong(var.getVarValue());
			return retValue;
		} catch (Exception e) {
			logger.error(e);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#getIntValue(java.lang.String, int)
	 */
	public int getIntValue(String key, int defaultValue) {
		Variable var = get(key);
		if(var == null || var.getVarValue()== null){
			return defaultValue;
		}
		try {
			int retValue = Integer.parseInt(var.getVarValue());
			return retValue;
		} catch (Exception e) {
			logger.error(e);
		}
		return defaultValue;
	}
	
	public boolean getBoolValue(String key, boolean defaultValue) {
		Variable var = get(key);
		if(var == null || var.getVarValue()== null){
			return defaultValue;
		}
		try {
			boolean retValue = Boolean.parseBoolean(var.getVarValue());
			return retValue;
		} catch (Exception e) {
			logger.error(e);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#getByteValue(java.lang.String, byte)
	 */
	public byte getByteValue(String key, byte defaultValue) {
		Variable var = get(key);
		if(var == null || var.getVarValue()== null){
			return defaultValue;
		}
		try {
			byte retValue = Byte.parseByte(var.getVarValue());
			return retValue;
		} catch (Exception e) {
			logger.error(e);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#getDateValue(java.lang.String, java.util.Date)
	 */
	public Date getDateValue(String key, Date defaultValue) {
		Variable var = get(key);
		if(var == null || var.getVarValue()== null){
			return defaultValue;
		}
		try {
			Date retValue = DateFormatUtil.toDate(var.getVarValue(), "yyyy-MM-dd HH:mm:ss");
			return retValue;
		} catch (Exception e) {
			logger.error(e);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#getDateValue(java.lang.String, java.lang.String, java.util.Date)
	 */
	public Date getDateValue(String key, String reg, Date defaultValue) {
		Variable var = get(key);
		if(var == null || var.getVarValue()== null){
			return defaultValue;
		}
		SimpleDateFormat sFormat = new SimpleDateFormat(reg);
		try {
			Date retValue = sFormat.parse(var.getVarValue());
			return retValue;
		} catch (Exception e) {
			logger.error(e);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IVariableManager#getAll()
	 */
	public Variable[] getAll() {
		List<Variable> varList = new ArrayList<Variable>();
		try{
			varList = varDao.getAll();
		} catch (Throwable t) {
			logger.error(t);
		}
		if (varList != null && !varList.isEmpty()) {
			return varList.toArray(new Variable[varList.size()]);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IVariableManager#get(com.primeton.paas.manage.api.model.Variable, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public Variable[] get(Variable criteria, IPageCond page) {
		List<Variable> list = new ArrayList<Variable>();
		try {
			if (page == null) {
				list = varDao.getVars(criteria);
			} else {
				if (page.getCount() <= 0) {
					page.setCount(varDao.getVarCount(criteria));
				}
				list = varDao.getVars(criteria, page);
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		if (list != null && list.size() > 0) {
			return list.toArray(new Variable[list.size()]);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IVariableManager#refresh()
	 */
	public void refresh() {
		varCache.clear();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IVariableManager#refresh(java.lang.String)
	 */
	public void refresh(String key) {
		varCache.remove(key);
	}
	
}
