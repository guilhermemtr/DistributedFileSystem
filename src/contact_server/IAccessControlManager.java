package contact_server;

import exceptions.NoPermissionsException;
import exceptions.NoSuchServerException;
import exceptions.ServerAlreadyExistsException;

public interface IAccessControlManager {

	void addUser(String username);
	
	void addOwner(String ownerName);

	void addServer(String server, String url, String user) throws ServerAlreadyExistsException;

	void grant(String server, String url, String user, String newUser) throws NoSuchServerException, NoPermissionsException;

	void revoke(String server, String user, String oldUser) throws NoSuchServerException, NoPermissionsException;

	boolean hasAccess(String server, String user) throws NoSuchServerException;
	
	String[] getServers(String user);
}
