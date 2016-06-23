/**
 * 
 */
package com.primeton.paas.app.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.primeton.paas.app.config.impl.ConfigurationHelper;
import com.primeton.paas.app.util.StringUtil;
import com.primeton.paas.app.util.UrlUtil;
import com.primeton.paas.app.util.XmlUtil;


/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class Configuration implements Serializable, Cloneable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -5116314070984865987L;
	
	private String filePath = null;

	private transient Document document = null;
	
	private Map<String, Module> modules = new LinkedHashMap<String, Module>();
	
	private Properties _prop = new Properties();
	
	protected final static String MODULE = "module";

	protected final static String GROUP = "group";

	protected final static String VALUE = "configValue";

	protected final static String MODULE_NAME = "name";

	protected final static String GROUP_NAME = "name";

	protected final static String VALUE_KEY = "key";

	protected final static String EOSConfig = "EOSConfig";

	protected final static String DEFAULT_MODULE_NAME = "global";
	
	protected final static String PROPERTY = "property";
	
	protected final static String PROPERTY_NAME = "name";
	
	protected final static String PROPERTY_VALUE = "value";
	
	protected final static String PROPERTY_LOCATION = "location";
	
	/**
	 * 
	 * @param configPath
	 * @throws ConfigurationRuntimeException
	 */
	private Configuration(String configPath) throws ConfigurationRuntimeException {
		if (configPath == null) {
			throw new ConfigurationRuntimeException("ConfigPath is null!");
		}
		filePath = new File(configPath).getAbsolutePath();		
		try {
			document = XmlUtil.parseFile(filePath);
		} catch (Exception e) {
			throw new ConfigurationRuntimeException("File[{0}] parse error!", new Object[]{filePath}, e);
		}
		parse(document, modules);
	}

	/**
	 * 
	 * @param confStream
	 * @throws ConfigurationRuntimeException
	 */
	private Configuration(InputStream confStream) throws ConfigurationRuntimeException {
		if (confStream == null) {
			throw new ConfigurationRuntimeException("ConfigStream is null!");
		}
		try {
			Document doc = XmlUtil.parseStream(confStream);
			parse(doc, this.modules);
		} catch (Exception e) {
			throw new ConfigurationRuntimeException("Document parse error!", e);
		}
	}

	/**
	 * 
	 * @param doc
	 * @throws ConfigurationRuntimeException
	 */
	private Configuration(Document doc) throws ConfigurationRuntimeException {
		if (doc == null) {
			throw new ConfigurationRuntimeException("Document is null!");
		}
		parse(doc, this.modules);
	}

	/**
	 * 
	 * @param doc
	 * @param moduleMap
	 * @throws ConfigurationRuntimeException
	 */
	private void parse(Document doc, Map<String, Module> moduleMap) throws ConfigurationRuntimeException {
		Element root = doc.getDocumentElement();
		try {			
			NodeList list = XmlUtil.findNodes(root, Configuration.PROPERTY);
			for (int i = 0; i < list.getLength(); i++) {
				Element propEle = (Element) list.item(i);
				
				String location = propEle.getAttribute(Configuration.PROPERTY_LOCATION);
				if (StringUtil.isNotNullAndBlank(location)) {
					try {
						location = ConfigurationHelper.getValueContainVars(location, _prop);
					} catch (Throwable ignore) {						
					}
					
					try {
						if (StringUtil.isNotNullAndBlank(filePath)) {
							_prop.load(UrlUtil.getURL(new File(new File(filePath).getParentFile(), location).getAbsolutePath(), Configuration.class.getClassLoader()).openStream());
						}	
					} catch (Throwable t) {							
						try {
							_prop.load(UrlUtil.getURL(location, Configuration.class.getClassLoader()).openStream());						
						} catch (Throwable ignore) {							
						}
					}					
				}
				
				String propName = propEle.getAttribute(Configuration.PROPERTY_NAME);
				if (StringUtil.isNotNullAndBlank(propName)) {
					_prop.setProperty(propName, propEle.getAttribute(Configuration.PROPERTY_VALUE));
				}				
			}
		} catch (Exception e) {
			throw new ConfigurationRuntimeException("Document parse error!", e);
		}
		
		try {
			NodeList list = XmlUtil.findNodes(root, Configuration.MODULE);
			for (int i = 0; i < list.getLength(); i++) {
				Element moduleEle = (Element) list.item(i);
				Module module = new Module(moduleEle, _prop);
				moduleMap.put(module.getName(), module);
			}
		} catch (Exception e) {
			throw new ConfigurationRuntimeException("Document parse error!", e);
		}
	}
	
	/**
	 * 
	 * @param configFile
	 * @return
	 * @throws ConfigurationRuntimeException
	 */
	public static Configuration initConfiguration(File configFile)
			throws ConfigurationRuntimeException {
		return initConfiguration(configFile == null ? null : configFile.getAbsolutePath());
	}
	
	/**
	 * 
	 * @param configFileUrl
	 * @return
	 * @throws ConfigurationRuntimeException
	 */
	public static Configuration initConfiguration(URL configFileUrl)
			throws ConfigurationRuntimeException {
		return initConfiguration(configFileUrl == null ? null : configFileUrl.getFile());
	}

	/**
	 * 
	 * @param confFilePath
	 * @return
	 * @throws ConfigurationRuntimeException
	 */
	public static Configuration initConfiguration(String confFilePath)
			throws ConfigurationRuntimeException {
		return new Configuration(confFilePath);
	}

	/**
	 * 
	 * @param confStream
	 * @return
	 * @throws ConfigurationRuntimeException
	 */
	public static Configuration initConfiguration(InputStream confStream)
			throws ConfigurationRuntimeException {
		return new Configuration(confStream);
	}

	/**
	 * 
	 * @param confDoc
	 * @return
	 * @throws ConfigurationRuntimeException
	 */
	public static Configuration initConfiguration(Document confDoc)
			throws ConfigurationRuntimeException {
		return new Configuration(confDoc);
	}
	
	/**
	 * 
	 * @param filePath
	 * @param create
	 * @return
	 * @throws ConfigurationRuntimeException
	 */
	public static Configuration initConfiguration(String filePath,
			boolean create) throws ConfigurationRuntimeException {
		if (filePath == null) {
			throw new ConfigurationRuntimeException("filepath is null!");
		}
		if (new File(filePath).exists()) {
			return initConfiguration(filePath);
		} else {
			if (create) {
				return createConfiguration(filePath, DEFAULT_MODULE_NAME);
			} else	{
				throw new ConfigurationRuntimeException("[filePath={0}] is not exist!", new String[]{filePath});
			}
		}
	}
	
	/**
	 * <pre>
	 * &lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt;
	 * &lt;EOSConfig&gt;
	 *     &lt;module name="global"&gt;
	 *     &lt;/module&gt;
	 * &lt;/EOSConfig&gt;
	 * </pre>
	 *
	 * @param confFilePath
	 * @param moduleName
	 * @return
	 * @throws ConfigurationRuntimeException
	 */
	@SuppressWarnings("deprecation")
	public static Configuration createConfiguration(String confFilePath,
			String moduleName) throws ConfigurationRuntimeException {

		Document document = XmlUtil.getNewDocument();
		Element root = document.createElement(EOSConfig);
		document.appendChild(root);

		Element module = XmlUtil.createField(root, MODULE);
		XmlUtil.setAttrValue(module, MODULE_NAME, moduleName);

		try {
			XmlUtil.saveDocument(document, confFilePath, true);
		} catch (Exception e) {
			throw new ConfigurationRuntimeException("New document cannot save to [path={0}].", new String[]{confFilePath}, e);
		}

		return new Configuration(confFilePath);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getConfigFilePath() {
		return filePath;
	}

	/**
	 * 
	 * @return
	 */
	public String[] moduleNames() {

		return this.modules.keySet().toArray(new String[0]);
	}
	
	/**
	 * 
	 * @param config
	 * @return
	 */
	public Configuration mergeConfiguration(Configuration config) {
		for(Module module : config.modules.values()) {
			mergeModule(module);
		}
		return this;
	}

	/**
	 * 
	 * @param moduleName
	 * @return
	 */
	public Module getModule(String moduleName) {
		return (Module) this.modules.get(moduleName);
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Module> getModules() {
		return modules;
	}

	/**
	 * 
	 * @param moduleName
	 * @return
	 */
	public Module addModule(String moduleName) {
		if (this.getModule(moduleName) != null) {
			return null;
		}
		Module newModule = new Module(moduleName);
		this.modules.put(moduleName, newModule);
		return newModule;
	}

	/**
	 * 
	 * @param module
	 * @return
	 */
	public Module addModule(Module module) {
		String moduleName = module.getName();
		Module moduleRet = this.getModule(moduleName);
		if (moduleRet != null) {
			return null;
		} else {
			this.modules.put(moduleName, module);
			module._prop = _prop;
			return module;
		}
	}
	
	/**
	 * 
	 * @param module
	 * @return
	 */
	public Module mergeModule(Module module) {
		String moduleName = module.getName();
		Module moduleRet = this.getModule(moduleName);
		if (moduleRet == null) {
			this.modules.put(moduleName, module);
			module._prop = _prop;
		} else {
			for (Group group : module.groups.values()) {
				moduleRet.mergeGroup(group);
			}
			this.modules.put(moduleName, moduleRet);
		}
		return this.getModule(moduleName);
	}

	/**
	 * 
	 * @param module
	 * @return
	 */
	public Module setModule(Module module) {
		if (module == null) {
			return null;
		}
		Module bef = this.modules.get(module.getName());
		this.deleteModule(module.getName());
		this.addModule(module);
		module._prop = _prop;
		return bef;
	}

	/**
	 * 
	 * @param moduleName
	 * @return
	 */
	public Module deleteModule(String moduleName) {
		Module module = this.getModule(moduleName);
		if (module == null) {
			return null;
		}
		this.modules.remove(moduleName);
		return module;
	}

	/**
	 * 
	 * @param moduleName
	 * @return
	 */
	public String[] groupNames(String moduleName) {
		Module module = this.getModule(moduleName);
		if (module == null) {
			return new String[0];
		} else {
			return module.groupNames();
		}
	}

	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @return
	 */
	public Group getGroup(String moduleName, String groupName) {
		Module module = this.getModule(moduleName);
		if (module == null)
			return null;
		return module.getGroup(groupName);
	}

	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @return
	 */
	public Group addGroup(String moduleName, String groupName) {
		Module module = this.getModule(moduleName);
		if (module == null) {
			module = this.addModule(moduleName);
		}
		return module.addGroup(groupName);
	}

	/**
	 * 
	 * @param moduleName
	 * @param group
	 * @return
	 */
	public Group addGroup(String moduleName, Group group) {
		Module module = this.getModule(moduleName);
		if (module == null) {
			module = this.addModule(moduleName);
		}
		return module.addGroup(group);
	}
	
	/**
	 * 
	 * @param moduleName
	 * @param group
	 * @return
	 */
	public Group mergeGroup(String moduleName, Group group) {
		Module module = this.getModule(moduleName);
		if (module == null) {
			module = this.addModule(moduleName);
		}
		return module.mergeGroup(group);
	}

	/**
	 * 
	 * @param moduleName
	 * @param group
	 * @return
	 */
	public Group setGroup(String moduleName, Group group) {
		Module module = this.getModule(moduleName);
		if (module == null) {
			module = this.addModule(moduleName);
		}		
		return module.setGroup(group);
	}

	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @return
	 */
	public Group deleteGroup(String moduleName, String groupName){
		Module module = this.getModule(moduleName);
		if (module == null) {
			return null;
		}
		return module.deleteGroup(groupName);
	}

	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @return
	 */
	public String[] valueNames(String moduleName, String groupName) {
		Group group = this.getGroup(moduleName, groupName);
		if (group == null) {
			return new String[0];
		} else {
			return group.valueNames();
		}
	}

	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @param keyName
	 * @return
	 */
	public String getConfigValue(String moduleName, String groupName,
			String keyName) {
		Module module = (Module) this.modules.get(moduleName);
		if (module == null)
			return null;

		return module.getConfigValue(groupName, keyName);
	}

	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @param key
	 * @param value
	 * @return
	 */
	public Value addValue(String moduleName, String groupName, String key, String value) {
		return addValue(moduleName, groupName, new Value(key, value));
	}

	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @param value
	 * @return
	 */
	public Value mergeValue(String moduleName, String groupName, Value value) {
		Module module = this.getModule(moduleName);
		if (module == null) {
			module = this.addModule(moduleName);
		}
		return module.mergeValue(groupName, value);
	}
	
	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @param value
	 * @return
	 */
	public Value addValue(String moduleName, String groupName, Value value) {
		Module module = this.getModule(moduleName);
		if (module == null) {
			module = this.addModule(moduleName);
		}
		return module.addValue(groupName, value);
	}

	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @param key
	 * @param value
	 * @return
	 */
	public Value setValue(String moduleName, String groupName, String key, String value) {
		return setValue(moduleName, groupName, new Value(key, value));
	}

	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @param value
	 * @return
	 */
	public Value setValue(String moduleName, String groupName, Value value) {
		Module module = this.getModule(moduleName);
		if (module == null) {
			module = this.addModule(moduleName);
		}
		return module.setValue(groupName, value);
	}

	/**
	 * 
	 * @param moduleName
	 * @param groupName
	 * @param key
	 * @return
	 */
	public Value deleteValue(String moduleName, String groupName, String key){
		Module module = this.getModule(moduleName);
		if (module == null) {
			return null;
		}
		return module.deleteValue(groupName, key);
	}

	/**
	 * 
	 * @throws ConfigurationRuntimeException
	 */
	public void reload() throws ConfigurationRuntimeException {
		if (this.filePath == null) {
			throw new ConfigurationRuntimeException("filepath is null");
		}
		document = XmlUtil.parseFile(this.filePath);
		if (isModifyAndUpdateDocument(this, false)) {
			modules.clear();
			parse(document, modules);
		}
	}

	/**
	 * 
	 * @throws ConfigurationRuntimeException
	 */
	public void save() throws ConfigurationRuntimeException {
		if (this.document == null) {
			return;
		}
		if (this.filePath == null) {
			throw new ConfigurationRuntimeException("filepath is null!");
		}
		try {
			if (isModifyAndUpdateDocument(this, true)) {
				XmlUtil.saveDocument(document, filePath, true);
			}			
		} catch (Exception e) {
			throw new ConfigurationRuntimeException("the document cannot save to [path={0}]!", new String[]{filePath}, e);
		}
	}

	/**
	 * 
	 * @param filePath
	 * @throws ConfigurationRuntimeException
	 */
	public void saveAs(String filePath) throws ConfigurationRuntimeException {
		if (filePath == null) {
			throw new ConfigurationRuntimeException("the filepath is null!");
		}
		if (this.document == null) {
			throw new ConfigurationRuntimeException("the document is null!");
		}
		try {
			isModifyAndUpdateDocument(this, true);
			XmlUtil.saveDocument(document, filePath, true);
		} catch (Exception e) {
			throw new ConfigurationRuntimeException("the document cannot save to [path={0}]!", new String[]{filePath}, e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Configuration clone() {
		Configuration config = null;
		try {
			config = (Configuration)super.clone();
			Document _oldDoc = config.document;
			Document newDoc = XmlUtil.newDocument();
			Node root = newDoc.importNode(_oldDoc.getDocumentElement(), true);
			newDoc.appendChild(root);
			config.document = newDoc;
			//	config.document = (Document)document.cloneNode(true);
			config.modules = new LinkedHashMap<String, Module>();
			for (Entry<String, Module> entry : modules.entrySet()) {
				config.modules.put(entry.getKey(), entry.getValue().clone());
			}
		} catch (CloneNotSupportedException e) {
		}
		return config;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return XmlUtil.node2String(toDocument(), true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if ((o == null) || !(o instanceof Configuration)) {
			return false;
		}
		Configuration t = (Configuration) o;
		if (modules.size() != t.modules.size()) {
			return false;
		}
		for (String key : t.modules.keySet()) {
			Module module1 = t.modules.get(key);
			Module module2 = modules.get(key);
			if (module1 == null) {
				if (module2 == null) {
					continue;
				} else {
					return false;
				}
			}
			if (!module1.equals(module2)) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + (filePath == null ? 0 : filePath.hashCode());
		result = 37 * result + modules.size();
		for (Module module : modules.values()) {
			result = 37 * result + (module == null ? 0 : module.hashCode());
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param s
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		
		s.defaultReadObject();
		
		if (filePath != null) {
			document = XmlUtil.parseFile(filePath);			
		}
	}
	
	/**
	 * 
	 * @param config
	 * @param updateFlag
	 * @return
	 */
	private static boolean isModifyAndUpdateDocument(Configuration config, boolean updateFlag) {
		
		Configuration oldConfig = null;
		if (config.filePath == null) {
			oldConfig = Configuration.initConfiguration(config.document);
		} else {
			oldConfig = Configuration.initConfiguration(config.filePath);
		}
		
		boolean modifyFlag = false;
		if (!config.equals(oldConfig)) {
			modifyFlag = true;
			if (updateFlag) {
				Element root = config.document.getDocumentElement();
				Map<String, Module> oldModuleMap = oldConfig.modules;

				Map<String, Module> newModuleMap = config.clone().getModules();

				for (String moduleName : oldModuleMap.keySet().toArray(new String[0])) {					
					Module newModule = newModuleMap.get(moduleName);
					if (newModule == null) {
						ConfigurationHelper.removeChild(root, Configuration.MODULE, Configuration.MODULE_NAME, moduleName);
						oldModuleMap.remove(moduleName);
					} else {
						Module oldModule = oldModuleMap.get(moduleName);
						for (String groupName : oldModule.groupNames()) {							
							Group newGroup = newModule.getGroup(groupName);
							if (newGroup == null) {
								ConfigurationHelper.removeChild(getElement(root, moduleName), getElement(root, moduleName, groupName));
								oldModule.deleteGroup(groupName);
							} else {
								Group oldGroup = oldModule.getGroup(groupName);
								for (String key : oldGroup.valueNames()) {					
									String value = oldGroup.getConfigValue(key);
									Value newValue = newGroup.getValue(key);
									if (newValue == null) {
										ConfigurationHelper.removeChild(getElement(root, moduleName, groupName), getElement(root, moduleName, groupName, key));
										oldGroup.deleteValue(key);
									} else {
										if (!StringUtil.equal(newValue.getValue(), value)) {
											Element valueElem = getElement(root, moduleName, groupName, key);											
											XmlUtil.removeAllChildNode(valueElem);
											XmlUtil.setNodeValue(valueElem, newValue.getValue());
										}
									}
									newGroup.deleteValue(key);
								}
								
								for (Entry<String, Value> addValueEntry : newGroup.getValues().entrySet()) {
									Value value = addValueEntry.getValue();
									Element valueElem = ConfigurationHelper.appendChild(getElement(root, moduleName, groupName), Configuration.VALUE, Configuration.VALUE_KEY, value.getName());
									XmlUtil.setNodeValue(valueElem, value.getValue());
								}
							}
							
							newModule.deleteGroup(groupName);					
						}
						
						for (Entry<String, Group> addGroupEntry : newModule.getGroups().entrySet()) {
							String groupName = addGroupEntry.getKey();
							Element groupElem = ConfigurationHelper.appendChild(getElement(root, moduleName), Configuration.GROUP, Configuration.GROUP_NAME, groupName);
							for (Value value : addGroupEntry.getValue().getValues().values()) {
								Element valueElem = ConfigurationHelper.appendChild(groupElem, Configuration.VALUE, Configuration.VALUE_KEY, value.getName());
								XmlUtil.setNodeValue(valueElem, value.getValue());
							}
						}
					}
					newModuleMap.remove(moduleName);
				}
				
				for (Entry<String, Module> addModuleEntry : newModuleMap.entrySet()) {
					String moduleName = addModuleEntry.getKey();
					Element moduleElem = ConfigurationHelper.appendChild(root, Configuration.MODULE, Configuration.MODULE_NAME, moduleName);
					for (Group group : addModuleEntry.getValue().getGroups().values()) {
						Element groupElem = ConfigurationHelper.appendChild(moduleElem, Configuration.GROUP, Configuration.GROUP_NAME, group.getName());
						for (Value value : group.getValues().values()) {
							Element valueElem = ConfigurationHelper.appendChild(groupElem, Configuration.VALUE, Configuration.VALUE_KEY, value.getName());
							XmlUtil.setNodeValue(valueElem, value.getValue());
						}
					}
				}
			}
		}
		return modifyFlag;
	}
	
	/**
	 * 
	 * @param root
	 * @param moduleName
	 * @return
	 */
	private static Element getElement(Element root, String moduleName) {
		return ConfigurationHelper.getChild(root, Configuration.MODULE, Configuration.MODULE_NAME, moduleName);
	}
	
	/**
	 * 
	 * @param root
	 * @param moduleName
	 * @param groupName
	 * @return
	 */
	private static Element getElement(Element root, String moduleName, String groupName) {
		return ConfigurationHelper.getChild(getElement(root, moduleName), Configuration.GROUP, Configuration.GROUP_NAME, groupName);
	}
	
	/**
	 * 
	 * @param root
	 * @param moduleName
	 * @param groupName
	 * @param valueName
	 * @return
	 */
	private static Element getElement(Element root, String moduleName, String groupName, String valueName) {
		return ConfigurationHelper.getChild(getElement(root, moduleName, groupName), Configuration.VALUE, Configuration.VALUE_KEY, valueName);
	}

	/**
	 * 
	 * @return
	 */
	public Document toDocument() {
		Configuration clone = this.clone();
		isModifyAndUpdateDocument(clone, true);
		return clone.document;
	}

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class Module implements Serializable, Cloneable {

		/**
		 * <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = -2504629936876873833L;

		private String name;
		
		private Properties _prop;

		private Map<String, Group> groups = new LinkedHashMap<String, Group>();

		/**
		 * 
		 */
		public Module() {
			this("");
		}

		/**
		 * 
		 * @param moduleName
		 */
		public Module(String moduleName) {
			name = moduleName;
			_prop = new Properties();
		}

		/**
		 * 
		 * @param moduleElement
		 * @throws ConfigurationRuntimeException
		 */
		public Module(Element moduleElement) throws ConfigurationRuntimeException {
			this(moduleElement, null);
		}

		/**
		 * 
		 * @param moduleElement
		 * @param prop
		 * @throws ConfigurationRuntimeException
		 */
		public Module(Element moduleElement, Properties prop) throws ConfigurationRuntimeException {
			if (prop == null) {
				_prop = new Properties();
			} else {
				_prop = prop;
			}
			this.parse(moduleElement);			
		}
		
		/**
		 * 
		 * @param moduleElement
		 * @throws ConfigurationRuntimeException
		 */
		private void parse(Element moduleElement) throws ConfigurationRuntimeException {
			this.name = moduleElement.getAttribute(Configuration.MODULE_NAME);
			try {
				NodeList nodeList = XmlUtil.findNodes(moduleElement, Configuration.GROUP);
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element groupEle = (Element) nodeList.item(i);
					Group group = new Group(groupEle, _prop);
					this.groups.put(group.name, group);
				}
			} catch (Exception e) {
				throw new ConfigurationRuntimeException("the element parse error!", e);
			}
		}

		/**
		 * 
		 * @return
		 */
		public String getName() {
			return name;
		}

		/**
		 * 
		 * @param moduleName
		 */
		public void setName(String moduleName) {
			name = moduleName;
		}
		
		/**
		 * 
		 * @param module
		 * @return
		 */
		public Module mergeModule(Module module) {
			for (Group group : module.groups.values()) {
				mergeGroup(group);
			}
			return this;
		}

		/**
		 * 
		 * @param groupName
		 * @return
		 */
		public Group addGroup(String groupName) {
			Group group = this.getGroup(groupName);
			if (group != null) {
				return null;
			}
			group = new Group(groupName);
			this.groups.put(groupName, group);

			return group;
		}

		/**
		 * 
		 * @param group
		 * @return
		 */
		public Group addGroup(Group group) {
			String groupName = group.getName();
			Group groupRet = this.getGroup(groupName);
			if (groupRet != null) {
				return null;
			} else {
				this.groups.put(groupName, group);
				group._prop = _prop;
				return group;
			}
		}
		
		/**
		 * 
		 * @param group
		 * @return
		 */
		public Group mergeGroup(Group group) {
			String groupName = group.getName();
			Group groupRet = this.getGroup(groupName);
			if (groupRet == null) {
				this.groups.put(groupName, group);
				group._prop = _prop;
			} else {
				for (Value value : group.values.values()) {
					groupRet.mergeValue(value);
				}
				this.groups.put(groupName, groupRet);
			}
			return this.getGroup(groupName);
		}

		/**
		 * 
		 * @param group
		 * @return
		 */
		public Group setGroup(Group group) {
			if (group == null) {
				return null;
			}
			
			Group bef = this.groups.get(group.getName());
			deleteGroup(group.getName());
			addGroup(group);
			group._prop = _prop;
			return bef;
		}

		/**
		 * 
		 * @param groupName
		 * @return
		 */
		public Group deleteGroup(String groupName) {
			Group group = this.getGroup(groupName);
			if (group == null) {
				return null;
			}
			this.groups.remove(groupName);
			return group;
		}

		/**
		 * 
		 * @param groupName
		 * @return
		 */
		public Group getGroup(String groupName) {
			return this.groups.get(groupName);
		}

		/**
		 * 
		 * @return
		 */
		public String[] groupNames() {
			return this.groups.keySet().toArray(new String[0]);
		}

		/**
		 * 
		 * @return
		 */
		public Map<String, Group> getGroups() {
			return groups;
		}

		/**
		 * 
		 * @param groupName
		 * @return
		 */
		public String[] valueNames(String groupName) {
			Group group = this.groups.get(groupName);
			if (group == null) {
				return new String[0];
			} else {
				return group.valueNames();
			}
		}

		/**
		 * 
		 * @param groupName
		 * @param key
		 * @return
		 */
		public String getConfigValue(String groupName, String key) {
			Group group = this.groups.get(groupName);
			if (group == null)
				return null;

			return group.getConfigValue(key);
		}

		/**
		 * 
		 * @param groupName
		 * @param key
		 * @param value
		 * @return
		 */
		public Value addValue(String groupName, String key, String value) {
			return addValue(groupName, new Value(key, value));
		}

		/**
		 * 
		 * @param groupName
		 * @param value
		 * @return
		 */
		public Value addValue(String groupName, Value value) {
			Group group = this.groups.get(groupName);
			if (group == null) {
				group = addGroup(groupName);
			}
			return group.addValue(value);
		}
		
		/**
		 * 
		 * @param groupName
		 * @param value
		 * @return
		 */
		public Value mergeValue(String groupName, Value value) {
			Group group = this.groups.get(groupName);
			if (group == null) {
				group = addGroup(groupName);
			}
			return group.mergeValue(value);
		}

		/**
		 * 
		 * @param groupName
		 * @param key
		 * @param value
		 * @return
		 */
		public Value setValue(String groupName, String key, String value) {
			return setValue(groupName, new Value(key, value));
		}

		/**
		 * 
		 * @param groupName
		 * @param value
		 * @return
		 */
		public Value setValue(String groupName, Value value) {
			Group group = this.groups.get(groupName);
			if (group == null) {
				group = addGroup(groupName);
			}
			return group.setValue(value);
		}

		/**
		 * 
		 * @param groupName
		 * @param key
		 * @return
		 */
		public Value deleteValue(String groupName, String key){
			Group group = this.groups.get(groupName);
			if (group == null) {
				return null;
			}
			return group.deleteValue(key);
		}

		/**
		 * 
		 * @return
		 */
		@SuppressWarnings("deprecation")
		public Element toElement() {
			Document document = XmlUtil.getNewDocument();
			Element moduleElem = document.createElement(Configuration.MODULE);
			XmlUtil.setAttrValue(moduleElem, Configuration.MODULE_NAME, name);

			for (Group group : this.getGroups().values()) {
				Element groupElem = XmlUtil.createField(moduleElem, GROUP);
				XmlUtil.setAttrValue(groupElem, GROUP_NAME, group.getName());
				for (Value value : group.getValues().values()) {
					Element valueElem = XmlUtil.createField(groupElem, VALUE);
					XmlUtil.setAttrValue(valueElem, VALUE_KEY, value.getName());
					XmlUtil.setNodeValue(valueElem, value.getValue());
				}
			}

			return moduleElem;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#clone()
		 */
		public Module clone() {
			Module module = null;
			try {
				module = (Module)super.clone();
				module.groups = new LinkedHashMap<String, Group>();
				for (Entry<String, Group> entry : groups.entrySet()) {
					module.groups.put(entry.getKey(), entry.getValue().clone());
				}
			} catch (CloneNotSupportedException e) {
			}			
			return module;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return XmlUtil.node2String(toElement(), true);
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			if ((o == null) || !(o instanceof Module)) {
				return false;
			}
			Module t = (Module) o;
			if (!StringUtil.equal(name, t.name)) {
				return false;
			}
			if (groups.size() != t.groups.size()) {
				return false;
			}
			for (String key : t.groups.keySet()) {
				Group group1 = t.groups.get(key);
				Group group2 = groups.get(key);
				if (group1 == null) {
					if (group2 == null) {
						continue;
					} else {
						return false;
					}
				}
				if (!group1.equals(group2)) {
					return false;
				}
			}
			return true;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			int result = 17;
			result = 37 * result + (name == null ? 0 : name.hashCode());
			result = 37 * result + groups.size();
			for (Group group : groups.values()) {
				result = 37 * result + (group == null ? 0 : group.hashCode());
			}
			
			return result;
		}
	}

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class Group implements Serializable, Cloneable {

		/**
		 * <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = 8376648763419109114L;

		private String name;
		
		private Properties _prop;

		private Map<String, Value> values = new LinkedHashMap<String, Value>();

		/**
		 * 
		 */
		public Group() {
			this("");
		}

		/**
		 * 
		 * @param groupName
		 */
		public Group(String groupName) {
			name = groupName;
			_prop = new Properties();
		}

		/**
		 * 
		 * @param groupElement
		 * @throws ConfigurationRuntimeException
		 */
		public Group(Element groupElement) throws ConfigurationRuntimeException {
			this(groupElement, null);
		}
		
		/**
		 * 
		 * @param groupElement
		 * @param prop
		 * @throws ConfigurationRuntimeException
		 */
		public Group(Element groupElement, Properties prop) throws ConfigurationRuntimeException {
			if (prop == null) {
				_prop = new Properties();
			} else {
				_prop = prop;
			}
			this.parse(groupElement);
		}

		/**
		 * 
		 * @param groupElement
		 * @throws ConfigurationRuntimeException
		 */
		private void parse(Element groupElement) throws ConfigurationRuntimeException {
			this.name = groupElement.getAttribute(Configuration.GROUP_NAME);
			try {
				NodeList nodeList = XmlUtil.findNodes(groupElement,
						Configuration.VALUE);
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element valueEle = (Element) nodeList.item(i);
					Value value = new Value(valueEle, _prop);

					this.values.put(value.name, value);
				}
			} catch (Exception e) {
				throw new ConfigurationRuntimeException("the element parse error!", e);
			}
		}

		/**
		 * 
		 * @return
		 */
		public String getName() {
			return name;
		}

		/**
		 * 
		 * @param groupName
		 */
		public void setName(String groupName) {
			name = groupName;
		}
		
		/**
		 * 
		 * @param group
		 * @return
		 */
		public Group mergeGroup(Group group) {
			for (Value value : group.values.values()) {
				mergeValue(value);
			}
			return this;
		}

		/**
		 * 
		 * @param key
		 * @param value
		 * @return
		 */
		public Value addValue(String key, String value) {

			return addValue(new Value(key, value));
		}

		/**
		 * 
		 * @param value
		 * @return
		 */
		public Value addValue(Value value) {
			Value valueInstance = this.getValue(value.getName());
			if (valueInstance != null) {
				return null;
			} else {
				this.values.put(value.getName(), value);
				value._prop = _prop;
				return value;
			}
		}

		/**
		 * 
		 * @param key
		 * @param value
		 * @return
		 */
		public Value setValue(String key, String value) {
			return setValue(new Value(key, value));
		}
		
		/**
		 * 
		 * @param value
		 * @return
		 */
		public Value mergeValue(Value value) {
			value._prop = _prop;
			this.values.put(value.getName(), value);
			return getValue(value.getName());
		}

		/**
		 * 
		 * @param value
		 * @return
		 */
		public Value setValue(Value value) {
			if (value == null) {
				return null;
			}
			Value bef = this.values.get(value.getName());
			deleteValue(value.getName());
			addValue(value);
			value._prop = _prop;
			return bef;
		}

		/**
		 * 
		 * @param key
		 * @return
		 */
		public Value deleteValue(String key) {
			Value value = this.getValue(key);
			if (value == null) {
				return null;
			} else {
				this.values.remove(key);
			}
			return value;
		}

		/**
		 * 
		 * @return
		 */
		public Map<String, Value> getValues() {
			return values;
		}

		/**
		 * 
		 * @param key
		 * @return
		 */
		public Value getValue(String key) {
			return (Value) this.values.get(key);
		}

		/**
		 * 
		 * @param keyName
		 * @return
		 */
		public String getConfigValue(String keyName) {
			Value value = this.values.get(keyName);
			if (value == null) {
				return null;
			} else {
				return value.getValue();
			}
		}

		/**
		 * 
		 * @return
		 */
		public String[] valueNames() {

			return this.values.keySet().toArray(new String[0]);
		}

		/**
		 * 
		 * @return
		 */
		public Map<String, String> toMapValues() {
			Collection<Value> col = this.values.values();
			Map<String, String> mapvalues = new LinkedHashMap<String, String>();
			for (Value v : col) {
				mapvalues.put(v.getName(), v.getValue());
			}
			return mapvalues;
		}

		/**
		 * 
		 * @return
		 */
		public Properties toProperties() {
			Collection<Value> col = this.values.values();
			Properties props = new Properties();
			for (Value v : col) {
				props.setProperty(v.getName(), v.getValue());
			}
			return props;
		}

		/**
		 * 
		 * @return
		 */
		@SuppressWarnings("deprecation")
		public Element toElement() {
			Document document = XmlUtil.getNewDocument();
			Element groupElem = document.createElement(Configuration.GROUP);
			XmlUtil.setAttrValue(groupElem, Configuration.GROUP_NAME, name);
			for (Value value : this.getValues().values()) {
				Element valueElem = XmlUtil.createField(groupElem, VALUE);
				XmlUtil.setAttrValue(valueElem, VALUE_KEY, value.getName());
				XmlUtil.setNodeValue(valueElem, value.getValue());
			}
			return groupElem;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#clone()
		 */
		public Group clone() {
			Group group = null;
			try {
				group = (Group)super.clone();
				group.values = new LinkedHashMap<String, Value>();
				for (Entry<String, Value> entry : values.entrySet()) {
					group.values.put(entry.getKey(), entry.getValue().clone());
				}
			} catch (CloneNotSupportedException e) {
			}
			return group;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return XmlUtil.node2String(toElement(), true);
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			if ((o == null) || !(o instanceof Group)) {
				return false;
			}
			Group t = (Group) o;
			if (!StringUtil.equal(name, t.name)) {
				return false;
			}
			if (values.size() != t.values.size()) {
				return false;
			}
			for (String key : t.values.keySet()) {
				Value value1 = t.values.get(key);
				Value value2 = values.get(key);
				if (value1 == null) {
					if (value2 == null) {
						continue;
					} else {
						return false;
					}
				}
				if (!value1.equals(value2)) {
					return false;
				}
			}
			return true;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			int result = 17;
			result = 37 * result + (name == null ? 0 : name.hashCode());
			result = 37 * result + values.size();
			for (Value value : values.values()) {
				result = 37 * result + (value == null ? 0 : value.hashCode());
			}
			
			return result;
		}
	}

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class Value implements Serializable, Cloneable {

		/**
		 * <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = 4215017621412986399L;

		private String name;

		private String value;
		
		private Properties _prop;
		
		/**
		 * 
		 */
		public Value() {
			this("");
		}

		/**
		 * 
		 * @param valueName
		 */
		public Value(String valueName) {
			this(valueName, "");
		}

		/**
		 * 
		 * @param keyName
		 * @param keyValue
		 */
		public Value(String keyName, String keyValue) {
			name = keyName;
			value = keyValue;
			_prop = new Properties();
		}

		/**
		 * 
		 * @param valueElement
		 */
		public Value(Element valueElement) {
			this(valueElement, null);
		}
		
		/**
		 * 
		 * @param valueElement
		 * @param prop
		 */
		public Value(Element valueElement, Properties prop) {
			if (prop == null) {
				_prop = new Properties();
			} else {
				_prop = prop;
			}
			this.parse(valueElement);
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#clone()
		 */
		public Value clone() {
			Value result = null;
			try {
				result = (Value)super.clone();
			} catch (CloneNotSupportedException e) {
			}
			return result;
		}

		/**
		 * 
		 * @param valueElement
		 */
		private void parse(Element valueElement) {
			this.name = valueElement.getAttribute(Configuration.VALUE_KEY);
			if (ConfigurationHelper.isSimpleNode(valueElement)) {
				this.value = XmlUtil.getNodeValue(valueElement);
			} else {
				String complexValue = XmlUtil.node2String(valueElement, false, false);	
				this.value = complexValue.substring(complexValue.indexOf('>') + 1, complexValue.lastIndexOf("</"));
			}
			if (this.value == null) {
				this.value = "";
			} else {
				this.value = this.value.trim();
			}
		}

		/**
		 * 
		 * @return
		 */
		public String getName() {
			return name;
		}

		public void setName(String valueName) {
			name = valueName;
		}

		/**
		 * 
		 * @return
		 */
		public String getValue() {
			try {
				return ConfigurationHelper.getValueContainVars(value, _prop);
			} catch (Throwable t) {
				return value;
			}			
		}

		/**
		 * 
		 * @param value
		 */
		public void setValue(String value) {
			if (value == null) {
				value = "";
			}
			this.value = value;
		}

		/**
		 * 
		 * @return
		 */
		@SuppressWarnings("deprecation")
		public Element toElement() {
			Document document = XmlUtil.getNewDocument();
			Element valueElem = document.createElement(Configuration.VALUE);
			XmlUtil.setAttrValue(valueElem, Configuration.VALUE_KEY, name);
			XmlUtil.setNodeValue(valueElem, value);
			return valueElem;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return XmlUtil.node2String(toElement(), true);
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			if ((o == null) || !(o instanceof Value)) {
				return false;
			}
			Value t = (Value)o;
			if (!StringUtil.equal(name, t.name)) {
				return false;
			}
			if (!StringUtil.equal(value, t.value)) {
				return false;
			}
			return true;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			int result = 17;
			result = 37 * result + (name == null ? 0 : name.hashCode());
			result = 37 * result + (value == null ? 0 : value.hashCode());
			return result;
		}
	}
	
}
