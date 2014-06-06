package wsRemoteFileServer;

import java.util.StringTokenizer;

import rmiRemoteFileServer.RemoteFileServerUtils;
import utils.Domains;
import utils.Logger;
import contactServer.IContactServer;

public class SoapRemoteFileServerMain {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: <url>@<user>:<path>");
			System.exit(-1);
		}
		
		Logger.setupLogger(System.err, true);
		
		StringTokenizer strTok = new StringTokenizer(args[0]);

		String serverName = strTok.nextToken(":");
		String path = strTok.nextToken(":");

		StringTokenizer userTok = new StringTokenizer(serverName);
		userTok.nextToken("@");
		String user = userTok.nextToken("@");

		String ipAddress = Domains.getIP();

		Logger.log("Getting the contact server...");

		
		IContactServer contact = null;

		SoapRemoteFileServer server = new SoapRemoteFileServer(path);

		String url = "http://" + ipAddress + ":8080/" + serverName;

		Logger.log("Launching file server...");
		
		RemoteFileServerUtils.exportFileServerDomain(url, server);

		RemoteFileServerUtils.keepAlive(server, contact, serverName, user, url);
		
		
	}
}
