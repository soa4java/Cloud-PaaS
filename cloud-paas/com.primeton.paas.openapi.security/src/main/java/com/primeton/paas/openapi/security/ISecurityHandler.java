/**
 * 
 */
package com.primeton.paas.openapi.security;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface ISecurityHandler {

	//	public byte[] digest(byte[] data);

	/**
	 * 
	 * @param data
	 * @param password
	 * @return
	 */
	public byte[] getSignature(byte[] data);

	/**
	 * 
	 * @param signature
	 * @param data
	 * @param password
	 * @return
	 */
	public boolean verifySignature(byte[] signature, byte[] data);

	/**
	 * 
	 * @param plainData
	 * @param password
	 * @return
	 */
	public byte[] encrypt(byte[] plainData);

	/**
	 * 
	 * @param cryptedData
	 * @param password
	 * @return
	 */
	public byte[] decrypt(byte[] cryptedData);
	
}
