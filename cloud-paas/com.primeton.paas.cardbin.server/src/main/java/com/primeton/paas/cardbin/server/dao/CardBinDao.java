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

import com.primeton.paas.cardbin.model.CardBinInfo;
import com.primeton.paas.cardbin.server.jdbc.ConnectionFactory;
import com.primeton.paas.cardbin.server.util.RuleUtil;
import com.primeton.paas.cardbin.spi.CardBinConstants;
import com.primeton.paas.cardbin.spi.CardBinRuntimeException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CardBinDao {
	
	private final String SQL_SELECT_ALL_CARDBIN = "select CARD_BIN, CARD_CN_NM, CARD_EN_NM, ISS_INS_ID_CD, ISS_INS_CN_NM, ISS_INS_EN_NM" +
			", CARD_BIN_LEN, CARD_ATTR, CARD_CATA, CARD_CLASS, CARD_BRAND, CARD_PROD, CARD_LVL, CARD_MEDIA" +
			", IC_APP_TP, PAN_LEN, PAN_SAMPLE, PAY_CURR_CD1, PAY_CURR_CD2, PAY_CURR_CD3, CARD_BIN_PRIV_BMP" +
			", PUBLISH_DT, CARD_VFY_ALGO, FRN_TRANS_IN, OPER_IN, EVENT_ID, REC_ID, REC_UPD_USR_ID, REC_UPD_TS" +
			", REC_CRT_TS, SYNC_ST, SYNC_BAT_NO, SUP_TS, PROC_ST" +
			" from PAS_CARDBIN";
	
	private CardBinInfo convertCardBinInfo(ResultSet rs) throws SQLException {
		CardBinInfo cardBinInfo = new CardBinInfo();

		String supportMap = "0000000000000000"; 
		String status = CardBinConstants.BANKCARD_STATS_NORMAL;

		cardBinInfo.setCardBin(rs.getString("CARD_BIN"));						 
		cardBinInfo.setCardCnName(rs.getString("CARD_CN_NM"));			 
		cardBinInfo.setCardEnName(rs.getString("CARD_EN_NM"));			 
		cardBinInfo.setCardLevel(rs.getString("CARD_LVL"));						 
		cardBinInfo.setCardBrand(rs.getString("CARD_BRAND"));			 
		cardBinInfo.setCardClass(rs.getString("CARD_CLASS"));				 
		cardBinInfo.setCardNature(rs.getString("CARD_ATTR"));					 
		cardBinInfo.setCardProduct(rs.getString("CARD_PROD"));				 
		cardBinInfo.setCreateStamp(rs.getTimestamp("REC_CRT_TS"));		 
		cardBinInfo.setIssuerInstCode(rs.getString("ISS_INS_ID_CD"));		 
		cardBinInfo.setStatus(status);
		cardBinInfo.setSupportMap(supportMap);
		cardBinInfo.setUpdateStamp(rs.getTimestamp("REC_UPD_TS"));		 
		
		return cardBinInfo;
	}
	
	private List<CardBinInfo> queryCardBinInfo(String sql, Object[] params) {
		List<CardBinInfo> list = new ArrayList<CardBinInfo>();

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
				CardBinInfo cardBinInfo = convertCardBinInfo(rs);
				list.add(cardBinInfo);
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
	 * @return
	 */
	public List<CardBinInfo> getAll() {
		return queryCardBinInfo(SQL_SELECT_ALL_CARDBIN, null);
	}

	/**
	 * 
	 * @return
	 */
	public List<CardBinInfo> getAllNormal() {
		//	String SQL_SELECT_ALL_NORMAL_CARDBIN = SQL_SELECT_ALL_CARDBIN + " where STATUS = ?";
		//	return queryCardBinInfo(SQL_SELECT_ALL_NORMAL_CARDBIN, new Object[] { CardBinConstants.BANKCARD_STATS_NORMAL });
		return getAll();
	}

	/**
	 * 
	 * @param cardBinCode
	 * @return
	 */
	public CardBinInfo getCardBinByCode(String cardBinCode) {
		String SQL_SELECT_CARDBIN_BY_CODE = SQL_SELECT_ALL_CARDBIN + " where CARD_BIN = ?";
		List<CardBinInfo> list = queryCardBinInfo(SQL_SELECT_CARDBIN_BY_CODE, new Object[] { cardBinCode });
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param instCode
	 * @param supportRule
	 * @return
	 */
	public List<CardBinInfo> findByRule(String instCode, String supportRule) {
		List<CardBinInfo> binList = getCardBinByBankCode(instCode);

		if (binList == null)
			return null;

		List<CardBinInfo> ansBinList = new ArrayList<CardBinInfo>();

		for (CardBinInfo bin : binList) {
			if (bin.getStatus().equals(CardBinConstants.BANKCARD_STATS_NORMAL)) {
				long binRule = RuleUtil.getSupportRuleByCardBin(bin);
				long aimRule = RuleUtil.binaryStrToLong(supportRule);
				if (((binRule & aimRule) ^ binRule) == 0) { 
					ansBinList.add(bin);
				}
			}
		}

		return ansBinList;
	}

	/**
	 * @param bankCode
	 * @return
	 */
	public List<CardBinInfo> getCardBinByBankCode(String bankCode) {
		String SQL_SELECT_CARDBIN_BY_INSTCODE = SQL_SELECT_ALL_CARDBIN + " where ISS_INS_ID_CD = ?";
		return queryCardBinInfo(SQL_SELECT_CARDBIN_BY_INSTCODE, new Object[] { bankCode });
	}

	/**
	 * 
	 * @param parentBin
	 * @return
	 */
	public List<CardBinInfo> getCardBinByParentBin(String parentBin) {
		String SQL_SELECT_CARDBIN_BY_PARENT_CODE = SQL_SELECT_ALL_CARDBIN + " where CARD_BIN like ?";
		return queryCardBinInfo(SQL_SELECT_CARDBIN_BY_PARENT_CODE, new Object[] { parentBin + "%" });
	}
	
}
