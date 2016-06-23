/**
 * 
 */
package com.primeton.paas.cardbin.server.util;

import org.apache.commons.lang3.StringUtils;

import com.primeton.paas.cardbin.model.CardBinInfo;
import com.primeton.paas.cardbin.model.RuleVo;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RuleUtil {

	private static final int SUPPMAP_SIZE = 16;
	private static final int RULLMAP_MAX_SIZE = 64;
	private static final int NATURE_SIZE = 5;
	private static final int CLASS_SIZE = 8;
	private static final int BRAND_SIZE = 11;
	private static final int PRODUCT_SIZE = 11;
	private static final int LEVEL_SIZE = 6;

	/**
	 * 
	 * @param brand
	 * @return
	 */
	private static int getBrandValue(String brand) {
		if (brand.equals("11"))
			return 8;
		else if (brand.equals("12"))
			return 9;
		else if (brand.equals("13"))
			return 10;
		else if (brand.equals("99"))
			return BRAND_SIZE;
		else
			return Integer.valueOf(brand);
	}
	
    private static int getClassValue(String cardClass) {

        if (cardClass.equals("99"))
            return CLASS_SIZE;
        else
            return Integer.valueOf(cardClass);
    }
	
    private final static char[] digits = {
    	'0' , '1' , '2' , '3' , '4' , '5' ,
    	'6' , '7' , '8' , '9' , 'a' , 'b' ,
    	'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
    	'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
    	'o' , 'p' , 'q' , 'r' , 's' , 't' ,
    	'u' , 'v' , 'w' , 'x' , 'y' , 'z'
        };
    
	/**
	 * 
	 * @param rule
	 * @param attrMap
	 */
	public static RuleVo getRuleByMap(String ruleMap) {
		RuleVo rule = new RuleVo();
		int len = 0;
		rule.setCardNature(ruleMap.substring(0, len + NATURE_SIZE));
		len += NATURE_SIZE;
		rule.setCardClass(ruleMap.substring(len, len + CLASS_SIZE));
		len += CLASS_SIZE;
		rule.setCardBrand(ruleMap.substring(len, len + BRAND_SIZE));
		len += BRAND_SIZE;
		rule.setCardProduct(ruleMap.substring(len, len + PRODUCT_SIZE));
		len += PRODUCT_SIZE;
		rule.setCardLevel(ruleMap.substring(len, len + LEVEL_SIZE));
		return rule;
	}
	
	/**
	 * @param rule
	 * @param attrMap
	 */
	public static String getMapByRule(RuleVo rule) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(rule.getCardNature());
		buffer.append(rule.getCardClass());
		buffer.append(rule.getCardBrand());
		buffer.append(rule.getCardProduct());
		buffer.append(rule.getCardLevel());
		return StringUtils.rightPad(buffer.toString(), RULLMAP_MAX_SIZE, '0');
	}
	
	
	/**
	 * @param rule
	 * @return
	 */
	public static long getSupportRuleByCardBin(CardBinInfo bin) {
		long bitmap = 0L;
		int pos = 0;
		int nature = Integer.valueOf(bin.getCardNature());
		if (nature == 0) {
			for (int i = 1; i <= NATURE_SIZE; i++) {
				bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos - i);
			}
		} else {
			bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos - nature);
		}
		pos += NATURE_SIZE;
		int classv = getClassValue(bin.getCardClass());
		if (classv == 0) {
			for (int i = 1; i <= CLASS_SIZE; i++) {
				bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos - i);
			}
		} else {
			bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos - classv);
		}
		pos += CLASS_SIZE;
		int brand = getBrandValue(bin.getCardBrand());
		if (brand == 0) {
			for (int i = 1; i <= BRAND_SIZE; i++) {
				bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos - i);
			}
		} else {
			bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos - brand);
		}
		// bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos -
		// getBrandValue(bin.getCardBrand()));
		pos += BRAND_SIZE;

		int product = Integer.valueOf(bin.getCardProduct());
		if (product == 0) {
			for (int i = 1; i <= PRODUCT_SIZE; i++) {
				bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos - i);
			}
		} else {
			bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos - product);
		}
		// bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos -
		// Integer.valueOf(bin.getCardProduct()));
		pos += PRODUCT_SIZE;

		int level = Integer.valueOf(bin.getCardLevel());
		if (level == 0) {
			for (int i = 1; i <= LEVEL_SIZE; i++) {
				bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos - i);
			}
		} else {
			bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos - level);
		}
		// bitmap = set(bitmap, RULLMAP_MAX_SIZE - pos -
		// Integer.valueOf(bin.getCardLevel()));
		return bitmap;
	}
	
	/**
	 * 
	 * @param bitmap
	 * @param pos
	 * @return
	 */
	public static long set(long bitmap, int pos) {
		return bitmap |= (1L << (pos));
	}
	
	
	/**
	 * 
	 * @param bitmap
	 * @param pos
	 * @return
	 */
	public static long clear(long bitmap, int pos) {
		return bitmap &= ~(1L << (pos));
	}
	
	/**
	 * 
	 * @param bitmap
	 * @param pos
	 * @return
	 */
	public static int get(long bitmap, int pos) {
		return (bitmap & (1L << pos)) == 0 ? 0 : 1;
	}

	
	/**
	 * @param str
	 * @return
	 */
	public static long binaryStrToLong(String str) {
		long num = 0L;
		for (int i = 0; i < str.length(); i++) {
			num = (num << 1) + (str.charAt(i) - '0');
		}
		return num;
	}
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public static String binaryIntToStr(int i) {
		char buf[] = new char[SUPPMAP_SIZE];
		int charPos = SUPPMAP_SIZE;
		do {
			buf[--charPos] = digits[i & 1];
			i >>>= 1;
		} while (charPos != 0);
		return new String(buf);
	}

}
