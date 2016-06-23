/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.List;
import java.util.UUID;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultVIPDao;
import com.primeton.paas.manage.api.impl.util.AddressUtil;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.exception.AddressException;
import com.primeton.paas.manage.spi.model.VIPSegment;
import com.primeton.paas.manage.spi.resource.IVIPManager;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultVIPManager implements IVIPManager {
	
	private DefaultVIPDao vipDao = DefaultVIPDao.getInstance();
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultVIPManager.class);
	

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVIPManager#add(com.primeton.paas.manage.spi.model.VIPSegment)
	 */
	public String add(VIPSegment segment) {
		if (segment.getBegin() == null || segment.getEnd() == null
				|| segment.getNetmask() == null) {
			return null;
		}
		List<VIPSegment> segments = vipDao.getAllVIPSegment();
		if (!AddressUtil.ifAvailableSegment(segment, segments)) {
			logger.error("Segment [" + segment.getBegin() + "] to ["
					+ segment.getEnd() + "] is not available!");
			return null;
		}
		segment.setId(UUID.randomUUID().toString());
		boolean success = vipDao.addVIPSegment(segment);
		if (!success) {
			logger.error("save " + segment + "failed!");
			return null;
		}
		return segment.getId();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVIPManager#remove(java.lang.String)
	 */
	public boolean remove(String id) {
		return vipDao.deleteVIPSegment(id);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVIPManager#update(com.primeton.paas.manage.spi.model.VIPSegment)
	 */
	public boolean update(VIPSegment segment) {
		if (segment.getId() == null || segment.getBegin() == null
				|| segment.getEnd() == null || segment.getNetmask() == null) {
			return false;
		}
		List<VIPSegment> segments = vipDao.getAllVIPSegment();
		if (!AddressUtil.ifAvailableSegment(segment, segments)) {
			logger.error("Segment [" + segment.getBegin() + "] to ["
					+ segment.getEnd() + "] is not available!");
			return false;
		}
		List<String> ips = vipDao.getUsedIPs(segment.getId());
		VIPSegment oldSegment = vipDao.getVIPSegment(segment.getId());
		if (ips != null && !ips.isEmpty()) {
			if (!AddressUtil.ifCanUpdate(oldSegment, ips, segment)) {
				return false;
			}
		}
		return vipDao.updateVIPSegment(segment);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVIPManager#get(java.lang.String)
	 */
	public VIPSegment get(String id) {
		return vipDao.getVIPSegment(id);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVIPManager#getAll(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public List<VIPSegment> getAll(IPageCond pageCond) {
		return vipDao.getAllVIPSegment(pageCond);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVIPManager#apply()
	 */
	public synchronized String apply() throws AddressException {
		String vip = null;
		String id = null;
		String netmask = null;
		List<VIPSegment> segments = vipDao.getAllVIPSegment();
		if (segments == null || segments.isEmpty()) {
			throw new AddressException("Apply Failed,no available segment");
		}
		for (VIPSegment segment : segments) {
			List<String> ips = vipDao.getUsedIPs(segment.getId());
			vip = AddressUtil.getAvailableVIP(segment, ips);
			netmask = segment.getNetmask();
			id = segment.getId();
			if (StringUtil.isEmpty(vip)) {
				break;
			}
		}

		if (StringUtil.isEmpty(vip)) {
			throw new AddressException(
					"Apply Failed,no more available IP in current segment");
		}
		vipDao.addVIP(vip, id);
		return vip + "/" + netmask;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVIPManager#apply(java.lang.String)
	 */
	public synchronized String apply(String id) throws AddressException {
		VIPSegment segment = vipDao.getVIPSegment(id);
		List<String> ips = vipDao.getUsedIPs(segment.getId());
		String vip = AddressUtil.getAvailableVIP(segment, ips);

		if (StringUtil.isEmpty(vip)) {
			throw new AddressException(
					"Apply Failed, no more available IP in current segment");
		}
		vipDao.addVIP(vip, id);
		return vip + "/" + segment.getNetmask(); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVIPManager#release(java.lang.String)
	 */
	public void release(String vip) throws AddressException {
		vipDao.deleteVIP(vip);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVIPManager#getUsedVIPs()
	 */
	public List<String> getUsedVIPs() {
		return vipDao.getAllUsedIPs();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVIPManager#getUsedVIPs(java.lang.String)
	 */
	public List<String> getUsedVIPs(String id) {
		return vipDao.getUsedIPs(id);
	}
	
	public List<String> getUsedVIPs(String id,IPageCond page){
		return vipDao.getUsedIPs(id,page);
	}

}
