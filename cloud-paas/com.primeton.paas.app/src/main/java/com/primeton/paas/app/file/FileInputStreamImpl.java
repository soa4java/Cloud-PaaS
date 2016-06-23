/**
 * 
 */
package com.primeton.paas.app.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.primeton.paas.app.api.file.IFile;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class FileInputStreamImpl extends FileInputStream {
	
	/**
	 * 
	 * @param file
	 * @param fileLock
	 * @throws FileNotFoundException
	 */
	public FileInputStreamImpl(IFile file, IFile fileLock) throws FileNotFoundException {
		super(((FileImpl)file).getFile());
	}
	
}