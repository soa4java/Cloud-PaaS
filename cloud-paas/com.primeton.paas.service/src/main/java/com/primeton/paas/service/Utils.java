/**
 * 
 */
package com.primeton.paas.service;
/**
 * 
 */

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码重构工具类. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class Utils {
	
	private static List<Entry> javaContentEntries = new ArrayList<Utils.Entry>();
	
	private static List<Entry> pomFileEntries = new ArrayList<Utils.Entry>();
	
	private static List<Entry> filePathEntries = new ArrayList<Utils.Entry>();
	
	static {
		javaContentEntries.add(new Entry("com.unionpay.upaas.", "com.primeton.paas."));
		javaContentEntries.add(new Entry("com.unionpay.", "com.primeton."));
		
//		pomFileEntries.add(new Entry("com.unionpay.upaas.", "com.primeton.paas."));
		pomFileEntries.add(new Entry("com.unionpay.upaas", "com.primeton.paas"));
//		pomFileEntries.add(new Entry("com.unionpay.", "com.primeton."));
		pomFileEntries.add(new Entry("com.unionpay", "com.primeton"));
		pomFileEntries.add(new Entry("upaas", "paas"));
		
		filePathEntries.add(new Entry("D:\\git\\PaaS\\upaas\\src\\cloud-upaas", "D:\\git\\PaaS\\paas\\src\\cloud-paas"));
		
		filePathEntries.add(new Entry("com.unionpay.upaas.", "com.primeton.paas.")); // 项目名称前缀
		filePathEntries.add(new Entry("com.unionpay.", "com.primeton.")); // 项目名称前缀
		filePathEntries.add(new Entry("com\\unionpay\\upaas", "com\\primeton\\paas"));
		filePathEntries.add(new Entry("com\\unionpay", "com\\primeton"));
		
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String src = "D:\\git\\PaaS\\upaas\\src\\cloud-upaas";
		
		copy(new File(src), filePathEntries);
	}
	
	/**
	 * 
	 * @param srcFile
	 * @param replaces
	 */
	public static void copy(File srcFile, List<Entry> replaces) {
		if (null == srcFile || null == replaces) {
			return;
		}
		String fileName = srcFile.getName();
		String filePath = srcFile.getAbsolutePath();
		
		String destFilePath = filePath;
		for (Entry entry : replaces) {
			destFilePath = destFilePath.replace(entry.getPattern(), entry.getReplace());
		}
		System.out.println(destFilePath);
		File destFile = new File(destFilePath);
		if (srcFile.isDirectory()) {
			destFile.mkdirs();
			File[] children = srcFile.listFiles();
			if (null != children && children.length > 0) {
				for (File file : children) {
					copy(file, replaces);
				}
			}
		} else {
			try {
				destFile.createNewFile();
				if (fileName.endsWith(".java") || fileName.endsWith(".project")) {
					copy(new FileInputStream(srcFile), new FileOutputStream(destFile), javaContentEntries, true);
					return;
				} else if (fileName.endsWith("pom.xml")) {
					copy(new FileInputStream(srcFile), new FileOutputStream(destFile), pomFileEntries, true);
					return;
				}
				copy(new FileInputStream(srcFile), new FileOutputStream(destFile), true);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param closeables
	 */
	public static void close(Closeable ... closeables) {
		if (null == closeables) {
			return;
		}
		for (Closeable closeable : closeables) {
			if (null != closeable) {
				try {
					closeable.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param input
	 * @param out
	 * @param replaces
	 * @param closeInput
	 */
	public static void copy(InputStream input, OutputStream out, List<Entry> replaces, boolean closeInput) {
		if (null == input || null == out) {
			return;
		}
		ByteArrayInputStream byteInput = null;
		try {
			if (null == replaces || replaces.isEmpty()) {
				copy(input, out, false);
				return;
			}
			
			byte[] bytes = new byte[input.available()];
			input.read(bytes);
			String content = new String(bytes);
			for (Entry entry : replaces) {
				if (content.contains(entry.getPattern())) {
					content = content.replaceAll(entry.getPattern(), entry.getReplace());
				}
			}
			copy(content.getBytes(), out, false);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (closeInput) {
				close(input);
			}
			close(out, byteInput);
		}
		
	}
	
	/**
	 * 
	 * @param input
	 * @param out
	 * @param close
	 */
	public static void copy(InputStream input, OutputStream out, boolean close) {
		if (null == input || null == out) {
			return;
		}
		try {
			byte[] bytes = new byte[input.available()];
			input.read(bytes);
			out.write(bytes);
		} catch (Exception e) {
		} finally {
			if (close) {
				close(out);
				close(input);
			}
		}
	}
	
	/**
	 * 
	 * @param bytes
	 * @param out
	 * @param close
	 */
	public static void copy(byte[] bytes, OutputStream out, boolean close) {
		if (null == bytes || null == out) {
			return;
		}
		try {
			out.write(bytes);
		} catch (Exception e) {
		} finally {
			if (close) {
				close(out);
			}
		}
	}
	
	/**
	 * 
	 * @author ZhongWen
	 *
	 */
	public static class Entry {
		
		String pattern;
		String replace;
		
		public Entry() {
			super();
		}

		public Entry(String pattern, String replace) {
			super();
			this.pattern = pattern;
			this.replace = replace;
		}

		public String getPattern() {
			return pattern;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		public String getReplace() {
			return replace;
		}

		public void setReplace(String replace) {
			this.replace = replace;
		}
		
	}

}
