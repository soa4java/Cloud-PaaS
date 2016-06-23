/**
 * 
 */
package com.primeton.paas.cardbin.model;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CardBinTrieNode {

	private CardBinInfo cardBin;

	private CardBinTrieNode[] child = new CardBinTrieNode[10];

	public CardBinInfo getCardBin() {
		return cardBin;
	}

	public void setCardBin(CardBinInfo cardBin) {
		this.cardBin = cardBin;
	}

	public CardBinTrieNode[] getChild() {
		return child;
	}

	public void setChild(CardBinTrieNode[] child) {
		this.child = child;
	}

	public void addSubNode(String subCardBin, CardBinInfo cardBin) {
		if (subCardBin.length() == 0) {
			this.cardBin = cardBin;
		} else {
			char key = subCardBin.charAt(0);
			int num = key - '0';
			CardBinTrieNode subTreeNode;
			if (child[num] != null) {
				subTreeNode = child[num];
			} else {
				subTreeNode = new CardBinTrieNode();
				child[num] = subTreeNode;
			}
			subTreeNode.addSubNode(subCardBin.substring(1), cardBin);
		}
	}

	public CardBinInfo returnCardBin(String subPan) {
		// System.out.println("PAN===>" + subPan);
		if (subPan.length() == 0)
			return cardBin;

		char key = subPan.charAt(0);

		CardBinTrieNode subTreeNode;
		int num = key - '0';
		if (child[num] != null) {
			subTreeNode = child[num];
			CardBinInfo bin = subTreeNode.returnCardBin(subPan.substring(1));
			if (bin != null)
				return bin;
		}
		return cardBin;
	}

	public CardBinTrieNode findNode(String subPan) {
		if (subPan.length() == 0)
			return this;

		char key = subPan.charAt(0);

		CardBinTrieNode subTreeNode;
		int num = key - '0';
		if (child[num] != null) {
			subTreeNode = child[num];
			CardBinTrieNode node = subTreeNode.findNode(subPan.substring(1));
			if (node.getCardBin() != null)
				return node;
		}
		return this;
	}

}
