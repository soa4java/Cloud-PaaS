/**
 * 
 */
package com.primeton.paas.openapi.base.uitl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;

import org.apache.commons.io.IOUtils;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.primeton.paas.openapi.base.uitl.impl.XPathAPI;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public final class XmlUtil {

	static final String XPATH_NODE_DELIM = "/";

	static final String XPATH_ATTR_DELIM = "@";

	static final String XPATH_ATTR_LINCL = "[";

	static final String XPATH_ATTR_RINCL = "]";

	static final String XPATH_ATTR_QUOTA = "\"";

	static final String XPATH_ATTR_SQUOTA = "'";

	static final String XPATH_ATTR_EQ = "=";

	// private static final String XPATH_SPACE_CHAR = "\t\n\r";

	public final static String UTF_XML_ENCODING = "UTF-8";

	final static String ENCODING_STR = "encoding";

	public static final String GBK_XML_ENCODING = "GBK";

	private static final String TAB_SPACE = "4";

	private static char[] chars = { 'n', (char) 9, (char) 10, (char) 13, ' ' };

	private XmlUtil() {
		super();
	}

	private static String fileXMLHeader(String fileName) {
		BufferedReader in = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(fileName);
			in = new BufferedReader(new InputStreamReader(input,
					UTF_XML_ENCODING));
			String header = in.readLine();
			if (header == null) {
				return null;
			} else {
				header = header.trim();
			}
			if (header.startsWith("<?") && header.endsWith("?>")) {
				return header;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(input);
		}
	}

	private static String xmlEncoding(String header) {
		if ((header == null) || header.equals("")) {
			return null;
		}

		int op = header.indexOf(ENCODING_STR);
		if (op == -1) {
			return null;
		}
		op = header.indexOf("\"", op);
		if (op == -1) {
			return null;
		}

		String tmp = header.substring(op + "\"".length());
		op = tmp.indexOf("\"");
		if (op == -1) {
			return null;
		}
		tmp = tmp.substring(0, op);
		return tmp.trim();
	}

	private static String stringXMLHeader(String xml) {
		int start = xml.indexOf("<?");
		if ((start != -1) && (xml.substring(0, start).trim().length() <= 2)) {
			int end = xml.indexOf("?>", start);
			if (end != -1) {
				return xml.substring(start, end + 2);
			}
		}
		return null;
	}

	public static Document newDocument() throws XmlUtilException {
		return getXMLBuilder().newDocument();
	}

	@Deprecated
	public final static Document getNewDocument() throws XmlUtilException {
		Document doc = getXMLBuilder().newDocument();
		return doc;
	}

	public static Document getOwnerDocument(Node node) {
		Document doc = null;
		if (node.getNodeType() == Node.DOCUMENT_NODE) {
			doc = (Document) node;
		} else {
			doc = node.getOwnerDocument();
		}
		return doc;
	}

	public final static Document setRoot(Document doc, String rootName) {
		Node root = doc.getDocumentElement();
		if (root != null) {
			doc.removeChild(root);
		}
		doc.appendChild(doc.createElement(rootName));
		return doc;
	}

	public final static Document setRoot(Document doc, Node root) {
		Node oldRoot = doc.getDocumentElement();
		if (oldRoot != null) {
			doc.removeChild(oldRoot);
		}
		doc.appendChild(root);
		return doc;
	}

	public final static Element getRoot(Document doc) {
		return doc.getDocumentElement();
	}

	public final static String getXmlEncoding(String fileName) {
		String header = fileXMLHeader(fileName);
		return xmlEncoding(header);
	}

	public static void createAttr(Document doc, Node atNode, String attrName,
			String attrValue) {
		Attr newAttr = doc.createAttribute(attrName);
		newAttr.setNodeValue(attrValue);
		atNode.getAttributes().setNamedItem(newAttr);
	}

	public static Element createElement(Document doc, String name, Object value) {
		if (name == null) {
			return null;
		}
		Element element = doc.createElement(name);
		if (value != null) {
			setElementValue(element, value);
		}
		return element;
	}

	public static void setElementValue(Element ele, Object value) {
		if (value == null) {
			return;
		}
		setNodeValue(ele, value.toString());
	}

	public static Element getElement(Element parentNode, String nodeName) {
		if (parentNode == null) {
			return null;
		}
		NodeList nl = parentNode.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if (nl.item(i).getNodeName().equals(nodeName)) {
					return (Element) nl.item(i);
				}
			}
		}
		return null;
	}

	public static Element createField(Element anElement, String name) {
		Element field = anElement.getOwnerDocument().createElement(name);
		anElement.appendChild(field);
		return field;
	}

	public static void setFieldValue(Element ele, String name, Object value) {
		if ((name == null) || (value == null)) {
			return;
		} else {
			Element e = subElement(ele, name);
			if (e == null) {
				e = createField(ele, name);
			}
			setElementValue(e, value);
		}
	}

	public static String getAttrValue(Element ele, String name) {
		if (name == null) {
			return null;
		} else {
			return ele.getAttribute(name);
		}
	}

	public static String getNodeValue(Node node, String xql)
			throws XmlUtilException {
		Node childNode = findNode(node, xql);
		if (childNode == null) {
			return null;
		} else {
			return getNodeValue(childNode);
		}
	}

	public final static NodeIterator findNodeIterator(Node node, String xql)
			throws XmlUtilException {
		return findNodeIterator(node, xql, false);
	}

	protected final static NodeIterator findNodeIterator(Node node, String xql,
			boolean isUseXalanParser) throws XmlUtilException {
		if ((xql == null) || (xql.length() == 0)) {
			throw new XmlUtilException("xpath is null!");
		}
		if (node == null) {
			throw new XmlUtilException("node is null!");
		}
		if (node.getNodeType() != Node.DOCUMENT_NODE) {
			xql = processXql(xql);
		}
		try {
			return XPathAPI.selectNodeIterator(node, xql);

		} catch (Exception e) {
			throw new XmlUtilException(e);
		}
	}

	public final static Node setNodeValue(Node node, String xql, String value)
			throws XmlUtilException {
		Node targetNode = findNode(node, xql);
		if (targetNode == null) {
			targetNode = appendNode(node, xql);
		}
		setNodeValue(targetNode, value);
		return targetNode;
	}

	public final static Node setNodeValue(Document doc, String xql, String value)
			throws XmlUtilException {
		Node targetNode = findNode(doc, xql);
		if (targetNode == null) {
			targetNode = appendNode(doc, xql);
		}
		NodeList children = targetNode.getChildNodes();
		int index = 0;
		int length = children.getLength();

		Node tmpNode = null;
		// Remove all of the current contents(TextNode)
		for (index = 0; index < length; index++) {
			tmpNode = children.item(index);
			if (tmpNode.getNodeType() == Node.TEXT_NODE) {
				targetNode.removeChild(tmpNode);
			}
		}
		setNodeValue(targetNode, value);
		return targetNode;
	}

	public static void setNodeValue(Node node, String value) {
		if (node == null) {
			return;
		} else {
			Node childNode = null;
			switch (node.getNodeType()) {
			case (Node.ELEMENT_NODE):
				childNode = node.getFirstChild();
				if (childNode == null) {
					childNode = node.getOwnerDocument().createTextNode(value);
					node.appendChild(childNode);
				} else if (childNode.getNodeType() == Node.TEXT_NODE) {
					childNode.setNodeValue(value);
				} else {
					node.appendChild(node.getOwnerDocument().createTextNode(
							value));
				}
				return;
			case (Node.TEXT_NODE):
				node.setNodeValue(value);
				return;
			case (Node.ATTRIBUTE_NODE):
				node.setNodeValue(value);
				return;
			}
		}
	}

	public final static Node setAttribute(Node node, String xql, String name,
			String value) throws XmlUtilException {
		Node childNode = findNode(node, xql);

		if (childNode == null) {
			childNode = appendNode(node, xql);
		}

		((Element) childNode).setAttribute(name, value);
		return childNode;
	}

	public static Node getNodeByAttribute(Node parent, String attributeName,
			String value) {
		if (parent == null || attributeName == null || value == null) {
			return null;
		}
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.hasAttributes()
					&& value.equals(getAttributeValue(child, attributeName))) {
				return child;
			}
			Node node = getNodeByAttribute(child, attributeName, value);
			if (node != null) {
				return node;
			}
		}
		return null;
	}

	public final static String getAttribute(Node node, String xql, String name)
			throws XmlUtilException {
		Node childNode = findNode(node, xql);

		if (childNode == null) {
			return null;
		}
		return ((Element) childNode).getAttribute(name);
	}

	public final static String findAttr(Node node, String attrName)
			throws XmlUtilException {
		if ((attrName == null) || (attrName.length() == 0)) {
			throw new XmlUtilException("attrName == null");
		}
		if (node == null) {
			throw new XmlUtilException("Source node == null");
		}
		Node attrNode = findNode(node, "@" + attrName);
		if (attrNode != null) {
			return attrNode.getNodeValue();
		}
		return null;
	}

	protected static Element createNode(Document doc, String path) {
		StringTokenizer stk = new StringTokenizer(path, XPATH_ATTR_LINCL
				.concat(XPATH_ATTR_RINCL).concat(XPATH_ATTR_DELIM)
				.concat(XPATH_ATTR_QUOTA).concat(XPATH_ATTR_SQUOTA));

		String eleName = null;
		String eleAttrName = null;
		String eleAttrVal = null;
		if (stk.hasMoreTokens()) {
			eleName = stk.nextToken();
		}
		if (eleName == null) {
			return null;
		}
		Element retElement = doc.createElement(eleName);
		while (stk.hasMoreTokens()) {
			eleAttrName = stk.nextToken();
			int index = eleAttrName.lastIndexOf(XPATH_ATTR_EQ);
			if (index >= 0) {
				eleAttrName = eleAttrName.substring(0, index);
			}
			if (stk.hasMoreTokens()) {
				eleAttrVal = stk.nextToken();
			}
			if (eleAttrName != null) {
				retElement.setAttribute(eleAttrName, eleAttrVal);
			}
		}

		return retElement;
	}

	public final static Node appendNode(Node node, String xql)
			throws XmlUtilException {
		if ((xql == null) || (xql.length() == 0)) {
			throw new XmlUtilException("xpath is null!");
		}
		if (node == null) {
			throw new XmlUtilException("node is null!");
		}
		String[] saXql = StringUtil.split(xql, XPATH_NODE_DELIM);
		if (saXql.length < 1) {
			throw new XmlUtilException("xpath is invalid!");
		}
		Document doc = getOwnerDocument(node);
		Node root = getRootNode(node, xql);
		if (root == null) {
			throw new XmlUtilException("root node is null!");
		}
		Node item = root;
		for (int i = 0; i < saXql.length; i++) {
			String itemXql = subXql(saXql, i);
			item = findNode(root, itemXql);

			if ((item == null) || (i == saXql.length - 1)) {
				// item = doc.createElement(saXql[i]);
				Node parentNode = (i == 0 ? null : findNode(root,
						subXql(saXql, i - 1)));
				if (saXql[i].startsWith(XPATH_ATTR_DELIM)) { // �����/root/data/list/@name�����xpath������name����
					String attrName = saXql[i].substring(1, saXql[i].length());
					if (parentNode == null) {
						((Element) root).setAttribute(attrName, "");
						item = ((Element) root).getAttributeNode(attrName);
					} else {
						((Element) parentNode).setAttribute(attrName, "");
						item = ((Element) parentNode)
								.getAttributeNode(attrName);
					}
				} else {
					item = createNode(doc, saXql[i]);
					if (parentNode == null) {
						root.appendChild(item);
					} else {
						parentNode.appendChild(item);
					}
				}
			}
		}
		return item;
	}

	private final static String subXql(String[] saXql, int index) {
		String subXql = "";
		for (int i = 0; i <= index; i++) {
			if (i >= saXql.length) {
				break;
			}
			if (subXql != "") {
				subXql = subXql.concat(XPATH_NODE_DELIM).concat(saXql[i]);
			} else {
				subXql = subXql.concat(saXql[i]);
			}
		}
		return subXql;
	}

	private static Node getRootNode(Node node, String xql)
			throws XmlUtilException {
		if (node.getNodeType() == Node.DOCUMENT_NODE) {
			String subXPath = xql;
			if (subXPath.startsWith(".".concat(XPATH_NODE_DELIM))) {
				subXPath = subXPath.substring(2);
			} else if (subXPath.startsWith(XPATH_NODE_DELIM)) {
				subXPath = subXPath.substring(1);
			}

			String[] xpath = StringUtil.split(subXPath, XPATH_NODE_DELIM);
			if (xpath.length <= 1) {
				throw new XmlUtilException("xpath is invalid!");
			}
			subXPath = xpath[0];

			Element docElement = getOwnerDocument(node).getDocumentElement();
			String nodeName = docElement.getNodeName();
			if (nodeName.equals(subXPath)) {
				return node;
			} else {
				throw new XmlUtilException("xpath is invalid!");
			}
		} else {
			return node;
		}
	}

	public final static Node appendNode(Node parentNode, Node childNode) {
		Document doc = parentNode.getOwnerDocument();
		if (doc == childNode.getOwnerDocument()) {
			return parentNode.appendChild(childNode);
		} else {
			return parentNode.appendChild(doc.importNode(childNode, true));
		}
	}

	public final static Node cloneNode(Node node, boolean isDepth) {
		return node.cloneNode(isDepth);
	}

	public final static Node copyNode(Node node, String srcXql, String desXql)
			throws XmlUtilException {
		Node srcNode = findNode(node, srcXql);
		if (srcNode == null) {
			return null;
		}
		Node targetNode = findNode(node, desXql);
		if (targetNode == null) {
			targetNode = appendNode(node, desXql);
		} else {
			if (targetNode.getNodeType() == Node.DOCUMENT_NODE) {
				targetNode = getOwnerDocument(targetNode).getDocumentElement();
			}
		}
		if (targetNode.getNodeType() != Node.ELEMENT_NODE) {
			throw new XmlUtilException(
					"destination node's type invalid, expected is ELEMENT_NODE, actual is "
							+ targetNode.getNodeType());
		}
		Node resultNode = cloneNode(srcNode, true);
		if (resultNode.getNodeType() == Node.ATTRIBUTE_NODE) {
			((Element) targetNode).setAttribute(resultNode.getNodeName(),
					getNodeValue(resultNode));
		} else {
			appendNode(targetNode, resultNode);
		}
		return resultNode;
	}

	public final static Node moveNode(Node node, String srcXql, String desXql)
			throws XmlUtilException {
		Node childNode = findNode(node, srcXql);
		if (childNode == null) {
			return null;
		}
		Node targetNode = findNode(node, desXql);
		if (targetNode == null) {
			targetNode = appendNode(node, desXql);
		}
		childNode = cloneNode(childNode, true);
		removeNode(node, srcXql);
		targetNode.appendChild(childNode);
		return targetNode;
	}

	public final static int moveNodes(Node node, String srcXql, String desXql)
			throws XmlUtilException {
		Node targetNode = findNode(node, desXql);
		if (targetNode == null) {
			targetNode = appendNode(node, desXql);
		}
		NodeList nodes = findNodes(node, srcXql);
		int length = nodes.getLength();
		for (int i = 0; i < length; i++) {
			Node tmp = nodes.item(i);
			Node newNode = tmp.cloneNode(true);
			targetNode.appendChild(newNode);
		}
		removeNodes(node, srcXql);
		return length;
	}

	public final static Node removeNode(Node node, String xql)
			throws XmlUtilException {
		Node childNode = findNode(node, xql);
		if (childNode == null) {
			return null;
		}
		return childNode.getParentNode().removeChild(childNode);
	}

	public final static void removeNodes(Node node, String xql)
			throws XmlUtilException {
		NodeList nodes = findNodes(node, xql);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node tmp = nodes.item(i);
			tmp.getParentNode().removeChild(tmp);
		}
	}

	public static Node removeSpaceNode(Node node) {
		NodeList list = node.getChildNodes();
		if ((list.getLength() == 1)
				&& (list.item(0).getNodeType() == Node.TEXT_NODE)
				&& (node.getNodeType() == Node.ELEMENT_NODE)) {
			return node;
		}
		for (int i = 0; i < list.getLength(); i++) {
			Node tmpNode = list.item(i);
			if (tmpNode.getNodeType() == Node.TEXT_NODE) {
				String str = tmpNode.getNodeValue();
				if (isSpace(str)) {
					tmpNode.getParentNode().removeChild(tmpNode);
					i--;
				}
			} else if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
				removeSpaceNode(tmpNode);
			}
		}
		return node;
	}

	public static void removeAllChildNode(Node node) {
		if (node == null) {
			return;
		}
		Node child = node.getFirstChild();
		while (true) {
			if (child == null) {
				break;
			}
			node.removeChild(child);
			child = node.getFirstChild();
		}
	}

	public static Node createElement(Document doc, Node parentNode,
			String nodeName) {
		Node newNode = doc.createElement(nodeName);
		parentNode.appendChild(newNode);
		return newNode;
	}

	public static Element createElement(Document document, String name) {
		Element element = document.createElement(name);
		document.appendChild(element);
		return element;
	}

	public static Element createElement(Element parent, String name) {
		return createField(parent, name, null);
	}

	public static Element createField(Element parent, String name, String value) {
		Document document = parent.getOwnerDocument();
		if (document == null) {
			return null;
		}
		// Creates an element of the type specified
		Element element = document.createElement(name);
		// Adds the node newChild to the end of the list of children of this
		// node
		parent.appendChild(element);

		if ((value != null) && !value.equals("")) {
			// Creates a Text node given the specified string
			Text text = document.createTextNode(value);
			element.appendChild(text);
		}
		return element;
	}

	public static String getFieldValue(Element element, String name) {
		if ((element == null) || (name == null) || name.equals("")) {
			return null;
		}
		// get the NodeList that contains all children of this node
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node field = children.item(i);
			if ((field instanceof Element) && field.getNodeName().equals(name)) {
				return getTextValue(field);
			}
		}
		return null;
	}

	public static Element getChild(Element element, String fieldName) {
		if ((element == null) || (fieldName == null) || fieldName.equals("")) {
			return null;
		}
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if ((child instanceof Element)
					&& child.getNodeName().equals(fieldName)) {
				return (Element) child;
			}
		}
		return null;
	}

	public static String getTextValue(Node node) {
		NodeList children = node.getChildNodes();
		if (children == null) {
			return ""; //$NON-NLS-1$
		}
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				String value = ((org.w3c.dom.Text) child).getNodeValue();
				if (value != null) {
					value = value.trim();
				}
				return value;
			}
		}
		return null;
	}

	public static String getAttributeValue(Node node, String name) {
		NamedNodeMap map = node.getAttributes();
		Node item = map.getNamedItem(name);
		if (null != item) {
			return item.getNodeValue();
		} else {
			return null;
		}
	}

	public static void setAttribute(Element element, String name, String value) {
		if ((element == null) || (value == null) || value.equals("")) {
			return;
		}
		element.setAttribute(name, value.trim());
	}

	public static void setAttrValue(Element ele, String name, Object value) {
		if (value == null) {
			return;
		} else {
			ele.setAttribute(name, value.toString());
		}
	}

	public final static NodeList findNodes(Node node, String xql)
			throws XmlUtilException {
		return findNodes(node, xql, false);
	}

	protected final static NodeList findNodes(Node node, String xql,
			boolean isUseXalanParser) throws XmlUtilException {
		if ((xql == null) || (xql.length() == 0)) {
			throw new XmlUtilException("xql == null");
		}
		if (node == null) {
			throw new XmlUtilException("Node == null");
		}
		try {
			if (node.getNodeType() != Node.DOCUMENT_NODE) {
				xql = processXql(xql);
			}

			return XPathAPI.selectNodeList(node, xql);

		} catch (Exception e) {
			throw new XmlUtilException(e);
		}
	}

	public final static Node findNode(Node node, String xql)
			throws XmlUtilException {
		return findNode(node, xql, false);
	}

	protected final static Node findNode(Node node, String xql,
			boolean isUseXalanParser) throws XmlUtilException {
		if ((xql == null) || (xql.length() == 0)) {
			throw new XmlUtilException("xql == null");
		}

		if (node == null) {
			throw new XmlUtilException("Source node == null");
		}
		try {
			if (node.getNodeType() != Node.DOCUMENT_NODE) {
				xql = processXql(xql);
			}
			return XPathAPI.selectSingleNode(node, xql);
		} catch (Exception e) {
			throw new XmlUtilException(e);
		}
	}

	private final static String processXql(String xql) {
		if ((xql.length() > 1) && (xql.charAt(0) == '/')) {
			xql = ".".concat(xql);
		}
		return xql;
	}

	public final static String subElementValue(Element superEle, String subName) {
		Element element = subElement(superEle, subName);
		if (element == null) {
			return null;
		} else {
			return getElementValue(element);
		}
	}

	public final static Element subElement(Element superEle, String subName) {
		NodeList list = superEle.getChildNodes();
		if (list == null) {
			return null;
		}
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals(subName)) {
					return (Element) node;
				}
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final static Vector subElementList(Element superEle, String subName) {
		Vector v = new Vector();
		NodeList list = superEle.getChildNodes();
		if (list == null) {
			return null;
		}

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals(subName)) {
					v.add(node);
				}
			}
		}
		return v;
	}

	public static String getElementValue(Element ele) {
		if (ele == null)
			return null;
		else {
			return getNodeValue(ele);
		}
	}

	public static Element find(Element element, String name) {
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if ((child instanceof Element) && child.getNodeName().equals(name)) {
				return (Element) child;
			}
		}
		return null;
	}

	public static Element findGrandson(Element element, String childName,
			String grandsonName) {
		Element child = find(element, childName);

		if (child == null) {
			return null;
		}
		return find(child, grandsonName);
	}

	public static String getGrandsonValue(Element element, String childName,
			String grandsonName) {
		Element grandson = findGrandson(element, childName, grandsonName);
		if (grandson == null) {
			return null;
		}
		return getTextValue(grandson);
	}

	public static String getNodeValue(Node node) {
		String value = null;
		if (node == null) {
			return null;
		} else {
			switch (node.getNodeType()) {
			case (Node.ELEMENT_NODE):
				StringBuffer contents = new StringBuffer();
				NodeList childNodes = node.getChildNodes();
				int length = childNodes.getLength();
				if (length == 0) {
					return null;
				}
				for (int i = 0; i < length; i++) {
					if (childNodes.item(i).getNodeType() == Node.TEXT_NODE) {
						contents.append(childNodes.item(i).getNodeValue());
					}
				}
				value = contents.toString();
				break;
			case (Node.TEXT_NODE):
				value = node.getNodeValue();
				break;
			case (Node.ATTRIBUTE_NODE):
				value = node.getNodeValue();
				break;
			}
		}
		if (value == null) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			result.append(c);
		}
		return result.toString().trim();
	}

	public static String getPath(Node node) {
		String path = "";
		Node current = node;
		while ((current != null) && !(current instanceof Document)) {
			path = current.getNodeName() + "/" + path;
			current = current.getParentNode();
		}
		if (path != null) {
			path = path.substring(0, path.lastIndexOf("/"));
		}
		return path;
	}

	public static Node removeSpace(Node node) {
		NodeList list = node.getChildNodes();
		if (list == null || list.getLength() == 0) {
			return node;
		}
		if ((list.getLength() == 1)
				&& (list.item(0).getNodeType() == Node.TEXT_NODE)
				&& (node.getNodeType() == Node.ELEMENT_NODE)) {
			Text text = (Text) node.getChildNodes().item(0);
			String data = text.getData();
			if (data != null) {
				text.setData(data.trim());
			}
			return node;
		}

		for (int i = 0; i < list.getLength(); i++) {
			Node tmpNode = list.item(i);
			if (tmpNode.getNodeType() == Node.TEXT_NODE) {
				String str = tmpNode.getNodeValue();
				if (XmlUtil.isSpace(str)) {
					tmpNode.getParentNode().removeChild(tmpNode);
					i--;
				}
			} else if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
				removeSpace(tmpNode);
			}
		}
		return node;
	}

	private static boolean isSpace(String str) {
		if (str == null) {
			return true;
		}
		for (int i = 0; i < str.length(); i++) {
			for (int j = 0; j < chars.length; j++) {
				if (!isSpace(str.charAt(i))) {
					return false;
				}
			}
		}
		return true;
	}

	private static boolean isSpace(char c) {
		for (int j = 0; j < chars.length; j++) {
			if (chars[j] == c) {
				return true;
			}
		}
		return false;
	}

	public final static Document parseString(String xml)
			throws XmlUtilException, SAXException, IOException {
		return parseStringThrowsException(xml);
	}

	public final static Document parseStringThrowsException(String xml)
			throws XmlUtilException, SAXException, IOException {
		String header = stringXMLHeader(xml);
		String encoding = xmlEncoding(header);
		return parseStringThrowsException(xml, encoding);
	}

	public final static Document parseStringThrowsException(String xml,
			String encoding) throws XmlUtilException, SAXException, IOException {
		try {
			ByteArrayInputStream bais = null;
			if (encoding == null) {
				bais = new ByteArrayInputStream(xml.getBytes());
			} else {
				bais = new ByteArrayInputStream(xml.getBytes(encoding));
			}

			return getXMLBuilder().parse(bais);
		} catch (XmlUtilException e) {
			throw new XmlUtilException(xml, e);
		} catch (SAXException e) {
			throw new SAXException(xml, e);
		} catch (IOException e) {
			throw new IOException(StringUtil.concat(e.getMessage(), ": ", xml));
		}
	}

	public final static Document parseFile(String fileName)
			throws XmlUtilException {
		return parseFileThrowsException(new File(fileName));
	}

	public final static Document parseFileThrowsException(String fileName,
			String encoding) throws XmlUtilException, SAXException, IOException {

		File f = new File(fileName);
		InputStream input = null;
		InputStreamReader isr = null;
		LineNumberReader lnr = null;
		try {
			input = new FileInputStream(f);
			if ((encoding == null) || encoding.equals("")) {
				encoding = System.getProperty("encoding", UTF_XML_ENCODING);
			}

			isr = new InputStreamReader(input, encoding);
			lnr = new LineNumberReader(isr);
			InputSource isrc = new InputSource(lnr);

			Document doc = getXMLBuilder().parse(isrc);
			return doc;
		} catch (XmlUtilException e) {
			throw new XmlUtilException(f.getAbsolutePath(), e);
		} catch (SAXException e) {
			throw new SAXException(f.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new IOException(StringUtil.concat(e.getMessage(), ": ",
					f.getAbsolutePath()));
		} finally {
			IOUtils.closeQuietly(lnr);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(input);
		}
	}

	public final static Document parseFileThrowsException(File file)
			throws XmlUtilException {
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			Document document = getXMLBuilder().parse(is);
			return document;
		} catch (Exception e) {
			throw new XmlUtilException(file == null ? "file is null"
					: file.getAbsolutePath(), e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public final static Document parseFileThrowsException(File file,
			String systemId) throws XmlUtilException {

		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			Document document = getXMLBuilder().parse(is, systemId);
			return document;
		} catch (Exception e) {
			throw new XmlUtilException(file == null ? "file is null"
					: file.getAbsolutePath(), e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public final static Document parseStream(InputStream stream)
			throws XmlUtilException {
		try {
			return getXMLBuilder().parse(stream);
		} catch (Exception e) {
			throw new XmlUtilException(e);
		}
	}

	public final static Document parseStrings(String source) throws Exception {
		return parseStrings(source, UTF_XML_ENCODING);
	}

	public final static Document parseStrings(String source, String encoding)
			throws Exception {
		try {
			if ((source != null) && (source.length() > 1)
					&& (source.charAt(0) == 65279)) {
				source = source.substring(1);
			}

			ByteArrayInputStream inputStream = null;
			source.charAt(0);
			inputStream = new ByteArrayInputStream(source.getBytes(encoding));
			Document document = getXMLBuilder().parse(inputStream);
			return document;
		} catch (Exception e) {
			throw new Exception(source, e);
		}
	}

	public final static String doc2String(Document doc) {
		return node2String(doc.getDocumentElement());
	}

	public final static String node2String(Node node, boolean isFormat,
			String head) throws XmlUtilException {
		String content = node2String(node, isFormat, false);
		if (head == null || head.trim().length() == 0)
			return content;
		else
			return head.concat(content);
	}

	public final static String node2String(Node node) throws XmlUtilException {
		return node2String(node, false);
	}

	public final static String node2String(Node node, boolean isFormat)
			throws XmlUtilException {
		return node2String(node, isFormat, true);
	}

	public final static String node2String(Node node, boolean isFormat,
			boolean hasHead) throws XmlUtilException {
		return node2String(node, isFormat, hasHead, UTF_XML_ENCODING);
	}

	public final static String node2String(Node node, boolean isFormat,
			boolean hasHead, String encoding) throws XmlUtilException {
		if (node == null) {
			return null;
		}
		if (node.getNodeType() == Node.DOCUMENT_NODE) {
			Node elem = ((Document) node).getDocumentElement();
			if (elem != null) {
				node = elem;
			}
		}
		CharArrayWriter writer = new CharArrayWriter();
		Properties prop = OutputPropertiesFactory
				.getDefaultMethodProperties("xml");
		prop.setProperty("encoding", encoding);

		Serializer serializer = SerializerFactory.getSerializer(prop);
		if (isFormat) {
			prop.setProperty(OutputKeys.INDENT, "yes");
			prop.setProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT,
					TAB_SPACE);
			XmlUtil.removeSpace(node);
		} else {
			prop.setProperty(OutputKeys.INDENT, "no");
		}
		serializer.setOutputFormat(prop);
		serializer.setWriter(writer);
		try {
			serializer.asDOMSerializer().serialize(node);
		} catch (IOException e) {
			throw new XmlUtilException(e);
		}

		String str = new String(writer.toCharArray());
		if (hasHead) {
			if (!str.startsWith("<?xml")) {
				str = ("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n")
						.concat(str);
			}
			return str;
		} else {
			if (!str.startsWith("<?xml")) {
				return str;
			}
			int op = str.indexOf("?>");
			str = str.substring(op + "?>".length()).trim();
			if (str.startsWith("\n")) {
				str = str.substring(1);
			}

			return str;
		}
	}

	public static synchronized void saveToFile(Document doc, String filePath)
			throws XmlUtilException, IOException {
		saveDocument(doc, filePath, false);
	}

	public static synchronized void saveToFile(Document doc, File file)
			throws XmlUtilException, IOException {
		saveDocument(doc, file, false);
	}

	public static void saveDocument(Document doc, String filePath,
			boolean isFormat) throws IOException {
		if ((doc == null) || (filePath == null)) {
			return;
		}
		filePath = filePath.replace('/', File.separatorChar);
		filePath = filePath.replace('\\', File.separatorChar);
		File file = new File(filePath);
		saveDocument(doc, file, isFormat);
	}

	public static void saveDocument(Document doc, File file, boolean isFormat)
			throws IOException {
		saveDocument(doc, file, isFormat, XmlUtil.UTF_XML_ENCODING);
	}

	public static void saveDocument(Document doc, File file, boolean isFormat,
			String encoding) throws IOException {
		if ((doc == null) || (file == null)) {
			return;
		}
		String content = null;
		content = node2String(doc, isFormat, true);
		if (!file.exists()) {
			file.createNewFile();
			file.renameTo(file);
		}
		BufferedReader in = new BufferedReader(new CharArrayReader(
				content.toCharArray()));
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), encoding));

			String tmp = in.readLine();
			while (true) {
				if (tmp == null) {
					break;
				} else {
					writer.write(tmp);
					writer.write("\r\n"); //$NON-NLS-1$
					writer.flush();
				}
				tmp = in.readLine();
			}
		} catch (IOException e) {
			throw new IOException(StringUtil.concat(e.getMessage(), ": ",
					file.getAbsolutePath()));
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	private static DocumentBuilderFactory dbf = DocumentBuilderFactory
			.newInstance();
	static {
		dbf.setIgnoringElementContentWhitespace(true);
	}

	private final static DocumentBuilder getXMLBuilder()
			throws XmlUtilException {
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db;
		} catch (ParserConfigurationException e) {
			throw new XmlUtilException(e);
		}
	}

	public static String readFile(File file, String charsetName)
			throws Exception {
		if ((file == null) || !file.exists()) {
			return null;
		}

		FileInputStream inputStream = null;

		try {
			inputStream = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, charsetName));
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append("\n"); //$NON-NLS-1$
			}
			return buffer.toString();
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
}
