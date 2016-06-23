/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.spi.model.VIPSegment;

/**
 * 
 * @author Yi.Liu (mailto:liu-yi@primeton.com)
 *
 */
public class AddressUtil {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(AddressUtil.class);

	/**
	 * Default. <br>
	 */
	private AddressUtil() {
		super();
	}

	/**
	 * @param segment
	 * @param vips
	 * @return
	 */
	public static String getAvailableVIP(VIPSegment segment, List<String> vips) {
		String vip = segment.getBegin();
		String end = segment.getEnd();
		while (true) {
			if (vips.contains(vip)) {
				vip = getNext(vip);
				if (outOfSegment(vip, end)) {
					vip = null;
					break;
				}
			} else {
				break;
			}
		}
		return vip;
	}

	/**
	 * 
	 * @param segment
	 * @param segments
	 * @return
	 */
	public static boolean ifAvailableSegment(VIPSegment segment,
			List<VIPSegment> segments) {
		if (segments == null || segments.isEmpty()) {
			return true;
		}
		for (VIPSegment vSegment : segments) {
			if (segment.getId() != null
					&& segment.getId().equals(vSegment.getId())) {
				continue;
			}
			if (cover(segment, vSegment)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean cover(VIPSegment s1, VIPSegment s2) {
		String[] seArray1Begin = s1.getBegin().split("\\.");
		String[] seArray2Begin = s2.getBegin().split("\\.");

		for (int i = 0; i < 3; i++) {
			if (!seArray1Begin[i].equals(seArray2Begin[i])) {
				return false;
			}
		}

		String[] seArray1End = s1.getEnd().split("\\.");
		String[] seArray2End = s2.getEnd().split("\\.");

		Integer s1Begin = Integer.parseInt(seArray1Begin[3]);
		Integer s1End = Integer.parseInt(seArray1End[3]);
		Integer s2Begin = Integer.parseInt(seArray2Begin[3]);
		Integer s2End = Integer.parseInt(seArray2End[3]);

		Integer begin = Math.max(s1Begin, s2Begin);
		Integer end = Math.min(s1End, s2End);
		int result = end - begin;
		if (result < 0) {
			return false;
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("New vipSegment coincide with the old!");
			}
			return true;
		}
	}

	/**
	 * 
	 * @param vip
	 * @return
	 */
	public static String getNext(String vip) {
		String[] vipArray = vip.split("\\.");
		Integer lastNumber = Integer.parseInt(vipArray[3]);
		String next = String.valueOf(lastNumber + 1);
		String nextIp = vipArray[0] + "." + vipArray[1] + "." + vipArray[2]
				+ "." + next;
		return nextIp;
	}

	/**
	 * 
	 * @param vip
	 * @param end
	 * @return
	 */
	public static boolean outOfSegment(String vip, String end) {
		if (Integer.parseInt(vip.split("\\.")[3]) > Integer.parseInt(end
				.split("\\.")[3])) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param oldSegment
	 * @param usedIPs
	 * @param newSegment
	 * @return
	 */
	public static boolean ifCanUpdate(VIPSegment oldSegment,
			List<String> usedIPs, VIPSegment newSegment) {
		if (usedIPs != null && !usedIPs.isEmpty()
				&& !sameSegment(oldSegment, newSegment)) {
			return false;
		}
		Integer begin = Integer.parseInt(newSegment.getBegin().split("\\.")[3]);
		Integer end = Integer.parseInt(newSegment.getEnd().split("\\.")[3]);

		for (String ip : usedIPs) {
			Integer ipInt = Integer.parseInt(ip.split("\\.")[3]);
			if (ipInt < begin || ipInt > end) {
				if (logger.isWarnEnabled()) {
					logger.warn("New segment exclude ips in using!!");
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param oldSegment
	 * @param newSegment
	 * @return
	 */
	private static boolean sameSegment(VIPSegment oldSegment,
			VIPSegment newSegment) {
		String[] oldBegin = oldSegment.getBegin().split("\\.");
		String[] newBegin = newSegment.getBegin().split("\\.");

		for (int i = 0; i < 3; i++) {
			if (!oldBegin[i].equals(newBegin[i])) {
				if (logger.isWarnEnabled()) {
					logger.warn("There hava some ips in using,new Segment must be the same segment!!");
				}
				return false;
			}
		}
		return true;
	}

}
