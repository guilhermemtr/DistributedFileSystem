package rmiRemoteFileServer;

import java.rmi.RemoteException;
import java.util.StringTokenizer;

import utils.Domains;
import utils.Logger;
import utils.RemoteUtils;
import contactServer.IContactServer;
import fileUtils.FileSystem;

public class RmiRemoteFileServerMain {

	public static void main(String[] args) {
		String contactAddress = null;
		if (args.length < 1) {
			System.err.println("Usage: <url>@<user>:<path>");
			System.exit(-1);
		}

		Logger.setupLogger(System.err, true);

		if (args.length == 2) {
			contactAddress = args[1];
			Logger.log("Server running on machine " + contactAddress);
		}
		StringTokenizer strTok = new StringTokenizer(args[0]);

		String serverName = strTok.nextToken(":");
		Logger.log("Creating a new file server with name: " + serverName);
		String path = strTok.nextToken(":");
		Logger.log("File server created on " + path);
		StringTokenizer userTok = new StringTokenizer(serverName);
		userTok.nextToken("@");
		String user = userTok.nextToken("@");
		Logger.log("Owner of file server is " + user);

		RemoteFileServerUtils.setupSecurityManager();

		RemoteUtils.createNodeRegistry();

		Logger.log("Getting the contact server...");

		String ipAddress = Domains.getIP();
		String url = Domains.buildRmiURL(serverName, ipAddress,
				RemoteUtils.RMIPORT);
		Logger.log("Server name:");
		Logger.log(serverName);
		Logger.log("Server url:");
		Logger.log(url);
		IRmiRemoteFileServer server = null;
		try {
			server = new RmiRemoteFileServer(new FileSystem(path));
		} catch (RemoteException e) {
			Logger.log("Couldn't create remote file server... exiting (-1)");
			System.exit(-1);
		}

		if (contactAddress == null)
			contactAddress = Domains.getContactServer();

		if (contactAddress == null) {
			Logger.log("No contact server available... exiting(-1)");
			System.exit(-1);
		}

		IContactServer contact = RemoteFileServerUtils.getContactServer(contactAddress);

		Logger.log("Launching file server...");
		Logger.log("Registrying file server...");
		RemoteFileServerUtils.exportFileServerDomain(url, server);
		RemoteFileServerUtils.registerFileServer(serverName, user, contact, url);

		RemoteFileServerUtils.keepAlive(server, contact, serverName, user, url);
	}

	

}
