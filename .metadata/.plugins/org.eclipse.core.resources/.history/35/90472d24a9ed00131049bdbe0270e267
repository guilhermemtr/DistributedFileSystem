package rmiRemoteFileServer;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import utils.Domains;
import utils.Logger;
import utils.RemoteUtils;
import contactServer.IContactServer;
import exceptions.FileAlreadyExistsException;
import exceptions.NoSuchFileException;
import exceptions.NoSuchPathException;
import fileUtils.Google2Api;
import fileUtils.GoogleDriveProxyFileSystem;

public class RmiRemoteGoogleDriveProxyFileServerMain {

	private static final String NETWORK_NAME = "Google";
    private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
    private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/drive";
    private static final Token EMPTY_TOKEN = null;
	
	public static void main(String[] args) throws ParseException, NoSuchPathException, FileAlreadyExistsException, NoSuchFileException {
		String contactAddress = null;
		if (args.length < 1) {
			System.err.println("Usage: <url>@<user>:<path>");
			System.exit(-1);
		}
		
        boolean startOver = true;
		
		String apiKey = "332198807015-8dqaaj920sd284a0gdk6q1ea6bmir8hi.apps.googleusercontent.com";
		String apiSecret = "7ITaliE1VCf0yCho3oyFO5sV";
		String callbackUrl = "urn:ietf:wg:oauth:2.0:oob";

		OAuthService service = new ServiceBuilder().provider(Google2Api.class).apiKey(apiKey).apiSecret(apiSecret)
				.callback(callbackUrl).scope(SCOPE).build();
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
		System.out.println();

		Verifier verifier = null;
		Token accessToken = new Token("ACCESS_TOKEN", "REFRESH_TOKEN");

		if (startOver)
		{
			// Obtain the Authorization URL
			System.out.println("Fetching the Authorization URL...");
			String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
			System.out.println("Got the Authorization URL!");
			System.out.println("Now go and authorize Scribe here:");
			System.out.println(authorizationUrl);
			System.out.println("And paste the authorization code here");
			System.out.print(">>");
			verifier = new Verifier(in.nextLine());
			System.out.println();

			// Trade the Request Token and Verfier for the Access Token
			System.out.println("Trading the Request Token for an Access Token...");
			accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
			System.out.println("Got the Access Token!");
			System.out.println("(if your curious it looks like this: " + accessToken + " )");
			System.out.println();
		}

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getCode());
		System.out.println(response.getBody());

		System.out.println();
		System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
		System.out.println();
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
			server = new RmiRemoteFileServer(new GoogleDriveProxyFileSystem(service, accessToken));
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