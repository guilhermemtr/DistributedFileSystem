package contactServer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import exceptions.NoPermissionsException;
import exceptions.NoSuchServerException;
import exceptions.ServerAlreadyExistsException;

public interface IContactServer extends Remote, Serializable {

	void registerFileServer(String serverName, String url, String user)
			throws RemoteException, ServerAlreadyExistsException;

	String[] getServers(String user) throws RemoteException;

	String getFileServer(String user, String serverName)
			throws RemoteException, NoPermissionsException, NoSuchServerException;

	void addPermissions(String user, String newUser, String server)
			throws RemoteException, NoPermissionsException, NoSuchServerException;

	void removePermissions(String user, String oldUser, String server)
			throws RemoteException, NoPermissionsException, NoSuchServerException;

	boolean checkPermissions(String serverName, String user)
			throws RemoteException, NoSuchServerException;

	String getURL() throws RemoteException;
	
	void alive() throws RemoteException;
}
