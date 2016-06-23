/**
 * 
 */
package com.primeton.paas.console.app.service.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author liming(mailto:li-ming@primeton.com)
 * 
 */
public class FileUtil {

	/**
	 * 
	 * @param upFile
	 * @param path
	 * @throws IOException
	 */
	public static void saveFile(File upFile, String path) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(upFile));
			bos = new BufferedOutputStream(new FileOutputStream(path));
			byte[] buf = new byte[(int) upFile.length()];
			int len = 0;
			while (((len = bis.read(buf)) != -1)) {
				bos.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param uploadFileStream
	 * @param path
	 * @throws IOException
	 */
	public static void saveFile(InputStream uploadFileStream, String path)
			throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(uploadFileStream);
			bos = new BufferedOutputStream(new FileOutputStream(path));
			byte[] buf = new byte[1024 * 5];
			int len = 0;
			while (((len = bis.read(buf)) != -1)) {
				bos.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (uploadFileStream != null) {
					uploadFileStream.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Don't read larger file. <br>
	 * 
	 * @param path
	 * @return
	 */
	public static byte[] readFile(String path) {
		if (null == path || path.trim().length() == 0) {
			return null;
		}
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				byte[] b = new byte[fis.available()];
				fis.read(b);
				return b;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (null != fis) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}
		return null;
	}

}