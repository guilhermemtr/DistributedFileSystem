package file_server;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DropBoxApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import utils.Domains;
import utils.Logger;
import utils.RemoteUtils;
import contact_server.IContactServer;
import file_systems.DropboxProxyFileSystem;

public class RmiRemoteDropboxProxyFileServerMain {

	private static final String API_KEY = "tm6p9mkvph9dgge";
	private static final String API_SECRET = "z20wh6fd88cm80n";
	private static final String SCOPE = "dropbox";
	private static final String AUTHORIZE_URL = "https://www.dropbox.com/1/oauth/authorize?oauth_token=";
	
	public static void main(String[] args) {
		String contactAddress = null;
		if (args.length < 1) {
			System.err.println("Usage: <url>@<user>:<path>");
			System.exit(-1);
		}

		OAuthService service = new ServiceBuilder().provider(DropBoxApi.class)
				.apiKey(API_KEY).apiSecret(API_SECRET).scope(SCOPE).build();
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		// Obter Request token
		Token requestToken = service.getRequestToken();
		System.out
				.println("Tem de obter autorizacao para a aplicacao continuar acedendo ao link:");
		System.out.println(AUTHORIZE_URL + requestToken.getToken());
		System.out.println("E carregar em enter quando der autorizacao");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());

		// O Dropbox usa como verifier o mesmo segredo do request token, ao
		// contrario de outros
		// sistemas, que usam um codigo fornecido na pagina web
		// Com esses sistemas a linha abaixo esta a mais
		verifier = new Verifier(requestToken.getSecret());

		// Obter access token
		Token accessToken = service.getAccessToken(requestToken, verifier);

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
			server = new RmiRemoteFileServer(new DropboxProxyFileSystem(service, accessToken));
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
