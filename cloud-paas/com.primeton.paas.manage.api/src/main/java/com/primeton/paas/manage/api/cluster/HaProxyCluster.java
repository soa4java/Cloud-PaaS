/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.HaProxyService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class HaProxyCluster extends AbstractCluster {

	private static final long serialVersionUID = -1190554550779554476L;

	public static final String TYPE = HaProxyService.TYPE;
	
	public static final String PROTOCOL_HTTP = "HTTP";
	public static final String PROTOCOL_HTTPS = "HTTPS";
	public static final String PROTOCOL_HTTP_HTTPS = "HTTP-HTTPS";
	
	public static final String SSL_LEVEL_DEFAULT = "1";
	
	public static final String PROTOCOL_MUTUAL_HTTPS = "MUTUAL-HTTPS";
	public static final String PROTOCOL_MUTUAL_HTTP_HTTPS = "MUTUAL-HTTP-HTTPS";
	
	private static final String DOMAIN = "domain";
	private static final String IS_ENABLE_DOMAIN = "isEnableDomain";  
	private static final String MEMBERS = "members";
	private static final String PROTOCOL_TYPE = "protocolType"; // HTTP | HTTPS | HTTPS+HTTP
	private static final String SSL_CERTIFICATE = "sslCertificate";
	private static final String SSL_CERTIFICATE_KEY = "sslCertificateKey";
	private static final String SSL_CA_CERTIFICATE = "sslCaCertificate";
	private static final String SSL_LEVEL = "sslLevel";
	
	public HaProxyCluster() {
		setType(TYPE);
		setName(TYPE + "-Cluster");
	}
	
	/**
	 * Members <br>
	 * 
	 * @return
	 */
	public String getMembers(){
		return getValue(MEMBERS, "");
	}
	
	/**
	 * Set Members <br>
	 * 
	 * @param members
	 */
	public void setMembers(String members) {
		setValue(MEMBERS, members);
	}
	
	/**
	 * Domain <br>
	 * 
	 * @return Domain
	 */
	public String getDomain() {
		return getValue(DOMAIN);
	}
	
	/**
	 * Set domain <br>
	 * 
	 * @param domain
	 */
	public void setDomain(String domain) {
		setValue(DOMAIN, domain);
	}

	/**
	 * isEnableDomain <br>
	 * 
	 * @return isEnableDomain
	 */
	public String getIsEnableDomain() {
		return getValue(IS_ENABLE_DOMAIN, "Y"); //$NON-NLS-1$
	}
	
	/**
	 * Set isEnableDomain <br>
	 * 
	 * @param isEnableDomain
	 */
	public void setIsEnableDomain(String isEnableDomain) {
		setValue(IS_ENABLE_DOMAIN, isEnableDomain);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProtocolType() {
		return getValue(PROTOCOL_TYPE, PROTOCOL_HTTP);
	}
	
	/**
	 * 
	 * @param protocolType
	 */
	public void setProtocolType(String protocolType) {
		setValue(PROTOCOL_TYPE, protocolType);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSslCertificate() {
		return getValue(SSL_CERTIFICATE, getDomain() + ".server.crt"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param sslCertificate
	 */
	public void setSslCertificate(String sslCertificate) {
		setValue(SSL_CERTIFICATE, sslCertificate);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSslCertificateKey() {
		return getValue(SSL_CERTIFICATE_KEY, getDomain() + ".server.key"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param sslCertificateKey
	 */
	public void setSslCertificateKey(String sslCertificateKey) {
		setValue(SSL_CERTIFICATE_KEY, sslCertificateKey);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getCaSslCertificate() {
		return getValue(SSL_CA_CERTIFICATE, getDomain() + ".ca.crt"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param sslCertificate
	 */
	public void setCaSslCertificate(String sslCertificate) {
		setValue(SSL_CA_CERTIFICATE, sslCertificate);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getSslLevel() {
		return getValue(SSL_LEVEL, SSL_LEVEL_DEFAULT);
	}
	
	/**
	 * 
	 * @param sslLevel
	 */
	public void setSslLevel(String sslLevel) {
		setValue(SSL_LEVEL, sslLevel);
	}
	
}
