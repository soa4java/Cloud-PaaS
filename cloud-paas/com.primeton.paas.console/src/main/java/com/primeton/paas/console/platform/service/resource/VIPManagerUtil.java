/**
 * 
 */
package com.primeton.paas.console.platform.service.resource;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.spi.exception.AddressException;
import com.primeton.paas.manage.spi.factory.VIPManagerFactory;
import com.primeton.paas.manage.spi.model.VIPSegment;
import com.primeton.paas.manage.spi.resource.IVIPManager;

/**
 * 虚拟IP管理
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class VIPManagerUtil {
	
	private static ILogger logger = LoggerFactory.getLogger(VIPManagerUtil.class);

	private static IVIPManager vipManager = VIPManagerFactory.getManager();

	/**
	 * 获取所有虚拟机ip地址段
	 * 
	 * @return
	 */
	public static List<VIPSegment> getAllVIPSegment(PageCond page) {

		List<VIPSegment> segments = new ArrayList<VIPSegment>();
		if (vipManager != null) {
			segments = vipManager.getAll(page);
		}
		if (segments.size() == 0 && page.getCurrentPage() <= 1) {
			page.setCount(0);
			return segments;
		}
		if (segments.size() <= 0 && page.getCurrentPage() > 1) {
			page.setBegin(page.getBegin() - page.getLength());
			segments = getAllVIPSegment(page);
		}
		return segments;
	}

	/**
	 * 
	 * @param segmentId
	 * @return
	 */
	public static VIPSegment getVIPSegmentById(String segmentId) {
		return vipManager.get(segmentId);
	}

	/**
	 * 查询所有已使用的虚拟ip
	 * 
	 * @return
	 */
	public static List<String> getUsedVIPs() {
		return vipManager.getUsedVIPs();
	}

	/**
	 * 查询某个网段的已使用的虚拟ip
	 * 
	 * @param id
	 * @return
	 */
	public static List<String> getUsedVIPsBySegmentId(String id) {
		return vipManager.getUsedVIPs(id);
	}

	/**
	 * 查询某个网段的已使用的虚拟ip
	 * 
	 * @param id
	 * @param page
	 * @return
	 */
	public static List<String> getUsedVIPsBySegmentId(String id, IPageCond page) {
		return vipManager.getUsedVIPs(id, page);
	}

	/**
	 * 新增
	 * 
	 * @param segment
	 * @return
	 */
	public static boolean addSegment(VIPSegment segment) {
		String id = vipManager.add(segment);
		if (id != null && !"".equals(id)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param begin
	 * @param end
	 * @param netmask
	 * @return
	 */
	public static VIPSegment makeVIPSegment(String begin, String end,
			String netmask) {
		VIPSegment segment = new VIPSegment();
		if (checkIfNumber(begin) && checkIfNumber(end)) {
			segment.setBegin(begin);
			segment.setEnd(end);
			segment.setNetmask(netmask);
		}
		return segment;
	}

	/**
	 * 删除
	 * 
	 * @param segment
	 * @return
	 */
	public static boolean delSegment(String segmentId) {
		return vipManager.remove(segmentId);
	}

	/**
	 * 
	 * @param segments
	 * @return
	 */
	public static boolean delSegments(VIPSegment[] segments) {
		boolean flag = true;
		if (segments == null || segments.length < 1) {
			return true;
		}

		for (VIPSegment segment : segments) {
			String segmentId = segment.getId();
			VIPSegment ssegment = getVIPSegmentById(segmentId);
			if (ssegment == null) {
				continue;
			}
			boolean result = vipManager.remove(segmentId);
			if (!result) {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 修改
	 * 
	 * @param segment
	 * @return
	 */
	public static boolean updateSegment(VIPSegment segment) {
		String segmentId = segment.getId();
		VIPSegment ssegment = getVIPSegmentById(segmentId);
		if (ssegment != null) {
			return vipManager.update(segment);
		}
		return false;
	}

	/**
	 * 删除ip
	 * 
	 * @param ip
	 *            192.168.100.1,192.168.100.2
	 * @return
	 */
	public static boolean deleteVIP(String ip) {
		String[] ips = ip.split(",");
		try {
			for (String vip : ips) {
				vipManager.release(vip);
			}
		} catch (AddressException e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param id
	 * @param begin
	 * @param end
	 * @param netmask
	 * @return
	 */
	public static VIPSegment makeVIPSegment(String id, String begin,
			String end, String netmask) {
		VIPSegment segment = new VIPSegment();
		if (checkIfNumber(begin) && checkIfNumber(end)) {
			segment.setId(id);
			segment.setBegin(begin);
			segment.setEnd(end);
			segment.setNetmask(netmask);
		}
		return segment;
	}

	/**
	 * 验证ip格式
	 * 
	 * @param ip
	 * @return
	 */
	private static boolean checkIfNumber(String ip) {
		String[] ips = ip.split("\\.");
		Integer num = 0;
		for (int i = 0; i < ips.length; i++) {
			try {
				num = Integer.parseInt(ips[i]);
			} catch (NumberFormatException ex) {
				return false;
			}
			if (num < 0 || num > 255) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param page
	 * @return
	 */
	public static PageCond resetPage(PageCond page) {
		page = new PageCond(0, 10, -1);
		return page;
	}

}
