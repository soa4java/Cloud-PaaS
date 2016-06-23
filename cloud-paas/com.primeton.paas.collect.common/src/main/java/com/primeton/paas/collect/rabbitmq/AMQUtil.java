/**
 * 
 */
package com.primeton.paas.collect.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-17
 *
 */
public class AMQUtil {
	
	public static void close(Channel channel) {
		if (channel != null) {
			try {
				channel.close();
			} catch (Throwable t) {
				try {
					channel.abort();
				} catch (IOException ignore) {
					// ignore.printStackTrace();
				}
			}
		}
	}
	
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (Throwable t) {
				connection.abort();
			}
		}
	}

}
