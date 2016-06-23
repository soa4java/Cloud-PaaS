/**
 * 
 */
package com.primeton.paas.cardbin.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
// @Entity
// @Table(name = "TBL_JOY_ISSUER_INF")
public class IssuerInst implements Serializable {

	private static final long serialVersionUID = 1624328349969211592L;

	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// private Integer id;

	// @Column(name = "ISS_INS_CD", length = 11)
	private String issuerInstCode;

	// @Column(name = "ISS_CN_NAME")
	private String issuerCnName;

	// @Column(name = "ISS_EN_NAME")
	private String issuerEnName;

	// @Column(name = "SUPPORT_MAP")
	private String supportMap;

	// @Column(name = "ISS_LOGO")
	private byte[] issuerLogo;

	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "UPDATE_STAMP")
	private Date updateStamp;

	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "CREATE_STAMP")
	private Date createStamp = new Date();

	public String getIssuerInstCode() {
		return issuerInstCode;
	}

	public void setIssuerInstCode(String issuerInstCode) {
		this.issuerInstCode = issuerInstCode;
	}

	public String getIssuerCnName() {
		if (issuerCnName != null)
			return issuerCnName.trim();
		return issuerCnName;
	}

	public void setIssuerCnName(String issuerCnName) {
		this.issuerCnName = issuerCnName;
	}

	public String getIssuerEnName() {
		if (issuerEnName != null)
			return issuerEnName.trim();
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

	public void setSupportMap(String supportMap) {
		this.supportMap = supportMap;
	}

	public String getSupportMap() {
		return supportMap;
	}

	public String toString() {
		return this.issuerInstCode + "_" + this.issuerCnName;
	}

}
