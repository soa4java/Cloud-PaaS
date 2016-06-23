/**
 * 
 */
package com.primeton.paas.cardbin.model;

import java.io.Serializable;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RuleVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ruleId;
	
	private String issuerInstCode;

	private String supportMap;
	
	private String cardNature;
	
	private String cardClass;
	
	private String cardBrand;
	
	private String cardProduct;
	
	private String cardLevel;
	
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
	
	public String getIssuerInstCode() {
		return issuerInstCode;
	}

	public void setIssuerInstCode(String issuerInstCode) {
		this.issuerInstCode = issuerInstCode;
	}

	public String getSupportMap() {
		return supportMap;
	}

	public void setSupportMap(String supportMap) {
		this.supportMap = supportMap;
	}

	public String getCardNature() {
		return cardNature;
	}

	public void setCardNature(String cardNature) {
		this.cardNature = cardNature;
	}

	public String getCardClass() {
		return cardClass;
	}

	public void setCardClass(String cardClass) {
		this.cardClass = cardClass;
	}

	public String getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}

	public String getCardProduct() {
		return cardProduct;
	}

	public void setCardProduct(String cardProduct) {
		this.cardProduct = cardProduct;
	}

	public String getCardLevel() {
		return cardLevel;
	}

	public void setCardLevel(String cardLevel) {
		this.cardLevel = cardLevel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String toString() {
		return null;
	}

}
