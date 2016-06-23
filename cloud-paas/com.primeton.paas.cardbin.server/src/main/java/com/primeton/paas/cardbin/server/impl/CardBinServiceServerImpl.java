/**
 * 
 */
package com.primeton.paas.cardbin.server.impl;

import org.apache.commons.lang3.StringUtils;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.cardbin.api.ICardBinService;
import com.primeton.paas.cardbin.model.BankCard;
import com.primeton.paas.cardbin.model.CardBinInfo;
import com.primeton.paas.cardbin.model.IssuerInst;
import com.primeton.paas.cardbin.server.dao.IssuerInstDao;
import com.primeton.paas.cardbin.server.util.CommonService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CardBinServiceServerImpl implements ICardBinService {

	private static final ILogger logger = LoggerFactory.getLogger(CardBinServiceServerImpl.class);

	private static CommonService commonService;

	private static IssuerInstDao issuerDao;

	private static final int CARDBIN_POS = 15;

	private static final int BASE_POS = 1;

	public CardBinServiceServerImpl() {
		commonService = CommonService.getInstance();
		issuerDao = new IssuerInstDao();
	}

	public BankCard getCardByPan(String pan) {
		return getCardByPan(pan, Integer.MAX_VALUE);
	}

	public BankCard getCardByPan(String pan, int map) {
		CardBinInfo cardBin = commonService.getCardBin(pan);
		if (cardBin == null) {
			logger.info("[HTTP Request]Get the bankcard by pan errro! cann't find the card bin, the pan is: " + pan);
			return null;
		}

		IssuerInst issuer = null;
		if ((map >>> CARDBIN_POS) > 0) {
			String instCode = cardBin.getIssuerInstCode();
			if (StringUtils.isEmpty(instCode)) {
				logger.info("[HTTP Request]Get the bankcard by pan errro! the inst code is null, the pan is: " + pan);
				return null;
			}

			issuer = issuerDao.getIssuerByCode(instCode);
			if (issuer == null) {
				logger.info("[HTTP Request]Get the bankcard by pan errro! cann't find the isuer inst, the pan is: " + pan);
			}
		}

		BankCard card = this.genBankCard(cardBin, issuer, pan, map);

		logger.info("[HTTP Request]Get the bankcard by pan! The pan is: " + pan + ", the cardbin: " + card.getCardBin() + ", the issuerinst: "
				+ (issuer == null ? "" : issuer.getIssuerEnName()));

		return card;
	}

	private BankCard genBankCard(CardBinInfo cardBin, IssuerInst issuer, String pan, int map) {
		BankCard card = new BankCard();

		int i = 1;
		if ((map & BASE_POS) != 0)
			card.setPan(pan);

		if ((map & (BASE_POS << (i++))) != 0)
			card.setCardBin(cardBin.getCardBin());

		if ((map & (BASE_POS << (i++))) != 0)
			card.setCardCNName(cardBin.getCardCnName());

		if ((map & (BASE_POS << (i++))) != 0)
			card.setCardENName(cardBin.getCardEnName());

		String nature = cardBin.getCardNature();
		if ((map & (BASE_POS << (i++))) != 0) {
			card.setCardNature(nature);
		}

		if ((map & (BASE_POS << (i++))) != 0) {
			String natureDes = "";
			if ("01".equals(nature))
				natureDes = "";
			else if ("02".equals(nature))
				natureDes = "";
			else if ("03".equals(nature))
				natureDes = "";
			else if ("04".equals(nature))
				natureDes = "";
			else if ("05".equals(nature))
				natureDes = "";
			else
				natureDes = "";
			card.setCardNatureDes(natureDes);
		}

		String cardclass = cardBin.getCardClass();
		if ((map & (BASE_POS << (i++))) != 0) {
			card.setCardClass(cardclass);
		}

		if ((map & (BASE_POS << (i++))) != 0) {
			String classDes = "";
			if ("01".equals(cardclass))
				classDes = "";
			else if ("02".equals(cardclass))
				classDes = "";
			else if ("03".equals(cardclass))
				classDes = "";
			else if ("04".equals(cardclass))
				classDes = "";
			else if ("05".equals(cardclass))
				classDes = "";
			else if ("06".equals(cardclass))
				classDes = "";
			else if ("07".equals(cardclass))
				classDes = "";
			else if ("99".equals(cardclass))
				classDes = "";
			else
				classDes = "";
			card.setCardClassDes(classDes);
		}

		String brand = cardBin.getCardBrand();
		if ((map & (BASE_POS << (i++))) != 0) {
			card.setCardBrand(brand);
		}

		if ((map & (BASE_POS << (i++))) != 0) {
			String brandDes = "";
			if ("01".equals(brand))
				brandDes = "VISA VIS";
			else if ("02".equals(brand))
				brandDes = "";
			else if ("03".equals(brand))
				brandDes = "";
			else if ("04".equals(brand))
				brandDes = "";
			else if ("05".equals(brand))
				brandDes = "";
			else if ("06".equals(brand))
				brandDes = "";
			else if ("07".equals(brand))
				brandDes = "";
			else if ("11".equals(brand))
				brandDes = "";
			else if ("12".equals(brand))
				brandDes = "";
			else if ("13".equals(brand))
				brandDes = "";
			else if ("99".equals(brand))
				brandDes = "";
			else
				brandDes = "";
			card.setCardBrandDes(brandDes);
		}

		String product = cardBin.getCardProduct();
		if ((map & (BASE_POS << (i++))) != 0) {
			card.setCardProduct(product);
		}

		if ((map & (BASE_POS << (i++))) != 0) {
			String productDes = "";
			if ("00".equals(product))
				productDes = "";
			else if ("01".equals(product))
				productDes = "";
			else if ("02".equals(product))
				productDes = "";
			else if ("03".equals(product))
				productDes = "";
			else if ("04".equals(product))
				productDes = "";
			else if ("05".equals(product))
				productDes = "";
			else if ("06".equals(product))
				productDes = "";
			else if ("07".equals(product))
				productDes = "";
			else if ("08".equals(product))
				productDes = "";
			else if ("09".equals(product))
				productDes = "";
			else if ("10".equals(product))
				productDes = "";
			else if ("11".equals(product))
				productDes = "";
			else
				productDes = "";
			card.setCardProductDes(productDes);
		}

		String level = cardBin.getCardLevel();
		if ((map & (BASE_POS << (i++))) != 0) {
			card.setCardLevel(level);
		}

		if ((map & (BASE_POS << (i++))) != 0) {
			String levelDes = "";
			if ("0".equals(level))
				levelDes = "";
			else if ("1".equals(level))
				levelDes = "";
			else if ("2".equals(level))
				levelDes = "";
			else if ("3".equals(level))
				levelDes = "";
			else if ("4".equals(level))
				levelDes = "";
			else if ("5".equals(level))
				levelDes = "";
			else if ("06".equals(level))
				levelDes = "";
			else
				levelDes = "";
			card.setCardLevelDes(levelDes);
		}

		if ((map & (BASE_POS << (i++))) != 0) {
			card.setIssuerInstCode(cardBin.getIssuerInstCode().trim());
		}

		if (issuer != null) {
			if ((map & (BASE_POS << (i++))) != 0)
				card.setHeadInstCode(issuer.getIssuerInstCode());

			if ((map & (BASE_POS << (i++))) != 0)
				card.setIssuerCnName(issuer.getIssuerCnName());

			if ((map & (BASE_POS << (i++))) != 0) {
				card.setIssuerEnName(issuer.getIssuerEnName());
			}

			if ((map & (BASE_POS << (i++))) != 0) {
				card.setIssuerLogo(issuer.getIssuerLogo());
			}

		}
		return card;
	}

}
