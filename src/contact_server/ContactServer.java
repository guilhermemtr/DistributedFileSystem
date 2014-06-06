package contact_server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import utils.Logger;
import exceptions.NoPermissionsException;
import exceptions.NoSuchServerException;
import exceptions.ServerAlreadyExistsException;

public class ContactServer extends UnicastRemoteObject implements
		IContactServer {

	private static final long serialVersionUID = 8610003430792653827L;
	
	private IAccessControlManager accessControl;
	private INameResolver dnsSystem;

	private String url;

	public ContactServer(String url) throws RemoteException {
		super();
		this.accessControl = new AccessControlManager();
		this.dnsSystem = new NameResolver();
		this.url = url;
		Logger.log("Building Contact server");
	}
	
	@Override
	public void registerFileServer(String serverName, String url, String user)
			throws RemoteException, ServerAlreadyExistsException {
		Logger.log("Registring file server " + serverName + " in url " + url);
		dnsSystem.registerServer(serverName, url);
		accessControl.addServer(serverName, url, user);
	}

	@Override
	public boolean checkPermissions(String serverName, String user)
			throws RemoteException, NoSuchServerException {
		Logger.log("Checking permissions of " + user + " to access server " + serverName);
		return accessControl.hasAccess(serverName, user);
	}

	@Override
	public String[] getServers(String user) throws RemoteException {
		return accessControl.getServers(user);
	}

	@Override
	public String getFileServer(String user, String serverName)
			throws RemoteException, NoPermissionsException, NoSuchServerException {
		if (!accessControl.hasAccess(serverName, user)) {
			return null;
		}
		return dnsSystem.getUrl(serverName);
	}

	@Override
	public void addPermissions(String user, String newUser, String server)
			throws RemoteException, NoPermissionsException, NoSuchServerException {
		accessControl.grant(server, getFileServer(user,server), user, newUser);
	}

	@Override
	public void removePermissions(String user, String oldUser, String server)
			throws RemoteException, NoPermissionsException, NoSuchServerException {
		accessControl.revoke(server, user, oldUser);
	}

	@Override
	public String getURL() throws RemoteException {
		return this.url;
	}

	@Override
	public void alive() throws RemoteException {
		
	}

}
