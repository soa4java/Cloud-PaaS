/**
 * 
 */
package com.primeton.paas.console.app.service.util;

/**
 * 服务常量定义
 *
 * @author liming(mailto:li-ming@primeton.com)
 */
public interface AppConstants {
	
	/* Common */
	public static final String REPO_HTTP = "http://";// http形式访问Svn资源库
	public static final String SERVICE_ID_SEPARATOR = "_"; // 服务标识间隔符
	public static final String REPO_LOCAL_PATH = "repository"; // 仓库本地路径
	public static final String REPO_REMOTE_PATH = "war"; // 仓库远程路径
	public static final long THE_LASTEST_REVISION = -1L; // 资源库最新版本
	
	public static final String WEB_APP_NAME = "default"; // 部署在Jetty容器webapp下的应用名称
	
	/* VMStateResult 执行状态 */
	public static final String IAAS_VM_STATE_SUCCESS = "SUCCESS"; // 成功
	public static final String IAAS_VM_STATE_FAIL = "FAIL"; // 失败
	public static final String IAAS_VM_STATE_PENDING = "PENDING";	//等待
	
	/* VMResponseStatus 执行状态 */
	public static final String IAAS_RESPONSE_STATE_OK = "OK"; // 成功
	public static final String IAAS_RESPONSE_STATE_ERROR = "ERROR"; // 失败

	/* WarServiceDefinition 部署方式 */
	public static final String DEPLOY_WAY_DEFAULT = "D"; // 默认（无，创建新版本；有，覆盖最新版本）
	public static final String DEPLOY_WAY_NEW_VERSION = "N"; // 创建新版本
	public static final String DEPLOY_WAY_OVERLAP_VERSION = "O"; // 覆盖指定版本
	
	/* War包 提交方式 */
	public static final String DEPLOY_STATUS_CREATE_NEW_VERSION = "N"; // 创建新版本
	public static final String DEPLOY_STATUS_OVERLAP_OLD_VERSION = "O"; // 覆盖指定版本
	public static final String DEPLOY_STATUS_OVERLAP_LASTEST_VERSION = "L"; // 覆盖最新版本
	
	/* War服务提交结果 */
	public static final String SUBMIT_RESULT_SUCCESS = "S"; // 提交成功
	public static final String SUBMIT_RESULT_FAIL = "F"; // 提交失败
	
	/* War包 部署结果 */
	public static final String DEPLOY_RESULT_SUCCESS = "S"; // 部署成功
	public static final String DEPLOY_RESULT_FAIL = "F"; // 部署失败
	
	/* ServiceInstance状态 */
	public static final int INSTANCE_STATE_RUN = 1; // 运行
	public static final int INSTANCE_STATE_STOP = 0; // 停止
	public static final int INSTANCE_STATE_ERROR = -1; // 错误
	public static final int INSTANCE_STATE_DESTROY = -2;// 销毁
	
}