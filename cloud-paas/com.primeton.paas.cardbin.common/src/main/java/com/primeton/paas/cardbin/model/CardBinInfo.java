/**
 * 
 */
package com.primeton.paas.cardbin.model;

import java.io.Serializable;
import java.util.Date;

import com.primeton.paas.cardbin.spi.CardBinConstants;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CardBinInfo implements Serializable {

	private static final long serialVersionUID = -8836283562201796654L;
	
	public static String unsyncStat = "00";
	
	public static String syncStat = "01";
	
    private String cardBin;

    private String cardCnName = "";

    private String cardEnName = "";
    
    private String supportMap = "0000000000000000";

    private String issuerInstCode;

    private String cardNature; 
    
    private String cardClass;
    
    private String cardBrand;
    
    private String cardProduct;
    
    private String cardLevel;
    
    private String status = CardBinConstants.BANKCARD_STATS_NORMAL;
    
    private Date updateStamp;

    private Date createStamp = new Date();

    public String getCardNature() {
		return cardNature;
	}

	public void setCardNature(String cardNature) {
		this.cardNature = cardNature;
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

	public String getCardBin() {
        return cardBin.trim();
    }

    public void setCardBin(String cardBin) {
        this.cardBin = cardBin;
    }

    public String getCardCnName() {
        return cardCnName.trim();
    }

    public void setCardCnName(String cardCnName) {
        this.cardCnName = cardCnName;
    }

    public String getCardEnName() {
        return cardEnName.trim();
    }

    public void setCardEnName(String cardEnName) {
        this.cardEnName = cardEnName;
    }

    public String getSupportMap() {
        return supportMap;
    }

    public void setSupportMap(String supportMap) {
        this.supportMap = supportMap;
    }

    public String getIssuerInstCode() {
        return issuerInstCode;
    }

    public void setIssuerInstCode(String issuerInstCode) {
        this.issuerInstCode = issuerInstCode;
    }

    public Date getUpdateStamp() {
        return updateStamp;
    }

    public void setUpdateStamp(Date updateStamp) {
        this.updateStamp = updateStamp;
    }

    public Date getCreateStamp() {
        return createStamp;
    }

    public void setCreateStamp(Date createStamp) {
        this.createStamp = createStamp;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toString() {
		return this.cardBin + "_" + this.cardCnName + "_" + this.cardBrand + "_" + this.cardClass + "_" + this.cardLevel + "_" + this.cardNature + "_" + this.issuerInstCode;
	}

}
