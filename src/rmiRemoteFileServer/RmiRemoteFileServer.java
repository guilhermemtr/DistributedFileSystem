package rmiRemoteFileServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import utils.Logger;
import exceptions.FileAlreadyExistsException;
import exceptions.NoSuchFileException;
import exceptions.NoSuchPathException;
import fileUtils.FileInfo;
import fileUtils.IFileSystem;

public class RmiRemoteFileServer extends UnicastRemoteObject implements
		IRmiRemoteFileServer {

	private static final long serialVersionUID = 6810600069166858611L;

	private IFileSystem fileSystem;

	public RmiRemoteFileServer(IFileSystem fs) throws RemoteException {
		super();
		this.fileSystem = fs;
		Logger.log("Building file server");
	}

	@Override
	public String[] ls(String path) throws NoSuchPathException {
		Logger.log("Request to list file in path" + path);
		return fileSystem.ls(path);
	}

	@Override
	public void mkDir(String dirName)
			throws FileAlreadyExistsException, NoSuchPathException {
		Logger.log("Creating a new dir in " + dirName);
		fileSystem.mkDir(dirName);
		
	}

	@Override
	public void rmDir(String dirName) throws NoSuchPathException {
		Logger.log("Removind a dir " + dirName);
		fileSystem.rmDir(dirName);
	}

	@Override
	public void rm(String filename) throws NoSuchFileException {
		Logger.log("Removing file " + filename);
		fileSystem.rm(filename);
	}

	@Override
	public FileInfo getAttr(String path)
			throws NoSuchFileException {
		Logger.log("Getting attributes for " + path);
		return fileSystem.getAttr(path);
	}

	@Override
	public byte[] getFile(String filename)
			throws NoSuchFileException, RemoteException {
		Logger.log("Getting file " + filename);
		return fileSystem.getFile(filename);
	}

	@Override
	public void putFile(String filename, byte[] file)
			throws FileAlreadyExistsException {
		Logger.log("Putting file " + filename);
		fileSystem.putFile(filename, file);
	}

	@Override
	public void alive() throws RemoteException {
		Logger.log("Someone is making ping to me :)");
	}

	@Override
	public Date getDate() throws RemoteException {
		Date currDate = new Date();
		Logger.log("Getting date " + currDate.toString());
		return currDate;
	}

}
