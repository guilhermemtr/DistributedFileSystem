package utils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Enumeration;

import contactServer.IContactServer;
import exceptions.NoPermissionsException;
import exceptions.NoSuchServerException;
import exceptions.ServerOfflineException;

public class Domains {
	
	public static final String CONTACTSEARCHADDRESS = "224.0.0.1";

	public static final int CONTACTSEARCHPORT = 21431;
	
	public static final String CONTACTSERVER = "ContactServer";
	public static final String SYNCFILENAME = ".sync";
	public static final String LOCALSUFFIX = ".local";
	
	public static boolean isLocal(String file) {
		boolean isLocal = false;
		if(file.equals(SYNCFILENAME)) isLocal = true;
		if(file.endsWith(LOCALSUFFIX)) isLocal = true;
		return isLocal;
	}
	
	
	public static String parsePath(String path) {
		String file;
		try {
			file = new File("/" + path).getCanonicalPath();
			String npath = file.equals("/") ? "." : file.substring(1);
			return npath;
		} catch (IOException e) {
		}
		return null;
	}
	
	public static String getPath(String path) {
		return path.endsWith("/") ? path : path + "/";
	}
	
	public static String getFileName(String filename) {
		return "\"" + filename + "\"";
	}
	
	/*
	 * Gets the url of a server.
	 * The user has to have permissions to access the server.
	 * The contact server must be online.
	 */
	public static String getURL(String server, String user,
			IContactServer contact) throws ServerOfflineException,
			NoPermissionsException, NoSuchServerException {
		int tries = RemoteUtils.NTRIES;
		while (tries > 0) {
			try {
				String serverURL = contact.getFileServer(user, server);
				Logger.log("Got " + server + " for user " + user);
				return serverURL;
			} catch (RemoteException e) {
				tries--;
				if (tries == 0) {
					Logger.log("Couldnt get " + server + " url");
					throw new ServerOfflineException(
							"Contact server is offline");
				}
			}
		}
		return null;
	}

	/*
	 * Builds an RMI url.
	 * Given the hostname, the port and the servername, it creates a url to access it.
	 */
	public static String buildRmiURL(String serverName, String hostName,
			int port) {
		if (port > 0) {
			return "//" + hostName + ":" + port + "/" + serverName;
		} else {
			return "//" + hostName + "/" + serverName;
		}
	}

	/*
	 * Creates a file server url, for a serverName whose owner is userName.
	 */
	public static String getFileServerURL(String serverName, String userName) {
		return serverName + "@" + userName;
	}

	public static String getSyncFilename(String localPath, String server, String user, String path) {
		return server + "@" + user + "_" + path + SYNCFILENAME;
	}
	
	/*
	 * Tries to get the external ip address.
	 * If it cant, it tries to get a local one.
	 * In the worst case, it returns null.
	 */
	public static String getIP() {
		Logger.log("Getting network interfaces");
		Enumeration<NetworkInterface> e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			Logger.log(e.toString());
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Logger.log(n.getDisplayName());
				if (n.isLoopback() || !n.isUp() || !n.supportsMulticast()) {
					Logger.log("Invalid interface");
					continue;
				}
				Enumeration<InetAddress> ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					Logger.log(i.toString());
					if (!i.getHostAddress().startsWith("127.")
							&& !i.getHostAddress().startsWith("fe80"))
						return i.getHostAddress();
				}
			}
			Logger.log("Problems with internet connection.");
		} catch (SocketException e1) {
			Logger.log("Problems with internet connection.");
		}
		try {
			Logger.log("No Internet connection...");
			Logger.log("Getting local address");
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			Logger.log("Im sorry for your computer.");
			return null;
		}
	}

	/*
	 * Tries to get a contact server.
	 */
	@SuppressWarnings("resource")
	public static String getContactServer() {
		Logger.log("Getting ip address");
		String ip = Domains.getIP();
		if(ip == null) {
			Logger.log("Couldnt get ip address.");
			return null;
		}
		Logger.log("Got ip address " + ip);
		try {
			Logger.log("Creating multicast socket");
			// Creates a multicast socket to send request to the contact
			// servers.
			MulticastSocket client = new MulticastSocket();

			Logger.log("Creating request packet");
			// Creates a datagram with the request.
			DatagramPacket request = new DatagramPacket(ip.getBytes(),
					ip.getBytes().length,InetAddress.getByName(CONTACTSEARCHADDRESS),CONTACTSEARCHPORT);

			Logger.log("Contact server's multicast group: "
					+ CONTACTSEARCHADDRESS);

			Logger.log("Creating response datagram packet");
			// Creates a packet to receive the response data.
			byte[] responseData = new byte[1024];
			DatagramPacket responsePacket = new DatagramPacket(responseData,
					responseData.length);

			Logger.log("Trying to get a response from any contact server");
			// Tries to get a contact server ipAddress.
			int tries = RemoteUtils.NTRIES;
			boolean done = false;

			while (!done) {
				try {
					// Sets the timeout for the response to the request.
					Logger.log("Set the timeout");

					Logger.log("Sending the client request");
					// Sends the request packet
					client.send(request);

					client.setSoTimeout(RemoteUtils.RETRYTIME);
					// Tries to receive the response packet
					client.receive(responsePacket);
					Logger.log("Received a response from a contact server");

					// Closes resources
					client.close();
					Logger.log("Closed opened sockets");

					String contactServerUrl = new String(responseData).trim();
					Logger.log(contactServerUrl);

					return contactServerUrl;
				} catch (SocketTimeoutException e) {
					tries--;
					if (tries == 0) {
						// Closes resources
						client.close();

						Logger.log("Closed opened sockets");

						Logger.log("Failed to get a response from contact server");

						throw new ServerOfflineException(
								"Any Contact Server has answered the request");
					}
				}
				RemoteUtils.sleepToRetry();
			}
		} catch (Exception e) {
			Logger.log("Got an unexpected exception");
			Logger.log(e.getLocalizedMessage());
		}
		return null;

	}
}
