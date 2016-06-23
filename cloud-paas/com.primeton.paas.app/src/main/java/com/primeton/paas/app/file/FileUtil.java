/**
 * 
 */
package com.primeton.paas.app.file;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.primeton.paas.app.api.file.IFile;
import com.primeton.paas.app.api.file.InnerFileUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class FileUtil {
	
	/**
	 * 
	 * @param rootPath
	 * @param path
	 * @return
	 */
	public static IFile getFile(String rootPath, String path) {
		return new FileImpl(rootPath, path);
	}
	
	/**
	 * 
	 * @param rootPath
	 * @param parent
	 * @param childPath
	 * @return
	 */
	public static IFile getFile(String rootPath, IFile parent, String childPath) {
		if (parent == null) {
			throw new IllegalArgumentException("parent file is null!");
		}
		if (childPath == null || childPath.trim().length() == 0) {
			throw new IllegalArgumentException("childPath is null!");
		}
		return getFile(rootPath, parent.getPath() + "/" +  childPath);
	}
	
	/**
	 * 
	 * 
	 * @param rootPath
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static InputStream getInputStream(String rootPath, IFile file) throws FileNotFoundException, IOException {
		if (file == null) {
			throw new IllegalArgumentException("file is null!");
		}
		if (!file.exists()) {
			throw new FileNotFoundException(file.getPath());
		}
		if (file.isDirectory()) {
			throw new IllegalArgumentException("'" + file.getPath() + "' is dir, not file!");
		}
		IFile fileLock = getFile(rootPath, file.getPath() + ".lock");
		try {
			return new FileInputStreamImpl(file,fileLock);
		} catch (FileNotFoundException e) {
			try {
				if (fileLock != null) {
					fileLock.delete();
				}
			} catch (Throwable ignore) {			
			}
			throw new FileNotFoundException(file.getPath());
		} catch (RuntimeException e) {
			try {
				if (fileLock != null) {
					fileLock.delete();
				}
			} catch (Throwable ignore) {			
			}
			throw e;
		}
	}
	
	/**
	 * 
	 * @param rootPath
	 * @param file
	 * @param isAppend
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static OutputStream getOutputStream(String rootPath, IFile file, boolean isAppend) throws FileNotFoundException, IOException {
		if (file == null) {
			throw new IllegalArgumentException("file is null!");
		}
		if (!file.exists()) {
			throw new FileNotFoundException(file.getPath());
		}
		if (file.isDirectory()) {
			throw new IllegalArgumentException("'" + file.getPath() + "' is dir, not file!");
		}
		IFile fileLock = getFile(rootPath, file.getPath() + ".lock");
		try {
			return new FileOutputStreamImpl(file, fileLock, isAppend);
		} catch (FileNotFoundException e) {
			try {
				if (fileLock != null) {
					fileLock.delete();
				}
			} catch (Throwable ignore) {			
			}
			throw new FileNotFoundException(file.getPath());
		} catch (RuntimeException e) {
			try {
				if (fileLock != null) {
					fileLock.delete();
				}
			} catch (Throwable ignore) {			
			}
			throw e;
		}
	}
	
	/**
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static byte[] read(IFile file) throws FileNotFoundException,
			IOException {
		if (file == null) {
			throw new IllegalArgumentException("file is null!");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException("file'" + file.getPath()
					+ "' is not existed!");
		}
		if (file.isDirectory()) {
			throw new IllegalArgumentException("zipFile'" + file.getPath()
					+ "' is dir, not file!");
		}

		InputStream fileIn = null;
		try {
			fileIn = InnerFileUtil.getInputStream(file);
			long size = file.length();
			if (size > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("File is too big:" + size);
			}
			return read(fileIn);
		} finally {
			closeQuietly(fileIn);
		}
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	private static byte[] read(InputStream input) throws IOException {
		if (input == null) {
			throw new IllegalArgumentException("InputStream is null!");
		}
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		copy(input, byteOut, 1024 * 4);
		return byteOut.toByteArray();
	}
	
	/**
	 * 
	 * @param input
	 * @param output
	 * @param bufferSize
	 * @return
	 * @throws IOException
	 */
	private static long copy(InputStream input, OutputStream output,
			int bufferSize) throws IOException {
		if (input == null) {
			throw new IllegalArgumentException("InputStream is null!");
		}
		if (output == null) {
			throw new IllegalArgumentException("OutputStream is null!");
		}
		if (bufferSize <= 0) {
			bufferSize = 1024 * 4;
		}
		ReadableByteChannel readChannel = Channels.newChannel(input);
		WritableByteChannel writeChannel = Channels.newChannel(output);
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		long count = 0;
		while (readChannel.read(buffer) != -1) {
			buffer.flip(); // Prepare for writing
			writeChannel.write(buffer);
			count += buffer.position();
			buffer.clear(); // Prepare for reading
		}
		return count;
	}
	
	/**
	 * 
	 * @param ioObj
	 */
	private static void closeQuietly(Object ioObj) {
		if (ioObj == null) {
			return;
		}

		if (ioObj instanceof Closeable) {
			try {
				((Closeable) ioObj).close();
				return;
			} catch (Throwable ignore) {
			}
		} else {
			try {
				Method method = ioObj.getClass().getMethod("close",
						new Class[0]);
				if (method != null) {
					method.invoke(ioObj, new Object[0]);
					return;
				}
			} catch (Throwable ignore) {
			}
			throw new IllegalArgumentException("ioObj'" + ioObj.getClass()
					+ "' is not support type!");
		}
	}
	
}
