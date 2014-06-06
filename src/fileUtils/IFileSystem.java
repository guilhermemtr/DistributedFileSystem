package fileUtils;

import java.io.Serializable;
import exceptions.*;

public interface IFileSystem extends Serializable {

	/**
	 * Lists the directory of the path.
	 * @param path
	 * @return
	 * @throws NoSuchPathException
	 */
	String[] ls(String path) throws NoSuchPathException;

	/**
	 * Makes a directory
	 * @param dirName
	 * @throws FileAlreadyExistsException
	 * @throws NoSuchPathException 
	 */
	void mkDir(String dirName) throws FileAlreadyExistsException, NoSuchPathException;

	/**
	 * Removes a directory
	 * @param dirName
	 * @throws NoSuchPathException
	 */
	void rmDir(String dirName) throws NoSuchPathException;
	
	/**
	 * Removes a file
	 * @param filename
	 * @throws NoSuchFileException
	 */
	void rm(String filename) throws NoSuchFileException;
	
	/**
	 * Gets the attributes of a file
	 * @param path
	 * @return
	 * @throws NoSuchFileException
	 */
	FileInfo getAttr(String path) throws NoSuchFileException;
	
	/**
	 * Gets a file's contents.
	 * @param filename
	 * @return
	 * @throws NoSuchFileException
	 */
	byte[] getFile(String filename) throws NoSuchFileException;
	
	/**
	 * Saves a new file, given its contents.
	 * @param filename
	 * @param file
	 * @throws FileAlreadyExistsException
	 */
	void putFile(String filename, byte[] file) throws FileAlreadyExistsException;
}
