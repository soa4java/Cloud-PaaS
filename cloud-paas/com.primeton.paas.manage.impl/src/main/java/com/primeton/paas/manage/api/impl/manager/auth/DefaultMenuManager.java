/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultAuthManageDao;
import com.primeton.paas.manage.api.impl.util.IAuthConstants;
import com.primeton.paas.manage.api.model.CapMenu;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultMenuManager {
	
	private ILogger log = ManageLoggerFactory.getLogger(DefaultMenuManager.class);
	
	private DefaultAuthManageDao dacbDao = DefaultAuthManageDao.getInstance();

	public static String CAP_MENU_ENTITY_ID_PROPERTY = "menuId";

	public static String CAP_MENU_ENTIT_Y_CODE_PROPERTY = "menuCode";

	public static String CAP_MENU_ENTITY_LEVEL_PROPERTY = "menuLevel";

	public static String CAP_MENU_ENTITY_NAME_PROPERTY = "menuName";

	public static String CAP_MENU_ENTITY_SEQ_PROPERTY = "menuSeq";

	public static String CAP_MENU_ENTITY_LINK_TYPE_PROPERTY = "linkType";

	public static String CAP_MENU_ENTITY_LINK_ACTION_PROPERTY = "linkAction";

	public static String CAP_MENU_ENTITY_ISLEAF_PROPERTY = "isleaf";

	public static String CAP_MENU_ENTITY_LINK_RES_PROPERTY = "linkRes";

	public DefaultMenuManager() {
	}

	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public CapMenu[] getAllCapMenu() throws DaoException {
		return queryMenuListByCriteria(null);
	}
	
	/**
	 * 
	 * @param menuId
	 * @return
	 */
	public CapMenu getCapMenuByMenuId(String menuId) {
		CapMenu menu = null;
		try {
			menu = dacbDao.getCapMenuByMenuId(menuId);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return menu;
	}
	
	/**
	 * 
	 * @param parentMenu
	 * @return
	 * @throws DaoException
	 */
	public CapMenu[] getChildMenuListByParentMenu(CapMenu parentMenu)
			throws DaoException {
		if (parentMenu != null) {
			String parentMenuId = parentMenu.getMenuId();
			return getChildMenuListByParentMenuId(parentMenuId);
		} else {
			return new CapMenu[0];
		}
	}

	/**
	 * 
	 * @param parentMenuId
	 * @return
	 */
	public CapMenu[] getChildMenuListByParentMenuId(String parentMenuId) {
		List<CapMenu> list = new ArrayList<CapMenu>();
		if (parentMenuId != null) {
			try {
				list = dacbDao.getCapMenuByParentMenuId(parentMenuId);
			} catch (DaoException e) {
				e.printStackTrace();
			}
		} else {
			return new CapMenu[0];
		}
		return list.toArray(new CapMenu[list.size()]);
	}

	/**
	 * 
	 * @param menu
	 * @return
	 * @throws DaoException
	 */
	public CapMenu[] getAllChildMenuByParentMenu(CapMenu menu)
			throws DaoException {
		CapMenu[] allMenus = getAllCapMenu();
		List<CapMenu> menus = new ArrayList<CapMenu>();
		String menuId = menu.getMenuId();
		for (CapMenu menut : allMenus) {
			String[] menutSeqs = menut.getMenuSeq().split("\\."); //$NON-NLS-1$
			if (menutSeqs.length >= 0) {
				List<String> menutSeqList = Arrays.asList(menutSeqs);
				if (menutSeqList.contains(menuId)) {
					menus.add(menut);
				}
			}
		}
		return menus.toArray(new CapMenu[menus.size()]);
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public CapMenu[] queryMenuListByCriteria(Map<String, String> criteria)
			throws DaoException {
		List<CapMenu> list = dacbDao.getCapMenuByCriteria(criteria);
		return list.toArray(new CapMenu[list.size()]);
	}
	
	/**
	 * 
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public CapMenu[] getFirstLevelMenu(IPageCond page) throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(CAP_MENU_ENTITY_LEVEL_PROPERTY, "1");
		List<CapMenu> list = new ArrayList<CapMenu>();
		if (page == null) {
			list = dacbDao.getCapMenuByCriteria(criteria);
		} else {
			if (page.getCount() <= 0) {
				page.setCount(dacbDao.getCapMenuByCriteria(criteria).size());
			}

			list = dacbDao.getCapMenuByCriteria(criteria, page);
			if (list == null || list.isEmpty()) {
				return new CapMenu[0];
			}
		}
		return list.toArray(new CapMenu[list.size()]);
	}
	
	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public CapMenu[] queryMenuListByCriteria(Map<String, String> criteria,
			IPageCond page) throws DaoException {
		if (criteria == null) {
			criteria = new HashMap<String, String>();
		}
		List<CapMenu> list = new ArrayList<CapMenu>();
		if (page == null) {
			list = dacbDao.getCapMenuByCriteria(criteria);
		} else {
			list = dacbDao.getCapMenuByCriteria(criteria, page);
			if (list == null || list.isEmpty()) {
				return new CapMenu[0];
			}
			if (page.getCount() <= 0) {
				page.setCount(list.size());
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	private String getCapMenuNextKey() throws DaoException {
		String maxKey = dacbDao.getCapMenuMaxPrimaryKey();
		String nextKey = "100"; //$NON-NLS-1$
		if (maxKey != null) {
			nextKey = String.valueOf(Long.parseLong(maxKey) + 1);
		}
		return nextKey;
	}
	
	/**
	 * 
	 * @param menuCode
	 * @return
	 * @throws DaoException
	 */
	public boolean isMenuCodeExist(String menuCode) throws DaoException {
		CapMenu menu = dacbDao.getCapMenuByMenuCode(menuCode);
		if (menu != null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param menu
	 * @return
	 */
	public boolean insertFirstLevelMenu(CapMenu menu) {
		try {
			if (menu == null) {
				return false;
			}
			String menuId = getCapMenuNextKey();
			menu.setMenuId(menuId);
			menu.setIsleaf("0"); //$NON-NLS-1$
			menu.setMenuLevel(1);
			if (menu.getCreateuser() == null) {
				menu.setCreateuser("sysadmin"); //$NON-NLS-1$
			}
			menu.setTenantId(IAuthConstants.DEFAULT_TENANT_ID);
			menu.setOpenmode("0"); //$NON-NLS-1$
			menu.setMenuSeq(menuId + "."); //$NON-NLS-1$

			insertMenuToDB(menu);
			return true;
		} catch (DaoException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @param menu
	 * @return
	 */
	public boolean insertMenu(CapMenu menu) {
		String tenantId = IAuthConstants.DEFAULT_TENANT_ID;
		try {
			menu.setIsleaf("1"); //$NON-NLS-1$
			// menu.setCreatetime(new Date());
			if (menu.getCreateuser() == null) {
				menu.setCreateuser("sysadmin"); //$NON-NLS-1$
			}
			menu.setTenantId(tenantId);
			menu.setOpenmode("0"); //$NON-NLS-1$
			String menuId = getCapMenuNextKey();
			menu.setMenuId(menuId);

			String parentMenuId = menu.getParentMenuId();
			if (parentMenuId == null) {
				menu.setMenuLevel(1);
				menu.setMenuSeq(menu.getMenuId() + "."); //$NON-NLS-1$
			} else {
				CapMenu parentMenu = getCapMenuByMenuId(parentMenuId);
				if (parentMenu != null) {
					menu.setMenuLevel(parentMenu.getMenuLevel() + 1);
					menu.setMenuSeq(parentMenu.getMenuSeq() + menu.getMenuId()
							+ "."); //$NON-NLS-1$
					if ("1".equals(parentMenu.getIsleaf())) { //$NON-NLS-1$
						parentMenu.setIsleaf("0"); //$NON-NLS-1$
						updateMenu(parentMenu);
					}
				} else {
					log.error("Insert menu [menuCode="
							+ menu.getMenuCode()
							+ "] failure, can not find the parentMenu by menuId [menuId = "
							+ parentMenuId + "].");
				}
			}
			insertMenuToDB(menu);
			return true;
		} catch (Throwable t) {
			log.error(
					"Insert menu [menuCode="
							+ menu.getMenuCode()
							+ "] failure, please do the operation again or contact the sysadmin.",
					t);
			return false;
		}
	}

	/**
	 * 
	 * @param menu
	 * @throws DaoException
	 */
	public void insertMenuToDB(CapMenu menu) throws DaoException {
		dacbDao.addCapMenu(menu);
	}
	
	/**
	 * 
	 * @param menu
	 * @return
	 * @throws DaoException
	 */
	public boolean isMenuUpdateLegal(CapMenu menu) throws DaoException {
		int menuLevel = menu.getMenuLevel();
		CapMenu parentMenu = getCapMenuByMenuId(String.valueOf(menu
				.getParentMenuId()));
		if (parentMenu != null) {
			int toParentMenuLevel = parentMenu.getMenuLevel();
			if (menuLevel >= toParentMenuLevel + 1) {
				return true;
			} else {
				String menuSeq = menu.getMenuSeq();
				String[] menuIds = menuSeq.split("\\."); //$NON-NLS-1$
				List<CapMenu> menus = new ArrayList<CapMenu>();
				for (String menuId : menuIds) {
					CapMenu menuTemp = getCapMenuByMenuId(menuId);
					menus.add(menuTemp);
				}
				int maxMenuLevel = 0;
				for (CapMenu tmpMenu : menus) {
					int tmpMenuLevel = tmpMenu.getMenuLevel();
					if (tmpMenuLevel > maxMenuLevel) {
						maxMenuLevel = tmpMenuLevel;
					}
				}
				if (maxMenuLevel - menuLevel + toParentMenuLevel >= 3) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param menuSeq
	 * @param moveLevel
	 * @param toLevel
	 * @return
	 * @throws DaoException
	 */
	public boolean isMenuMoveLegal(String menuSeq, int moveLevel, int toLevel)
			throws DaoException {
		String[] menuIds = menuSeq.split("\\."); //$NON-NLS-1$
		List<CapMenu> menus = new ArrayList<CapMenu>();
		for (String menuId : menuIds) {
			CapMenu menuTemp = getCapMenuByMenuId(menuId);
			menus.add(menuTemp);
		}
		int maxMenuLevel = 0;
		for (CapMenu tmpMenu : menus) {
			int tmpMenuLevel = tmpMenu.getMenuLevel();
			if (tmpMenuLevel > maxMenuLevel) {
				maxMenuLevel = tmpMenuLevel;
			}
		}
		if (maxMenuLevel - moveLevel + toLevel >= 3) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param menu
	 * @param oldParentMenuId
	 * @return
	 */
	public boolean updateMenu(CapMenu menu, long oldParentMenuId) {
		try {
			List<CapMenu> collectMenus = new ArrayList<CapMenu>();
			CapMenu[] siblingMenus = getChildMenuListByParentMenuId(String
					.valueOf(oldParentMenuId));
			if (siblingMenus != null && siblingMenus.length == 1) {
				CapMenu oldParentMenu = getCapMenuByMenuId(String
						.valueOf(oldParentMenuId));
				oldParentMenu.setIsleaf("1"); //$NON-NLS-1$
				if (menu.getParentMenuId() != oldParentMenu.getMenuId()) {
					collectMenus.add(oldParentMenu);
				}
			}
			String menuSeq = menu.getMenuSeq();
			String[] menuIds = menuSeq.split("\\."); //$NON-NLS-1$
			List<CapMenu> menus = new ArrayList<CapMenu>();
			for (String menuId : menuIds) {
				CapMenu menuTemp = getCapMenuByMenuId(menuId);
				menus.add(menuTemp);
			}
			CapMenu newParentMenu = getCapMenuByMenuId(String.valueOf(menu
					.getParentMenuId()));
			if (newParentMenu != null) {
				int newParentLevel = newParentMenu.getMenuLevel();
				String newParentSeq = newParentMenu.getMenuSeq();
				int oldLevel = menu.getMenuLevel();
				for (CapMenu tmpMenu : menus) {
					int oldTmpMenuLevel = tmpMenu.getMenuLevel();
					String oldTmpMenuSeq = tmpMenu.getMenuSeq();
					if (tmpMenu.getMenuId() == menu.getMenuId()) {
						// menu.setCapMenu(newParentMenu);
						menu.setParentMenuId(newParentMenu.getMenuId());
						menu.setMenuLevel(oldTmpMenuLevel - oldLevel
								+ newParentLevel + 1);
						menu.setMenuSeq(newParentSeq
								+ menu.getMenuId()
								+ "." //$NON-NLS-1$
								+ StringUtil.substringAfter(oldTmpMenuSeq,
										menuSeq));
						collectMenus.add(menu);
						continue;
					}
					tmpMenu.setMenuLevel(oldTmpMenuLevel - oldLevel
							+ newParentLevel + 1);
					tmpMenu.setMenuSeq(newParentSeq + menu.getMenuId() + "." //$NON-NLS-1$
							+ StringUtil.substringAfter(oldTmpMenuSeq, menuSeq));
					collectMenus.add(tmpMenu);
				}
				if ("1".equals(newParentMenu.getIsleaf())) { //$NON-NLS-1$
					newParentMenu.setIsleaf("0"); //$NON-NLS-1$
					collectMenus.add(newParentMenu);
				}
			} else {
				int oldLevel = menu.getMenuLevel();
				for (CapMenu tmpMenu : menus) {
					int oldTmpMenuLevel = tmpMenu.getMenuLevel();
					String oldTmpMenuSeq = tmpMenu.getMenuSeq();
					if (tmpMenu.getMenuId() == menu.getMenuId()) {
						// menu.setCapMenu(null);
						menu.setParentMenuId(null);
						collectMenus.add(menu);
						menu.setMenuLevel(oldTmpMenuLevel - oldLevel + 1);
						menu.setMenuSeq(menu.getMenuId()
								+ "." //$NON-NLS-1$
								+ StringUtil.substringAfter(oldTmpMenuSeq,
										menuSeq));
						continue;
					}
					tmpMenu.setMenuLevel(oldTmpMenuLevel - oldLevel + 1);
					tmpMenu.setMenuSeq(menu.getMenuId() + "." //$NON-NLS-1$
							+ StringUtil.substringAfter(oldTmpMenuSeq, menuSeq));

					collectMenus.add(tmpMenu);
				}
			}

			updateMenuBatch(collectMenus.toArray(new CapMenu[collectMenus
					.size()]));
			return true;
		} catch (Throwable t) {
			t.printStackTrace();
			log.error(
					"Update menu [menuId="
							+ menu.getMenuId()
							+ "] failure, please do the operation again or contact the sysadmin.",
					t);
			return false;
		}
	}

	/**
	 * 
	 * @param menus
	 * @throws DaoException
	 */
	public void updateMenuBatch(CapMenu[] menus) throws DaoException {
		if (null == menus || menus.length == 0) {
			return;
		}
		for (CapMenu menu : menus) {
			updateMenu(menu);
		}
	}
	
	/**
	 * 
	 * @param menu
	 * @throws DaoException
	 */
	public void updateMenu(CapMenu menu) throws DaoException {
		dacbDao.updateMenuByMenuId(menu);
	}
	
	/**
	 * 
	 * @param menus
	 * @throws DaoException
	 */
	public void deleteMenuBatchByMenuIds(CapMenu[] menus) throws DaoException {
		if (null == menus || menus.length <= 0) {
			return;
		}
		dacbDao.deleteMenuBatchByMenuIds(Arrays.asList(menus));
	}
	
	/**
	 * 
	 * @param menu
	 * @throws DaoException
	 */
	public void deleteMenu(CapMenu menu) throws DaoException {
		dacbDao.deleteMenuByMenuId(menu.getMenuId());
	}
	
	/**
	 * 
	 * @param menu
	 * @return
	 * @throws DaoException
	 */
	public boolean deleteFirstLevelMenu(CapMenu menu) throws DaoException {
		if (menu == null || menu.getParentMenuId() != null) {
			return false;
		}
		CapMenu[] submenus = getAllChildMenuByParentMenu(menu);
		deleteMenuBatchByMenuIds(submenus);
		return true;
	}
	
	/**
	 * 
	 * @param menuId
	 * @return
	 * @throws DaoException
	 */
	public boolean deleteCapMenuByMenuId(String menuId) throws DaoException {
		CapMenu menu = getCapMenuByMenuId(menuId);
		if (menu == null) {
			return false;
		}
		String parentMenuId = menu.getParentMenuId();
		CapMenu[] siblingMenus = this
				.getChildMenuListByParentMenuId(parentMenuId);
		if (siblingMenus != null && siblingMenus.length == 1) {
			CapMenu parentMenu = getCapMenuByMenuId(parentMenuId);
			parentMenu.setIsleaf("1"); //$NON-NLS-1$
			updateMenu(parentMenu);
		}
		CapMenu[] submenus = getAllChildMenuByParentMenu(menu);
		deleteMenuBatchByMenuIds(submenus);
		return true;
	}
	
	/**
	 * 
	 * @param fromMenu
	 * @param toMenu
	 * @param menu
	 * @return
	 */
	public boolean moveCapMenu(CapMenu fromMenu, CapMenu toMenu, CapMenu menu) {
		try {
			List<CapMenu> collectMenus = new ArrayList<CapMenu>();
			CapMenu[] oldSiblingMenus = this
					.getChildMenuListByParentMenuId(fromMenu.getMenuId());
			if (oldSiblingMenus != null && oldSiblingMenus.length == 1) {
				CapMenu oldParentMenu = getCapMenuByMenuId(fromMenu.getMenuId());
				oldParentMenu.setIsleaf("1"); //$NON-NLS-1$
				// CapMenu oldParentMenu = oldSiblingMenus[0].getCapMenu();
				// oldParentMenu.setIsleaf("1");
				collectMenus.add(oldParentMenu);
			}

			String menuSeq = menu.getMenuSeq();
			String[] menuIds = menuSeq.split("\\."); //$NON-NLS-1$
			List<CapMenu> menus = new ArrayList<CapMenu>();
			for (String menuIdt : menuIds) {
				CapMenu menuTemp = getCapMenuByMenuId(menuIdt);
				menus.add(menuTemp);
			}
			long toParentMenuId = 0;
			if (toMenu != null) {
				toParentMenuId = Long.parseLong(toMenu.getMenuId());
			}
			CapMenu newParentMenu = this.getCapMenuByMenuId(String
					.valueOf(toParentMenuId));
			if (newParentMenu != null) {
				int newParentLevel = newParentMenu.getMenuLevel();
				String newParentSeq = newParentMenu.getMenuSeq();
				int oldLevel = menu.getMenuLevel();
				for (CapMenu tmpMenu : menus) {
					int oldTmpMenuLevel = tmpMenu.getMenuLevel();
					String oldTmpMenuSeq = tmpMenu.getMenuSeq();
					if (tmpMenu.getMenuId() == menu.getMenuId()) {
						// menu.setCapMenu(newParentMenu);
						menu.setParentMenuId(newParentMenu.getMenuId());
						menu.setMenuLevel(oldTmpMenuLevel - oldLevel
								+ newParentLevel + 1);
						menu.setMenuSeq(newParentSeq
								+ menu.getMenuId()
								+ "." //$NON-NLS-1$
								+ StringUtil.substringAfter(oldTmpMenuSeq,
										menuSeq));
						collectMenus.add(menu);
						continue;
					}
					tmpMenu.setMenuLevel(oldTmpMenuLevel - oldLevel
							+ newParentLevel + 1);
					tmpMenu.setMenuSeq(newParentSeq + menu.getMenuId() + "." //$NON-NLS-1$
							+ StringUtil.substringAfter(oldTmpMenuSeq, menuSeq));
					collectMenus.add(tmpMenu);
				}
				if ("1".equals(newParentMenu.getIsleaf())) { //$NON-NLS-1$
					newParentMenu.setIsleaf("0"); //$NON-NLS-1$
					collectMenus.add(newParentMenu);
				}
			} else {
				int oldLevel = menu.getMenuLevel();
				for (CapMenu tmpMenu : menus) {
					int oldTmpMenuLevel = tmpMenu.getMenuLevel();
					String oldTmpMenuSeq = tmpMenu.getMenuSeq();
					if (tmpMenu.getMenuId() == menu.getMenuId()) {
						// menu.setCapMenu(null);
						menu.setParentMenuId(null);
						collectMenus.add(menu);
						menu.setMenuLevel(oldTmpMenuLevel - oldLevel + 1);
						menu.setMenuSeq(menu.getMenuId()
								+ "." //$NON-NLS-1$
								+ StringUtil.substringAfter(oldTmpMenuSeq,
										menuSeq));
						continue;
					}
					tmpMenu.setMenuLevel(oldTmpMenuLevel - oldLevel + 1);
					tmpMenu.setMenuSeq(menu.getMenuId() + "." //$NON-NLS-1$
							+ StringUtil.substringAfter(oldTmpMenuSeq, menuSeq));

					collectMenus.add(tmpMenu);
				}
			}

			updateMenuBatch(collectMenus.toArray(new CapMenu[collectMenus
					.size()]));
			return true;
		} catch (Throwable t) {
			log.error(
					"Move menu [menuId="
							+ menu.getMenuId()
							+ "] failure, please do the operation again or contact the sysadmin.",
					t);
			return false;
		}
	}

	/**
	 * 
	 * @param resourceID
	 * @param resourceType
	 * @param linkAction
	 * @throws DaoException
	 */
	public void updateMenuByResourceChange(String resourceID,
			String resourceType, String linkAction) throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(CAP_MENU_ENTITY_LINK_TYPE_PROPERTY, resourceType);
		criteria.put(CAP_MENU_ENTITY_LINK_RES_PROPERTY, resourceID);
		CapMenu[] menus = queryMenuListByCriteria(criteria);

		if (menus != null && menus.length > 0) {
			List<CapMenu> menuList = new ArrayList<CapMenu>();
			for (CapMenu menu : menus) {
				if (linkAction != null) {
					if (!linkAction.equals(menu.getLinkAction())) {
						menu.setLinkAction(linkAction);
						menuList.add(menu);
					}
				} else {
					if (menu.getLinkAction() != null) {
						menu.setLinkAction(null);
						menuList.add(menu);
					}
				}
			}
			if (!menuList.isEmpty()) {
				updateMenuBatch(menuList.toArray(new CapMenu[menuList.size()]));
			}
		}
	}

	/**
	 * 
	 * @param temp
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public CapMenu[] queryRootNodes(CapMenu temp, PageCond page)
			throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(CAP_MENU_ENTITY_ISLEAF_PROPERTY, "0"); //$NON-NLS-1$
		return queryMenuListByCriteria(criteria, page);
	}

	/**
	 * 
	 * @param linkRes
	 * @throws DaoException
	 */
	public void deleteMenuByResId(String linkRes) throws DaoException {
		dacbDao.deleteMenuByLinkRes(linkRes);
	}
	
	/**
	 * 
	 * @param linkRess
	 * @throws DaoException
	 */
	public void deleteMenuByResIds(String[] linkRess) throws DaoException {
		dacbDao.deleteMenuByLinkRess(linkRess);
	}
	
}