/**
 * 
 */
package com.primeton.paas.openapi.base.uitl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public final class NetUtil {

	private NetUtil() {
		super();
	}

	public static String getLocalHostIPAddress() {
		String[] ips = getAllLocalHostIPAddress(false);
		if (ips.length > 0) {
			return ips[0];
		}
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			try {
				return InetAddress.getByName(null).getHostAddress();
			} catch (UnknownHostException ex) {
				return "localhost";
			}
		}
	}

	public static String[] getAllLocalHostIPAddress() {
		return getAllLocalHostIPAddress(true);
	}

	private static String[] getAllLocalHostIPAddress(boolean allIpFlag) {
		List<String> ips = new ArrayList<String>();
		try {
			Enumeration<?> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				Enumeration<?> netAddresses = ni.getInetAddresses();
				while (netAddresses.hasMoreElements()) {
					ip = (InetAddress) netAddresses.nextElement();
					//	if ((ip != null) && ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
					//		&& ip.getHostAddress().indexOf(":") == -1) {
					if ((ip != null) && !ip.isLoopbackAddress()	 
							&& ip.getHostAddress().indexOf(":") == -1) {
						String address = ip.getHostAddress();
						if (allIpFlag) {
							ips.add(address);
						} else {
							return new String[] { address };
						}
					}
				}
			}
		} catch (Exception ignore) {
		}
		return ips.toArray(new String[0]);
	}

	public static String getHostIPAddress(String hostName) {
		try {
			return InetAddress.getByName(hostName).getHostAddress();
		} catch (UnknownHostException e1) {
			return null;
		}
	}

	public static String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			try {
				return InetAddress.getByName(null).getHostName();
			} catch (UnknownHostException e1) {
				return null;
			}
		}
	}

	public static String getHostName(String ipv4) {
		try {
			String[] ips = ipv4.split("[.]");
			if (ips.length == 4) {
				return getInetAddress(ipv4).getCanonicalHostName();
			}
		} catch (UnknownHostException e) {
		}
		return null;
	}

	public static boolean canConnect(String ip, int port, int timeOut) {
		if (ip == null || port == 0) {
			return false;
		}
		try {
			InetAddress serverAddress = getInetAddress(ip);
			if (!checkIP(serverAddress, timeOut)) {
				return false;
			}
			if (!checkPort(serverAddress, port, timeOut)) {
				return false;
			}
		} catch (Throwable t) {
			return false;
		}
		return true;
	}

	private static boolean checkIP(InetAddress address, int timeOut) {
		if (address == null) {
			return false;
		}
		try {
			return address.isReachable(timeOut);
		} catch (Throwable e) {
			return false;
		}
	}

	private static boolean checkPort(InetAddress address, int port, int timeOut) {
		if (address == null || port == 0) {
			return false;
		}
		Socket clientSocket = null;
		InetSocketAddress serverAddressEndpoint = null;
		try {
			clientSocket = new Socket();
			serverAddressEndpoint = new InetSocketAddress(address, port);
			clientSocket.connect(serverAddressEndpoint, timeOut);
			return true;
		} catch (Throwable e) {
			return false;
		} finally {
			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	public static InetAddress getInetAddress(String ipv4) throws  UnknownHostException {
			String[] ips = ipv4.split("[.]");
			return InetAddress.getByAddress(new byte[]{
						(byte)Integer.parseInt(ips[0]),
						(byte)Integer.parseInt(ips[1]),
						(byte)Integer.parseInt(ips[2]),
						(byte)Integer.parseInt(ips[3])});

	}

	public static void closeQuietly(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
	
}
