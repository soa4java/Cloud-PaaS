/**
 * 
 */
package com.primeton.paas.cardbin.server.util;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.cardbin.model.CardBinInfo;
import com.primeton.paas.cardbin.model.CardBinTree;
import com.primeton.paas.cardbin.model.IssuerInst;
import com.primeton.paas.cardbin.server.dao.CardBinDao;
import com.primeton.paas.cardbin.server.dao.IssuerInstDao;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CommonService {

	private static final ILogger logger = LoggerFactory.getLogger(CommonService.class);

	private static CommonService SERVICE = null;

	private CardBinDao binDao;

	private IssuerInstDao issuerDao;

	private List<CardBinInfo> bins;

	private AtomicReference<CardBinTree> atomicBinTree;

	/**
	 * Get CommonService Instance
	 * 
	 * @return
	 */
	public static CommonService getInstance() {
		if (SERVICE != null) {
			return SERVICE;
		}
		SERVICE = new CommonService();
		return SERVICE;
	}

	private CommonService() {
		binDao = new CardBinDao();
		issuerDao = new IssuerInstDao();

		bins = binDao.getAllNormal();
		CardBinTree binTree = new CardBinTree(bins);
		atomicBinTree = new AtomicReference<CardBinTree>(binTree);
		
		createCardBinTree();
	}
	
	private void createCardBinTree() {
		logger.info("Create Card Bin Tree");
		List<CardBinInfo> cardBinInfoList = binDao.getAll();
		constructCardBinTree(cardBinInfoList);
		logger.info("Finish Card Bin Tree Creation");
	}
	
	public void constructCardBinTree(List<CardBinInfo> bins) {
		CardBinTree binTree = new CardBinTree(bins);
		atomicBinTree.set(binTree);
	}

	public void reBuildCardBinTree() {
		logger.info("Rebuild the card bin tree!!!");

		bins = binDao.getAllNormal();
		CardBinTree binTree = new CardBinTree(bins);
		atomicBinTree.set(binTree);
	}

	public CardBinInfo getCardBin(String pan) {
		String cardBin = pan;
		CardBinInfo cardBinInfo = atomicBinTree.get().traverseTree(cardBin);
		return cardBinInfo;
	}

	public IssuerInst getIssuerInfo(String pan) {
		CardBinInfo cardBin = getCardBin(pan);
		if (cardBin == null) {
			logger.info("Can't find the card information for this pan! [Pan] : " + pan);
			return null;
		}
		return issuerDao.getIssuerByCode(cardBin.getIssuerInstCode());
	}

	public CardBinInfo getCardBinByBinCode(String binCode) {
		CardBinInfo cardBinInfo = atomicBinTree.get().traverseTree(binCode);
		if (cardBinInfo != null) {
			return cardBinInfo;
		} else {
			logger.info("[CARD BIN TREE]Can't find the card bin! [bin code] : " + binCode);
			return null;
		}
	}

	public IssuerInst getIssuerInfoByBinCode(String binCode) {
		CardBinInfo cardBin = getCardBinByBinCode(binCode);
		if (cardBin != null) {
			IssuerInst inst = issuerDao.getIssuerByCode(cardBin
					.getIssuerInstCode());
			if (inst != null) {
				return inst;
			} else {
				logger.info("[CARD BIN TREE]Can't find the issuer inst information for this card bin! [BinCode] : " + binCode);
			}
		}
		return null;
	}

	public void addCardBinToTree(CardBinInfo cardBin) {
		atomicBinTree.get().addNode(cardBin);
		logger.info("[CARD BIN TREE]Add or Update the card bin on the tree! [BinCode] : " + cardBin.getCardBin());
	}

	public void removeCardBinOnTree(String binCode) {
		atomicBinTree.get().removeNode(binCode);
		logger.info("[CARD BIN TREE]Remove the card bin on the tree! [BinCode] : " + binCode);
	}
	
}
