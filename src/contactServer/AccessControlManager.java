package contactServer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import utils.Logger;
import exceptions.NoPermissionsException;
import exceptions.NoSuchServerException;
import exceptions.ServerAlreadyExistsException;

public class AccessControlManager implements IAccessControlManager {

	private static final String NOSUCHSERVER = "No server named ";
	private static final String NOPERMISSIONS = " does not have permissions to access ";

	// The userpermissions
	// The key is the username
	// The value is a map of the servers it has access to.
	private Map<String, Map<String, String>> userPermissions;
	
	//The owners of each of the file servers.
	private Map<String, Map<String,String>> owners;
	
	//The servers (The value map are the users with permissions to access each of them).
	private Map<String, Map<String, String>> servers;

	public AccessControlManager() {
		this.userPermissions = new ConcurrentHashMap<String, Map<String, String>>();
		this.owners = new ConcurrentHashMap<String, Map<String, String>>();
		this.servers = new ConcurrentHashMap<String, Map<String, String>>();
	}

	// Creates a user (if it does not exist yet)
	@Override
	public void addUser(String username) {
		Logger.log("Trying to add user " + username);
		((ConcurrentHashMap<String, Map<String, String>>) userPermissions)
				.putIfAbsent(username, new ConcurrentHashMap<String, String>());
	}
	
	@Override
	public void addOwner(String ownerName) {
		Logger.log("Trying to add owner " + ownerName);
		((ConcurrentHashMap<String, Map<String, String>>) owners)
				.putIfAbsent(ownerName, new ConcurrentHashMap<String, String>());
	}

	@Override
	public void addServer(String server, String url, String owner)
			throws ServerAlreadyExistsException {
		Logger.log("Trying to add " + server);
		
		this.addUser(owner);
		this.addOwner(owner);
		
		servers.put(server, new ConcurrentHashMap<String, String>());
		servers.get(server).put(owner, owner);
		
		userPermissions.get(owner).put(server, url);
		
		owners.get(owner).put(server, url);
		
		Logger.log("Registering " + server + " assigned to " + url);
		assert (hasAccess(server, owner));
		Logger.log(owner + " has permissions to access " + server);
	}

	@Override
	public void grant(String server, String url, String user, String newUser)
			throws NoSuchServerException, NoPermissionsException {
		this.addUser(newUser);
		Map<String, String> authorized = servers.get(server);

		if (authorized == null) {
			Logger.log("Couldnt grant permissions to " + newUser);
			Logger.log(NOSUCHSERVER + server);
			throw new NoSuchServerException(NOSUCHSERVER + server);
		}

		if (!authorized.containsKey(user)) {
			Logger.log("Couldnt grant permissions to " + newUser);
			Logger.log(user + NOPERMISSIONS + server);
			throw new NoPermissionsException(user + NOPERMISSIONS + server);
		}

		this.addUser(newUser);

		authorized.put(newUser, newUser);

		this.userPermissions.get(newUser).put(server, url);
		
		Logger.log("Granting permissions to " + newUser + " to access "
				+ server + ". Permissions given by " + user);
	}

	@Override
	public void revoke(String server, String user, String oldUser)
			throws NoPermissionsException {
		if(user == oldUser) {
			throw new NoPermissionsException(user + " cant remove itself");
		}
		
		Map<String,String> owned = owners.get(oldUser);
		if(owned != null) {
			if(owned.containsKey(server)) {
				throw new NoPermissionsException("Cant revoke permissions of the filesystem's owner");
			}
		}
		
		Map<String, String> authorized = servers.get(server);
		if (!authorized.containsKey(user))
			throw new NoPermissionsException(user + NOPERMISSIONS + server);
		authorized.remove(oldUser);
		Map<String, String> userServers = userPermissions.get(oldUser);
		if (userServers != null) {
			Logger.log("Revoking permissions for user " + oldUser);
			userServers.remove(server);
		}
	}

	@Override
	public boolean hasAccess(String server, String user) {
		Logger.log("Checking permissions for user: " + user + " on server "
				+ server);
		Map<String, String> servers = userPermissions.get(user);
		if (servers == null)
			return false;
		return servers.containsKey(server);
	}

	@Override
	public String[] getServers(String user) {
		Logger.log("Getting authorized servers for user " + user);
		Map<String, String> servers = userPermissions.get(user);
		if (servers == null)
			return null;

		Set<String> keys = servers.keySet();
		String[] availableServers = new String[keys.size()];
		int i = 0;
		for (String k : keys) {
			availableServers[i++] = k;
		}
		return availableServers;
	}

}
