package fileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import utils.Logger;

import exceptions.FileAlreadyExistsException;
import exceptions.NoSuchFileException;
import exceptions.NoSuchPathException;

public class FileSystem implements IFileSystem {

	public static final String NODIR = "Directory does not exist.";
	public static final String ALREADYEXISTSDIR = "Directory already exists.";
	public static final String[] FILEALREADYEXISTS = { "File ",
			" already exists." };
	public static final String NOSUCHFILENAME = "File not found ";
	public static final String PROBLEMWITHFILE = "Problem handling ";
	public static final String READ = "r";
	public static final String WRITE = "rw";

	private static final long serialVersionUID = 6810600069166858611L;

	private String path;

	public FileSystem(String path) {
		super();
		this.path = path;
		if(!this.path.endsWith("/"))
			this.path = this.path + "/";
		Logger.log("Building up file system in " + path);
	}

	@Override
	public String[] ls(String path) throws NoSuchPathException {
		File f = new File(this.path + path);
		if (!f.exists()) {
			Logger.log(NODIR + " " + path);
			throw new NoSuchPathException(NODIR);
		}
		return f.list();
	}

	@Override
	public void mkDir(String path) throws FileAlreadyExistsException, NoSuchPathException {
		File f = new File(this.path);
		if (!f.exists()) {
			Logger.log(ALREADYEXISTSDIR + " " + path);
			throw new NoSuchPathException(ALREADYEXISTSDIR);
		}
		File newDirectory = new File(this.path + path);
		if (newDirectory.exists()) {
			Logger.log(ALREADYEXISTSDIR + " " + path);
			throw new FileAlreadyExistsException(ALREADYEXISTSDIR);
		}
		newDirectory.mkdir();
	}

	@Override
	public void rmDir(String path) throws NoSuchPathException {
		File f = new File(this.path + path);
		if (!f.exists() || !f.isDirectory()) {
			Logger.log(NODIR + path);
			throw new NoSuchPathException(NODIR);
		}
		f.delete();
	}

	@Override
	public void rm(String filename) throws NoSuchFileException {
		File f = new File(this.path + filename);
		if (!f.exists() || f.isDirectory()) {
			Logger.log(NOSUCHFILENAME + filename);
			throw new NoSuchFileException(NOSUCHFILENAME + filename);
		}
		f.delete();
	}

	@Override
	public FileInfo getAttr(String path) throws NoSuchFileException {
		File f = new File(this.path + path);
		if (f.exists())
			return new FileInfo(path, f.length(), new Date(f.lastModified()),
					f.isFile());
		else {
			Logger.log(NOSUCHFILENAME + path);
			throw new NoSuchFileException(NOSUCHFILENAME + path);
		}
	}

	@Override
	public byte[] getFile(String filename) throws NoSuchFileException {
		try {
			RandomAccessFile f = new RandomAccessFile(path + filename, READ);
			byte[] buf = new byte[(int) f.length()];
			f.readFully(buf);
			f.close();
			Logger.log("Got file " + filename);
			return buf;
		} catch (FileNotFoundException e) {
			Logger.log(NOSUCHFILENAME + filename);
			throw new NoSuchFileException(NOSUCHFILENAME + filename);
		} catch (IOException e) {
			Logger.log(PROBLEMWITHFILE + filename);
			throw new NoSuchFileException(PROBLEMWITHFILE + filename);
		}
	}

	@Override
	public void putFile(String filename, byte[] fileData)
			throws FileAlreadyExistsException {
		File file = new File(this.path , filename);
		if (file.exists()) {
			Logger.log(FILEALREADYEXISTS[0]
					+ filename + FILEALREADYEXISTS[1]);
			throw new FileAlreadyExistsException(FILEALREADYEXISTS[0]
					+ filename + FILEALREADYEXISTS[1]);
		}
		try {
			RandomAccessFile f = new RandomAccessFile(file, WRITE);
			f.write(fileData);
			f.close();
			Logger.log("Saving" + path + filename + " in the file system");
		} catch (IOException e) {
			Logger.log(PROBLEMWITHFILE + filename);
			throw new FileAlreadyExistsException(PROBLEMWITHFILE + filename);
		}
	}
}
