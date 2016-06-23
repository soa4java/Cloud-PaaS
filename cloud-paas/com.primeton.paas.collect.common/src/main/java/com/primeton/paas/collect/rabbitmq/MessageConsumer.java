/**
 * 
 */
package com.primeton.paas.collect.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-17
 *
 */
public abstract class MessageConsumer implements Runnable {
	
	private String connName;
	private String queueName;
	private long timeout;
	
	private boolean flag = true;
	private boolean isRunning = false;
	
	/**
	 * @param connName AMQConnection name
	 * @param queueName message queue
	 * @param timeout fetch message timeout once
	 */
	public MessageConsumer(String connName, String queueName, long timeout) {
		super();
		this.connName = connName;
		this.queueName = queueName;
		this.timeout = timeout;
	}
	
	/**
	 * Begin fetch message from queue <br>
	 * 
	 */
	public void open() {
		if (isRunning && flag) {
			return;
		}
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.setName(MessageConsumer.class.getSimpleName() + "-" + queueName);
		t.start();
	}
	
	/**
	 * 
	 */
	public void close() {
		if (isRunning || flag) {
			flag = false;
		}
	}
	
	public void close(long timeout) {
		if (isRunning() || flag) {
			flag = false;
		}
		long begin = System.currentTimeMillis();
		long end = begin;
		
		while (true) {
			end = System.currentTimeMillis();
			if (end - begin > timeout) {
				break;
			}
			if (!isRunning()) {
				break;
			}
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				// ignore
				// e.printStackTrace();
			}
		}
	}

	/**
	 * @return the connName
	 */
	public String getConnName() {
		return connName;
	}

	/**
	 * @param connName the connName to set
	 */
	public void setConnName(String connName) {
		this.connName = connName;
	}

	/**
	 * @return the queueName
	 */
	public String getQueueName() {
		return queueName;
	}

	/**
	 * @param queueName the queueName to set
	 */
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		isRunning = true;
		while (flag) {
			try {
				byte[] message = consume();
				if (message != null && message.length > 0) {
					doMessage(message);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		isRunning = false;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException 
	 * @throws ConsumerCancelledException 
	 * @throws ShutdownSignalException 
	 */
	public byte[] consume() {
		Channel channel = null;
		try {
			Connection conn = AMQConnectionFactory.getConnection(connName);
			channel = conn.createChannel();
			try {
				// If queue not exists, then throw exception and channel auto closed.
				channel.queueDeclarePassive(queueName);
			} catch (Throwable e) {
				// Message queue not exists, then declare it use default parameters.
				channel = conn.createChannel();
				channel.queueDeclare(queueName, false, false, false, null);
			}
			// New message consumer
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			QueueingConsumer.Delivery delivery = consumer.nextDelivery(timeout);
			return delivery == null ? null : delivery.getBody();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			AMQUtil.close(channel);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageConsumer [connName=" + connName + ", queueName="
				+ queueName + "]";
	}

	/**
	 * Handle message from message queue(decode message to java.lang.String or other java model) <br>
	 * 
	 * @param bytes message content
	 */
	public abstract void doMessage(byte[] message);
	

}
