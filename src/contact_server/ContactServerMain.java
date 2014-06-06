package contact_server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import utils.Domains;
import utils.Logger;
import utils.RemoteUtils;

public class ContactServerMain {
	
	public static void main(String args[]) {
		String contactServerName = Domains.CONTACTSERVER;
		if(args.length == 1) {
			contactServerName = args[0];
		}
		
		Logger.setupLogger(System.err, true);
		
		Logger.log("Contact server name: " + contactServerName);
		
		Logger.log("Setting new security policy");
		System.getProperties().put("java.security.policy",
				"policy.all");

		if (System.getSecurityManager() == null) {
			Logger.log("Setting new security manager");
			System.setSecurityManager(new RMISecurityManager());
		}

		RemoteUtils.createNodeRegistry();

		String url = "/" + contactServerName;
		Logger.log("Contact server local url is: " + url);
		
		
		IContactServer contact = null;
		try {
			contact = new ContactServer(url);
		} catch (RemoteException e) {
			Logger.log(e.getMessage());
			return;
		}
		
		try {
			Naming.rebind(url, contact);
			Logger.log("Contact server URL:" + url);
		} catch (RemoteException e) {
			Logger.log(e.getMessage());
		} catch (MalformedURLException e) {
			Logger.log(e.getMessage());
		}
		
		ClientRequestHandler.handleClientRequests(contactServerName);

	}

}
