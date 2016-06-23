/**
 * 
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class NginxService extends AbstractService {

	private static final long serialVersionUID = 3132175310488755013L;
	
	public static final String TYPE = "Nginx";
	
	
	/**
	 * Nginx worker processors, better configure less than CPU core processors. <br>
	 */
	private static final String WORKER_PROCESSES = "worker_processes";
	
	/**
	 * one worker processor maxsize connections. Can support 1/2 request size. <br>
	 */
	private static final String WORKER_CONNECTIONS = "worker_connections";
	
	/**
	 * keepalive_timeout <br>
	 */
	private static final String KEEPALIVE_TIMEOUT = "keepalive_timeout";
	
	/**
	 * types_hash_max_size <br>
	 */
	private static final String TYPES_HASH_MAX_SIZE = "types_hash_max_size";
	
	/**
	 * enable can monitor page. <br>
	 */
	private static final String ALLOW_ACCESS_HOSTS = "allow_access_hosts";
	
	/**
	 * request size limit. <br>
	 */
	private static final String CLIENT_MAX_BODY_SIZE = "client_max_body_size";
	
	/**
	 * SSL. <br>
	 */
	private static final String SSL_CERTIFICATE_PATH = "sslCertificatePath";
	
	/**
	 * 
	 */
	public NginxService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getWorkerProcesses() {
		return getValue(WORKER_PROCESSES, 1);
	}
	
	/**
	 * 
	 * @param workerProcesses
	 */
	public void setWorkerProcesses(int workerProcesses) {
		if (workerProcesses > 0) {
			setValue(WORKER_PROCESSES, workerProcesses);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getWorkerConnections() {
		return getValue(WORKER_CONNECTIONS, 1024);
	}
	
	/**
	 * 
	 * @param workerConnections
	 */
	public void setWorkerConnections(int workerConnections) {
		if (workerConnections > 0) {
			setValue(WORKER_CONNECTIONS, workerConnections);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getKeepaliveTimeout() {
		return getValue(KEEPALIVE_TIMEOUT, 65);
	}
	
	/**
	 * 
	 * @param keepaliveTimeout
	 */
	public void setKeepaliveTimeout(int keepaliveTimeout) {
		if (keepaliveTimeout > 0) {
			setValue(KEEPALIVE_TIMEOUT, keepaliveTimeout);
		}
	}
	
	/**
	 * types_hash_max_size <br>
	 * 
	 * @return types_hash_max_size
	 */
	public int getTypesHashMaxSize() {
		return getValue(TYPES_HASH_MAX_SIZE, 2048);
	}
	
	/**
	 * types_hash_max_size <br>
	 * 
	 * @param typesHashMaxSize types_hash_max_size
	 */
	public void setTypesHashMaxSize(int typesHashMaxSize) {
		setValue(TYPES_HASH_MAX_SIZE, typesHashMaxSize);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAllowAccessHosts() {
		return getValue(ALLOW_ACCESS_HOSTS, "127.0.0.1"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param allowAccessHosts
	 */
	public void setAllowAccessHosts(String allowAccessHosts) {
		setValue(ALLOW_ACCESS_HOSTS, allowAccessHosts);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getClientMaxBodySize() {
		return getValue(CLIENT_MAX_BODY_SIZE, 100);
	}
	
	/**
	 * 
	 * @param clientMaxBodySize
	 */
	public void setClientMaxBodySize(int clientMaxBodySize) {
		setValue(CLIENT_MAX_BODY_SIZE, clientMaxBodySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSslCertificatePath() {
		return getValue(SSL_CERTIFICATE_PATH);
	}
	
	/**
	 * 
	 * @param sslCertificatePath
	 */
	public void setSslCertificatePath(String sslCertificatePath) {
		setValue(SSL_CERTIFICATE_PATH, sslCertificatePath);
	}
	
}
