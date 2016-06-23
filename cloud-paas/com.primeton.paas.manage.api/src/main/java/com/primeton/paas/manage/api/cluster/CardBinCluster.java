/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.CardBinService;

/**
 * Marked @Deprecated by ZhongWen.Li
 * 
 * @author Hao
 *
 */
@Deprecated
public class CardBinCluster extends AbstractCluster {

	private static final long serialVersionUID = 7853291699473065511L;

	public static final String TYPE = CardBinService.TYPE;
	
	public CardBinCluster() {
		setType(TYPE);
		setName(TYPE + "-Cluster");
	}
	
}
