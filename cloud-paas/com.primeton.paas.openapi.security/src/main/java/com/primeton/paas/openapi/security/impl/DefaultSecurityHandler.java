/**
 * 
 */
package com.primeton.paas.openapi.security.impl;

import com.primeton.paas.openapi.security.ISecurityHandler;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultSecurityHandler implements ISecurityHandler {
	
	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.security.ISecurityHandler#getSignature(byte[], byte[])
	 */
	public byte[] getSignature(byte[] data) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.security.ISecurityHandler#verifySignature(byte[], byte[], byte[])
	 */
	public boolean verifySignature(byte[] signature, byte[] data) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.security.ISecurityHandler#encrypt(byte[], byte[])
	 */
	public byte[] encrypt(byte[] plainData) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.security.ISecurityHandler#decrypt(byte[], byte[])
	 */
	public byte[] decrypt(byte[] cryptedData) {
		return null;
	}
	
}
