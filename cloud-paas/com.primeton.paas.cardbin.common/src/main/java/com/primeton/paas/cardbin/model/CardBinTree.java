/**
 * 
 */
package com.primeton.paas.cardbin.model;

import java.util.List;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CardBinTree {

	CardBinTrieNode rootNode = new CardBinTrieNode();

	public CardBinTree(List<CardBinInfo> cardBins) {

		for (CardBinInfo cardBin : cardBins) {
			rootNode.addSubNode(cardBin.getCardBin(), cardBin);
		}
	}

	public CardBinInfo traverseTree(String pan) {

		return rootNode.returnCardBin(pan);
	}

	public CardBinTrieNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(CardBinTrieNode rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * 
	 * @param cardBin
	 */
	public void addNode(CardBinInfo cardBin) {
		rootNode.addSubNode(cardBin.getCardBin(), cardBin);
	}

	/**
	 * 
	 * @param binCode
	 */
	public void removeNode(String binCode) {
		CardBinTrieNode node = rootNode.findNode(binCode);
		if (node != null && node.getCardBin() != null) {
			node.setCardBin(null);
		}
	}
	
}
