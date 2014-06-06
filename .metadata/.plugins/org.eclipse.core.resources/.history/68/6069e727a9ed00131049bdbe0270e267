package wsRemoteFileServer;

import javax.jws.WebMethod;
import javax.jws.WebService;

import exceptions.FileAlreadyExistsException;
import exceptions.NoSuchFileException;
import exceptions.NoSuchPathException;
import fileUtils.FileInfo;
import fileUtils.FileSystem;
import fileUtils.IFileSystem;

@WebService
public class SoapRemoteFileServer {

	public static final String NOUSER = "Invalid user ";

	private IFileSystem fileSystem;

	public SoapRemoteFileServer() {
		this(".");
	}
	
	public SoapRemoteFileServer(String path) {
		super();
		this.fileSystem = new FileSystem(path);
	}

	@WebMethod
	public String[] ls(String path) throws NoSuchPathException {
		return fileSystem.ls(path);
	}

	@WebMethod
	public void mkDir(String dirName) throws FileAlreadyExistsException,
			NoSuchPathException {
		fileSystem.mkDir(dirName);

	}

	@WebMethod
	public void rmDir(String dirName) throws NoSuchPathException {
		fileSystem.rmDir(dirName);
	}

	@WebMethod
	public void rm(String filename) throws NoSuchFileException {
		fileSystem.rm(filename);
	}

	@WebMethod
	public FileInfo getAttr(String path) throws NoSuchFileException {
		return fileSystem.getAttr(path);
	}

	@WebMethod
	public byte[] getFile(String filename) throws NoSuchFileException {
		return fileSystem.getFile(filename);
	}

	@WebMethod
	public void putFile(String filename, byte[] file)
			throws FileAlreadyExistsException {
		fileSystem.putFile(filename, file);
	}

	@WebMethod
	public
	void alive() {
	}

}
