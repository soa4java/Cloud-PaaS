/**
 * 
 */
package com.primeton.paas.cardbin.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.cardbin.model.IssuerInst;
import com.primeton.paas.cardbin.server.jdbc.ConnectionFactory;
import com.primeton.paas.cardbin.spi.CardBinRuntimeException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class IssuerInstDao {
	
	private final String SQL_SELECT_ALL_ISSINS = "select CARD_BIN, CARD_CN_NM, CARD_EN_NM, ISS_INS_ID_CD, ISS_INS_CN_NM, ISS_INS_EN_NM" +
			", CARD_BIN_LEN, CARD_ATTR, CARD_CATA, CARD_CLASS, CARD_BRAND, CARD_PROD, CARD_LVL, CARD_MEDIA" +
			", IC_APP_TP, PAN_LEN, PAN_SAMPLE, PAY_CURR_CD1, PAY_CURR_CD2, PAY_CURR_CD3, CARD_BIN_PRIV_BMP" +
			", PUBLISH_DT, CARD_VFY_ALGO, FRN_TRANS_IN, OPER_IN, EVENT_ID, REC_ID, REC_UPD_USR_ID, REC_UPD_TS" +
			", REC_CRT_TS, SYNC_ST, SYNC_BAT_NO, SUP_TS, PROC_ST" +
			" from PAS_CARDBIN";
	
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private IssuerInst convertIssuerInst(ResultSet rs) throws SQLException {
		IssuerInst issuerInst = new IssuerInst();

		String supportMap = "0000000000000000"; 

		issuerInst.setIssuerInstCode(rs.getString("ISS_INS_ID_CD")); 
		issuerInst.setIssuerCnName(rs.getString("ISS_INS_CN_NM"));				 
		issuerInst.setIssuerEnName(rs.getString("ISS_INS_EN_NM"));					 
		issuerInst.setSupportMap(supportMap);
		//	issuerInst.setIssuerLogo(rs.getBytes("ISS_LOGO"));									 
		issuerInst.setUpdateStamp(rs.getTimestamp("REC_UPD_TS"));					 
		issuerInst.setCreateStamp(rs.getTimestamp("REC_CRT_TS"));						 
	    
		return issuerInst;
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	private List<IssuerInst> queryIssuerInst(String sql, Object[] params) {
		List<IssuerInst> list = new ArrayList<IssuerInst>();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					stmt.setObject(i+1, params[i]);
				}
			}
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				IssuerInst issuerInst = convertIssuerInst(rs);
				list.add(issuerInst);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CardBinRuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				throw new CardBinRuntimeException(e);
			}
		}
		
		return list;
	}

	/**
	 * 
	 * @param instCode
	 * @return
	 */
	public IssuerInst getIssuerByCode(String instCode) {
		IssuerInst inst = null;
		String SQL_SELECT_ISSINS_BY_CODE = SQL_SELECT_ALL_ISSINS + " where ISS_INS_ID_CD = ?";
		List<IssuerInst> list = queryIssuerInst(SQL_SELECT_ISSINS_BY_CODE, new Object[] { instCode });
		if (list != null && list.size() > 0) {
			inst = list.get(0);
		}
		if (inst == null && instCode.length() > 4) {
			String SQL_SELECT_ISSINS_BY_PARENT_CODE = SQL_SELECT_ALL_ISSINS + " where ISS_INS_ID_CD like ?";
			list = queryIssuerInst(SQL_SELECT_ISSINS_BY_PARENT_CODE, new Object[] { instCode.substring(0, 6) + "%" });
			if (list != null && list.size() > 0) {
				inst = list.get(0);
			}
		}
		return inst;
	}

	/**
	 * 
	 * @param issuerCnName
	 * @return
	 */
	public List<IssuerInst> getIssuerByName(String issuerCnName) {
		String SQL_SELECT_ISSINS_BY_NAME = SQL_SELECT_ALL_ISSINS + " where ISS_INS_CN_NM like ?";
		return queryIssuerInst(SQL_SELECT_ISSINS_BY_NAME, new Object[] { "%" + issuerCnName + "%" });
	}
	
}
