/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public abstract class AbstractClusterManager implements IClusterManager {
	
	private List<String> types = new ArrayList<String>();

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#getTypes()
	 */
	public String[] getTypes() {
		if (null == types || types.isEmpty()) {
			ServiceLoader<ICluster> loader = ServiceLoader.load(ICluster.class);
			if (loader != null) {
				for (ICluster obj : loader) {
					if (obj != null && !StringUtil.isEmpty(obj.getType())) {
						types.add(obj.getType());
					}
				}
			}
		}
		return types.toArray(new String[types.size()]);
	}

}
