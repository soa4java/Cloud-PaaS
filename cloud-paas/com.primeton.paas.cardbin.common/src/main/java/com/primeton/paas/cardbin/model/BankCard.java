/**
 * 
 */
package com.primeton.paas.cardbin.model;

import java.io.Serializable;

public class BankCard implements Serializable {
    
	private static final long serialVersionUID = -8604397755014197956L;

    private String pan; 
    
    private String cardBin;
    
    private String cardCNName;
    
    private String cardENName;
    
    private String cardNature;
    
    private String cardNatureDes;
    
    private String cardClass;   
    
    private String cardClassDes;
    
    private String cardBrand; 
    
    private String cardBrandDes;

    private String cardProduct;
    
    private String cardProductDes;

    private String cardLevel;
    
    private String cardLevelDes;

    private String issuerInstCode;
    
    private String headInstCode;
    
    private String issuerCnName;
    
    private String issuerEnName;
    
    private byte[] issuerLogo;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCardBin() {
        return cardBin;
    }

    public void setCardBin(String cardBin) {
        this.cardBin = cardBin;
    }

    public String getCardCNName() {
        return cardCNName;
    }

    public void setCardCNName(String cardCNName) {
        this.cardCNName = cardCNName;
    }

    public String getCardENName() {
        return cardENName;
    }

    public void setCardENName(String cardENName) {
        this.cardENName = cardENName;
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

    public String getIssuerInstCode() {
        return issuerInstCode;
    }

    public void setIssuerInstCode(String issuerInstCode) {
        this.issuerInstCode = issuerInstCode;
    }

    public String getIssuerCnName() {
        return issuerCnName;
    }

    public void setIssuerCnName(String issuerCnName) {
        this.issuerCnName = issuerCnName;
    }

    public String getIssuerEnName() {
        return issuerEnName;
    }

    public void setIssuerEnName(String issuerEnName) {
        this.issuerEnName = issuerEnName;
    }

    public byte[] getIssuerLogo() {
        return issuerLogo;
    }

    public void setIssuerLogo(byte[] issuerLogo) {
        this.issuerLogo = issuerLogo;
    }

    public String getCardNatureDes() {
        return cardNatureDes;
    }

    public void setCardNatureDes(String cardNatureDes) {
        this.cardNatureDes = cardNatureDes;
    }

    public String getCardClassDes() {
        return cardClassDes;
    }

    public void setCardClassDes(String cardClassDes) {
        this.cardClassDes = cardClassDes;
    }

    public String getCardBrandDes() {
        return cardBrandDes;
    }

    public void setCardBrandDes(String cardBrandDes) {
        this.cardBrandDes = cardBrandDes;
    }

    public String getCardProductDes() {
        return cardProductDes;
    }

    public void setCardProductDes(String cardProductDes) {
        this.cardProductDes = cardProductDes;
    }

    public String getCardLevelDes() {
        return cardLevelDes;
    }

    public void setCardLevelDes(String cardLevelDes) {
        this.cardLevelDes = cardLevelDes;
    }

    public String getHeadInstCode() {
        return headInstCode;
    }

    public void setHeadInstCode(String headInstCode) {
        this.headInstCode = headInstCode;
    }
    
    public String toString() {
    	return this.pan + "_" + this.cardBin + "_" + this.cardCNName + "_" + this.cardClass + "_" + this.cardBrand + "_" + this.cardNature + "_" + this.cardLevel + "_" + this.cardProduct + "_" + this.issuerInstCode + "_" + this.issuerCnName;
    }
    
}
