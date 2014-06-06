package rmiRemoteFileServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import utils.Domains;
import utils.Logger;
import utils.RemoteUtils;
import contactServer.IContactServer;
import exceptions.ServerAlreadyExistsException;

public class RemoteFileServerUtils {

	public static void keepAlive(IRmiRemoteFileServer fserver,
			IContactServer contact, String serverName, String user, String url) {
		while (true) {
			RemoteUtils.sleepToRetry("checking contact server",
					RemoteUtils.LONGRETRYTIME);
			try {
				if (contact != null) {
					contact.alive();
				} else {
					Logger.log("Something has gone wrong with the contact server.");
					contact = getContactServer(Domains.getContactServer());
					registerFileServer(serverName, user, contact, url);
					exportFileServerDomain(url, fserver);
					RemoteUtils.sleepToRetry();
				}
			} catch (RemoteException e) {
				Logger.log("Something has gone wrong with the contact server.");
				contact = getContactServer(Domains.getContactServer());
				registerFileServer(serverName, user, contact, url);
				exportFileServerDomain(url, fserver);
				RemoteUtils.sleepToRetry();
			}
		}
	}

	public static void setupSecurityManager() {
		Logger.log("Setting new system security policy");
		System.getProperties().put("java.security.policy", "policy.all");

		if (System.getSecurityManager() == null) {
			Logger.log("Setting up a new security manager for file server");
			System.setSecurityManager(new RMISecurityManager());
		}
	}

	public static IContactServer getContactServer(String contactAddress) {
		if (contactAddress == null)
			return null;
		int tries = RemoteUtils.NTRIES;
		IContactServer contact = null;
		while (true) {

			try {
				Logger.log("Finding contact server:");
				Logger.log(contactAddress);
				contact = (IContactServer) Naming.lookup(contactAddress);
				Logger.log("Found contact server.");
				break;
			} catch (MalformedURLException e) {
				Logger.log("Couldn't find server... exiting (-1)");
				System.exit(-1);
			} catch (NotBoundException e) {
				Logger.log("No such URL Server registered... exiting(-1)");
				System.exit(-1);
			} catch (RemoteException e) {
				tries--;
				contactAddress = Domains.getContactServer();
				if (tries == 0) {
					Logger.log("Check your network connection and retry.");
					System.exit(-1);
				}
				RemoteUtils.sleepToRetry();
			}
		}
		return contact;
	}

	public static void registerFileServer(String serverName, String user,
			IContactServer contact, String url) {
		if (contact == null)
			return;

		while (true) {
			try {
				try {
					contact.registerFileServer(serverName, url, user);
				} catch (ServerAlreadyExistsException e) {
					Logger.log("Server was already registered.");
					break;
				}
				Logger.log("Registered file server to contact server.");
				break;
			} catch (RemoteException e) {
				Logger.log("Couldn't register to contact server...");
				break;
			}
		}
	}

	public static void exportFileServerDomain(String url,
			IRmiRemoteFileServer server) {
		try {
			Naming.rebind(url, server);
		} catch (RemoteException e) {
			Logger.log("Couldn't rebind the file server.");
		} catch (MalformedURLException e) {
			Logger.log("Malformed URL Expression:");
			Logger.log(url);
			System.exit(-1);
		}
	}

}
