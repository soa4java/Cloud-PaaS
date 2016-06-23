/**
 * 
 */
package com.primeton.paas.manage.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

/**
 * Copy from cesium agent-java. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class RuntimeExec {
	
	private static ILogger logger = LoggerFactory.getLogger(RuntimeExec.class);
	
	public RuntimeExec() {
		super();
	}

    /**
     * 
     * @param cmd
     * @param timeout
     * @return
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public String execute(final String cmd, final long timeout) 
    		throws IOException, TimeoutException, InterruptedException {
		if (cmd == null || cmd.trim().length() == 0) {
			return null;
		}
    	long time = 1000L * 5;
    	time = timeout > 0 ? timeout : time;
        
    	long begin = System.currentTimeMillis();
    		
    	logger.info("Begin execute command { " + cmd + " }, timeout = " + time + " seconds.");
        
    	Process process = Runtime.getRuntime().exec(cmd); 
        
        TimeWorker timeWorker = new TimeWorker(process);
        process.getOutputStream().close();
        OutputWorker outputWorker = new OutputWorker(process.getInputStream());
        String result = null;
        outputWorker.start();
        timeWorker.start();
        try {
            timeWorker.join(time);
            if (timeWorker.exit == null) {
                throw new TimeoutException(cmd);
            } else if(timeWorker.exit != 0) {
            	logger.error("Execute command " + cmd + " error, error message: " + this.getOutput(process.getErrorStream()));
                result = "error";
            } else {
                result = outputWorker.output;
            }
        } catch (InterruptedException e) {
            timeWorker.interrupt();
            Thread.currentThread().interrupt();
            throw e;
        } finally {
            process.destroy();
        }
        
        long end = System.currentTimeMillis();
        logger.info("Finish execute command { " + cmd + " }, time spent " + (end - begin)/1000L + " seconds.");
    	
    	return result;
    }
    
    /**
     * 
     * @param stream
     * @return
     * @throws IOException
     */
    private String getOutput(InputStream stream) throws IOException {
		if (stream == null) {
			return null;
		}
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        while((line = reader.readLine()) != null) {
            buffer.append(line + "\n");
        }
        reader.close();
        return buffer.toString().trim();
    }
    
    /**
     * 
     * @author lizhongwen(mailto:lizw@primeton.com)
     *
     */
    private static class OutputWorker extends Thread {
    	
        private final InputStream stream;
        private String output = null;

        private OutputWorker(InputStream stream) {
            this.stream = stream;
        }
        
        public void  run() {
            String line = null;
            StringBuffer buffer=new StringBuffer();
            BufferedReader reader=null;
            try {
                reader = new BufferedReader(new InputStreamReader(stream));
                while((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }
                this.output = buffer.toString().trim();
            } catch (IOException e) {
            	logger.error(e.getMessage());
            } finally {
                if(reader!=null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    	logger.error(e.getMessage());
                    }
                }
            }
        }

    }
    
    /**
     * 
     * @author lizhongwen(mailto:lizw@primeton.com)
     *
     */
    private static class TimeWorker extends Thread {
    	
        private final Process process;
        
        private Integer exit = null;

        private TimeWorker(Process process) {
            this.process = process;
        }
        
        public void run() {
            try {
                exit = process.waitFor();
            } catch (InterruptedException e) {
            	logger.error(e.getMessage());
            }
        }
    }
    
}
