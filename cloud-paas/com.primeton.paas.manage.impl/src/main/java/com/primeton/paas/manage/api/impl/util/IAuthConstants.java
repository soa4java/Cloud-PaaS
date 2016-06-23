/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

/**
 * 授权管理常量接口
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public interface IAuthConstants {

	/**
	 * 将功能适配resourceType为function字符串 Comment for
	 * <code>FUNCTION_TO_RESOURCE_TYPE</code>
	 */
	String FUNCTION_TO_RESOURCE_TYPE = "function";

	/**
	 * 0代表无权限，1代表有权限 Comment for <code>FUNCTION_TO_STATES</code>
	 */
	String[] FUNCTION_TO_STATES = new String[] {
			"0", "1"
	};
	//功能路径
	String FUNCTION_ACTION = "function_action";
	//资源ID
	String FUNCTION_PARAN_RESOURCE_ID = "__resourceId";
	//资源类型
	String FUNCTION_PARAM_REAOURCE_TYPE = "__resourceType";

	// 所有实体中的租户属性
	static final String TENANT_PROPERTY = "tenantId";

	// 是叶子节点
	static final String ORG_ISLEAF_YES = "0";

	// 不是叶子节点
	static final String ORG_ISLEAF_NOT = "1";

	// 用户PartyType标识
	static final String USER_PARTY_TYPE_ID = "user";

	// 机构PartyType标识
	static final String ORG_PARTY_TYPE_ID = "org";

	// role组织机构类型
	static final String ROLE_PARTY_TYPE_ID = "role";

	// 人员类型
	static final String EMP_PARTY_TYPE_ID = "emp";

	static final String ORG_ROLE_PARTY_TYPE_ID = "orgRole";

	// 用户邮箱标识
	static final String EMAIL = "email";

	// 默认租户
	static final String DEFAULT_TENANT_ID = "default";
	static final String DEFAULT_ROLE_TYPE = "role";
	static final String DEFAULT_PARTY_TYPE = "role";
	static final String DEFAULT_RES_TYPE = "function";
	static final String DEFAULT_RES_STATE = "1";
	static final String DEFAULT_PARTY_SCOPE = "0";

}