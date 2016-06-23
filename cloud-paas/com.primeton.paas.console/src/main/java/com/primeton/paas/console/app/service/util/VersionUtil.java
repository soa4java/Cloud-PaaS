/**
 * 
 */
package com.primeton.paas.console.app.service.util;

import java.util.StringTokenizer;

import com.primeton.paas.exception.api.PaasRuntimeException;

/**
 * 应用版本工具类 <br>
 *
 * @author liming (mailto:li-ming@primeton.com)
 */
public class VersionUtil {

	/**
	 * 判断版本是否合法 <br>
	 * 
	 * @param version
	 * @return
	 */
	public static boolean isValid(String version) {
		try {
			addFromOldVersion(version);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	/**
	 * 应用初始版本号 <br>
	 * 
	 * @return 应用初始版本号
	 */
	public static String getInitVersion() {
		return "1.1.1"; //$NON-NLS-1$
	}

	/**
	 * versionSign为 A.B.C (B,C:0..99 A>0).如果不正确,返回异常
	 * 
	 * @param versionSign
	 * @return
	 * @throws WFServiceException
	 */
	public static String addFromOldVersion(String versionSign)
			throws PaasRuntimeException {
		StringTokenizer st = new StringTokenizer(versionSign, ".");
		if (st.countTokens() != 3)
			throw new PaasRuntimeException("version sign is error :"
					+ versionSign);

		String s1 = st.nextToken();
		String s2 = st.nextToken();
		String s3 = st.nextToken();
		try {
			int int1 = Integer.parseInt(s1);
			int int2 = Integer.parseInt(s2);
			int int3 = Integer.parseInt(s3);
			if (int1 <= 0 || int2 > 99 || int3 > 99)
				throw new PaasRuntimeException(
						"version sign is error,number be X.Y.Z (X>0;Y:0-99,Z:0-99)! your version is"
								+ versionSign);
			int3++;
			if (int3 == 100) {
				int3 = 0;
				int2++;
				if (int2 == 100) {
					int2 = 0;
					int1++;
				}
			}
			return int1 + "." + int2 + "." + int3;
		} catch (NumberFormatException e) {
			throw new PaasRuntimeException(
					"version sign is error,number error:" + versionSign);
		}
	}

}
