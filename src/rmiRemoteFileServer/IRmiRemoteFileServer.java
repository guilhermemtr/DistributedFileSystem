package rmiRemoteFileServer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

import exceptions.FileAlreadyExistsException;
import exceptions.NoSuchFileException;
import exceptions.NoSuchPathException;
import fileUtils.FileInfo;

public interface IRmiRemoteFileServer extends Remote, Serializable {

	//lists the directories in a path.
	String[] ls(String path) throws RemoteException, NoSuchPathException;

	//makes a directory
	void mkDir(String dirName) throws RemoteException, FileAlreadyExistsException, NoSuchPathException;

	//removes a directory
	void rmDir(String dirName) throws RemoteException, NoSuchPathException;
	
	//removes a file
	void rm(String filename) throws RemoteException, NoSuchFileException;
	
	//Gets the file information of a file
	FileInfo getAttr(String path) throws RemoteException, NoSuchFileException;
	
	//Gets a remote reference to a file data
	byte[] getFile(String filename) throws RemoteException, NoSuchFileException;
	
	//Puts a file in its filesystem, given its filename and filedata
	void putFile(String filename, byte[] file) throws RemoteException, FileAlreadyExistsException;
	
	//checks if the remote file server is alive.
	void alive() throws RemoteException;
	
	//Gets the date of the server
	Date getDate() throws RemoteException;
}
