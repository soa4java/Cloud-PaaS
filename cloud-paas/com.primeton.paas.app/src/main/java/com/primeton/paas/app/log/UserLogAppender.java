/**
 * 
 */
package com.primeton.paas.app.log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import com.primeton.paas.app.config.model.UserLogModel;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-20
 *
 */
public class UserLogAppender extends AppenderSkeleton {
	
	protected Layout layout;
	
	protected String type;

	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#close()
	 */
	public void close() {
		
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#requiresLayout()
	 */
	public boolean requiresLayout() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
	protected void append(LoggingEvent event) {
		if(event != null) {
			StringBuffer message = new StringBuffer(this.layout.format(event));
			String[] statckTrace = event.getThrowableStrRep();
			if(statckTrace != null && statckTrace.length > 0) {
				for (String content : statckTrace) {
					message.append(content).append("\n");
				}
			}
			// toBuffer
			if(type==null){
				type=UserLogModel.DEFAULT_USERLOG_TYPE;
			}
			UserLogData userLog = new UserLogData();
			userLog.setType(type);
			userLog.setMessage(message.toString());
			LogBuffer.cacheUserLog(userLog);
			//	LogBuffer.cacheUserLog(message.toString());
		}
	}

	/**
	 * @return the layout
	 */
	public Layout getLayout() {
		return layout;
	}

	/**
	 * @param layout the layout to set
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	/**
	 * @return log type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
