/**
 * 
 */
package com.primeton.paas.app.config.impl;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.primeton.paas.app.util.StringUtil;
import com.primeton.paas.app.util.XmlUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConfigurationHelper {
	
	protected static String DELIM_START = "${";
	protected static char   DELIM_STOP  = '}';
	protected static int DELIM_START_LEN = 2;
	protected static int DELIM_STOP_LEN = 1;

	/**
	 * 
	 * Perform variable substitution in string <code>val</code> from the values
	 * of keys found in the system propeties.
	 * 
	 * <p>
	 * The variable substitution delimeters are <b>${</b> and <b>}</b>.
	 * 
	 * <p>
	 * For example, if the System properties contains "key=value", then the call
	 * 
	 * <pre>
	 * String s = getValueContainVars(&quot;Value of key is ${key}.&quot;);
	 * </pre>
	 * 
	 * will set the variable <code>s</code> to "Value of key is value.".
	 * 
	 * <p>
	 * If no value could be found for the specified key, then the
	 * <code>props</code> parameter is searched, if the value could not be found
	 * there, then substitution defaults to the empty string.
	 * 
	 * <p>
	 * For example, if system propeties contains no value for the key
	 * "inexistentKey", then the call
	 * 
	 * <pre>
	 * String s = getValueContainVars(&quot;Value of inexistentKey is [${inexistentKey}]&quot;);
	 * </pre>
	 * 
	 * will set <code>s</code> to "Value of inexistentKey is []"
	 * 
	 * <p>
	 * An {@link java.lang.IllegalArgumentException} is thrown if
	 * <code>val</code> contains a start delimeter "${" which is not balanced by
	 * a stop delimeter "}".
	 * </p>
	 * 
	 * <p>
	 * <b>Author</b> Avy Sharell</a>
	 * </p>
	 * 
	 * @param val
	 *            The string on which variable substitution is performed.
	 * @throws IllegalArgumentException
	 *             if <code>val</code> is malformed.
	 */
	public static String getValueContainVars(String val, Properties props) throws IllegalArgumentException {

		StringBuffer sbuf = new StringBuffer();

		int i = 0;
		int j, k;

		while (true) {
			j = val.indexOf(DELIM_START, i);
			if (j == -1) {
				// no more variables
				if (i == 0) { // this is a simple string
					return val;
				} else { // add the tail string which contails no variables and
							// return the result.
					sbuf.append(val.substring(i, val.length()));
					return sbuf.toString();
				}
			} else {
				sbuf.append(val.substring(i, j));
				k = val.indexOf(DELIM_STOP, j);
				if (k == -1) {
					throw new IllegalArgumentException(
							'"'		+ val
									+ "\" has no closing brace. Opening brace at position "
									+ j + '.');
				} else {
					j += DELIM_START_LEN;
					String key = val.substring(j, k);
					// first try in System properties
					String replacement = getSystemProperty(key, null);
					// then try props parameter
					if (replacement == null && props != null) {
						replacement = props.getProperty(key);
					}

					if (replacement != null) {
						// Do variable substitution on the replacement string
						// such that we can solve "Hello ${x2}" as "Hello p1"
						// the where the properties are
						// x1=p1
						// x2=${x1}
						String recursiveReplacement = getValueContainVars(replacement, props);
						sbuf.append(recursiveReplacement);
					}
					i = k + DELIM_STOP_LEN;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	public static String getSystemProperty(String key, String def) {
		try {
			return System.getProperty(key, def);
		} catch (Throwable e) { // MS-Java throws com.ms.security.SecurityExceptionEx
			return def;
		}
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isSimpleNode(Node node) {
		NodeList childList = node.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			short type = childList.item(i).getNodeType();
			if ((type != Node.COMMENT_NODE) && (type != Node.TEXT_NODE)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public static void removeChild(Element parent, Element child) {
		if (child == null || parent == null) {
			return;
		}
		Document doc = parent.getOwnerDocument();
		
		if (doc == child.getOwnerDocument()) {
			parent.removeChild(child);
		} else {
			parent.removeChild(doc.importNode(child, true));
		}
	}
	
	/**
	 * 
	 * @param parent
	 * @param childNodeName
	 * @param childNodeAttriName
	 * @param childNodeAttriValue
	 * @return
	 */
	public static Element appendChild(Element parent, String childNodeName, String childNodeAttriName, String childNodeAttriValue) {
		Element child = XmlUtil.createField(parent, childNodeName);
		XmlUtil.setAttrValue(child, childNodeAttriName, childNodeAttriValue);
		return child;
	}

	/**
	 * 
	 * @param parent
	 * @param childNodeName
	 * @param childNodeAttriName
	 * @param childNodeAttriValue
	 * @return
	 */
	public static Element removeChild(Element parent, String childNodeName, String childNodeAttriName, String childNodeAttriValue) {
		NodeList childList = parent.getElementsByTagName(childNodeName);
		for (int i = 0; i < childList.getLength(); i++) {
			Element child = (Element)childList.item(i);
			if (StringUtil.equal(childNodeAttriValue, child.getAttribute(childNodeAttriName))) {
				parent.removeChild(child);
				return child;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param parent
	 * @param childNodeName
	 * @param childNodeAttriName
	 * @param childNodeAttriValue
	 * @return
	 */
	public static Element getChild(Element parent, String childNodeName, String childNodeAttriName, String childNodeAttriValue) {
		NodeList childList = parent.getElementsByTagName(childNodeName);
		for (int i = 0; i < childList.getLength(); i++) {
			Element child = (Element) childList.item(i);
			if (StringUtil.equal(childNodeAttriValue, child.getAttribute(childNodeAttriName))) {
				return child;
			}
		}
		return null;
	}

}
