package client;
import contact_server.IContactServer;
import exceptions.FileAlreadyExistsException;
import exceptions.NoPermissionsException;
import exceptions.NoSuchFileException;
import exceptions.NoSuchPathException;
import exceptions.ServerOfflineException;
import file_utils.FileInfo;

public interface IRemoteFileSystemClient {

	/**
	 * Sets up the contact server
	 * 
	 * @param contact
	 */
	void setupContactServer(IContactServer contact);

	/**
	 * Updates the contact server.
	 */
	void updateContactServer();

	/**
	 * Devolve um array com os servidores a que o utilizador tem acesso.
	 * 
	 * @return <code>String[]</code> the online servers array.
	 * @throws ServerOfflineException
	 *             if it couldn't get the info.
	 */
	String[] servers() throws ServerOfflineException;

	/**
	 * Adiciona o utilizador user a lista de utilizadores com autorizaçao para
	 * aceder ao servidor server
	 * 
	 * @param server
	 *            <code>String</code>- the server
	 * @param user
	 *            <code>String</code>- the user
	 * @return <code>Boolean</code> <code>False</code> in case of error.
	 *         <code>True</code> otherwise.
	 * @throws ServerOfflineException
	 *             if the connection was not possible.
	 */
	boolean addPermission(String server, String user)
			throws ServerOfflineException;

	/**
	 * Remove o utilizador user da lista de utilizadores com autorizacao para
	 * aceder ao servidor server.
	 * 
	 * @param server
	 *            <code>String</code>- the server
	 * @param user
	 *            <code>String</code>- the user
	 * @return <code>Boolean</code> <code>False</code> in case of error.
	 *         <code>True</code> otherwise.
	 * @throws ServerOfflineException
	 *             if the connection was not possible.
	 */
	boolean remPermission(String server, String user)
			throws ServerOfflineException;

	/**
	 * Devolve um array com os ficheiros/directoria na directoria dir no
	 * servidor server@user (ou no sistema de ficheiros do cliente caso server
	 * == null).
	 * 
	 * @param server
	 *            <code>String</code> - the server
	 * @param user
	 *            <code>String</code> - the user
	 * @param dir
	 *            <code>String</code> - the directory in which files should be
	 *            listed.
	 * @return <code>String[]</code> the ls output.
	 * @throws ServerOfflineException
	 *             if the connection was not possible.
	 * @throws NoSuchPathException
	 *             if there is no such path.
	 * @throws NoPermissionsException
	 *             if the user does not have permissions in the server.
	 */
	String[] dir(String server, String user, String dir)
			throws ServerOfflineException, NoPermissionsException,
			NoSuchPathException;

	/**
	 * Synchronizes a local path with a remote file server's path.
	 * 
	 * @param localPath
	 *            the local path to be synced.
	 * @param fileServer
	 *            the remote file server
	 * @param fileServerOwner
	 *            the remote file server owner.
	 * @param remotePath
	 *            the remote file server path.
	 * @return 
	 * @throws ServerOfflineException
	 *             if the remote server is offline.
	 * @throws NoPermissionsException
	 *             if the user does not have permissions.
	 * @throws NoSuchPathException
	 *             if there is no such local path, or no such remote path.
	 */
	void sync(String localPath, String fileServer, String fileServerOwner,
			String remotePath) throws ServerOfflineException,
			NoPermissionsException, NoSuchPathException;

	/**
	 * Cria a directoria dir no servidor server@user (ou no sistema de ficheiros
	 * do cliente caso server == null).
	 * 
	 * @param server
	 *            <code>String</code>- the server
	 * @param user
	 *            <code>String</code>- the user
	 * @param dir
	 *            <code>String</code> - the directory that should be created.
	 * @return <code>String[]</code> the ls output.
	 * @throws ServerOfflineException
	 *             if the connection was not possible.
	 * @throws FileAlreadyExistsException
	 *             if there is already a file with the same name.
	 * @throws NoPermissionsException
	 *             if the user does not have permissions in the server.
	 * @throws NoSuchPathException
	 */
	boolean mkdir(String server, String user, String dir)
			throws ServerOfflineException, NoPermissionsException,
			FileAlreadyExistsException, NoSuchPathException;

	/**
	 * Remove a directoria dir no servidor server@user (ou no sistema de
	 * ficheiros do cliente caso server == null).
	 * 
	 * @param server
	 *            <code>String</code>- the server
	 * @param user
	 *            <code>String</code>- the user
	 * @param dir
	 *            <code>String</code> - the directory that should be removed.
	 * @return <code>Boolean</code> <code>False</code> in case of error.
	 *         <code>True</code> otherwise.
	 * @throws ServerOfflineException
	 *             if the connection was not possible.
	 * @throws NoSuchPathException
	 *             if there is no such path.
	 * @throws NoPermissionsException
	 *             if the user does not have permissions in the server.
	 */
	boolean rmdir(String server, String user, String dir)
			throws ServerOfflineException, NoPermissionsException,
			NoSuchPathException;

	/**
	 * Remove o ficheiro path no servidor server@user. (ou no sistema de
	 * ficheiros do cliente caso server == null).
	 * 
	 * @param server
	 *            <code>String</code>- the server.
	 * @param user
	 *            <code>String</code>- the user.
	 * @param path
	 *            <code>String</code> - the directory that should be removed.
	 * @return <code>Boolean</code> <code>False</code> in case of error.
	 *         <code>True</code> otherwise.
	 * @throws ServerOfflineException
	 *             if the connection was not possible.
	 * @throws NoSuchFileException
	 *             if there is no such file.
	 * @throws NoPermissionsException
	 *             if the user does not have permissions in the server.
	 */
	boolean rm(String server, String user, String path)
			throws ServerOfflineException, NoPermissionsException,
			NoSuchFileException;

	/**
	 * Devolve informacao sobre o ficheiro/directoria path no servidor
	 * server@user. (ou no sistema de ficheiros do cliente caso server == null).
	 * 
	 * @param server
	 *            <code>String</code>- the server.
	 * @param user
	 *            <code>String</code>- the user.
	 * @param path
	 *            <code>String</code>- the directory that should be removed.
	 * @return <code>String</code>- the file attributes.
	 * @throws ServerOfflineException
	 *             if the connection was not possible.
	 * @throws NoSuchFileException
	 *             if there is no such file.
	 * @throws NoPermissionsException
	 *             if the user does not have permissions in the server.
	 * @throws NoSuchPathException
	 */
	FileInfo getAttr(String server, String user, String path)
			throws ServerOfflineException, NoPermissionsException,
			NoSuchFileException, NoSuchPathException;

	/**
	 * Copia ficheiro de fromPath no servidor fromServer@fromUser para o
	 * ficheiro toPath no servidor toServer@toUser. (caso fromServer/toServer ==
	 * local, corresponde ao sistema de ficheiros do cliente). TODO
	 * 
	 * @param server
	 *            <code>String</code>- the server
	 * @param user
	 *            <code>String</code>- the user
	 * @param dir
	 *            <code>String</code> - the directory in which files should be
	 *            listed.
	 * @param fromServer
	 *            <code>String</code>- the server from which the file will be
	 *            copied.
	 * @param fromUser
	 *            <code>String</code>- the from Server's user.
	 * @param fromPath
	 *            <code>String</code>- the directory in which files should be
	 *            listed.
	 * @param toServer
	 *            <code>String</code>- the server to which the file should be
	 *            copied.
	 * @param toUser
	 *            <code>String</code>- the user to which the file will be copied
	 *            in the remote server.
	 * @param toPath
	 *            <code>String</code>- the path to which the file should be
	 *            copied.
	 * @return <code>Boolean</code>- <code>False</code> if there was an error.
	 *         <code>True</code> otherwise.
	 * @throws ServerOfflineException
	 *             if the connection was not possible.
	 * @throws FileAlreadyExistsException
	 *             if the file already exists.
	 * @throws NoSuchFileException
	 *             if there is no such file.
	 * @throws NoPermissionsException
	 *             if the user does not have permissions in the server.
	 */
	boolean cp(String fromServer, String fromUser, String fromPath,
			String toServer, String toUser, String toPath)
			throws ServerOfflineException, NoPermissionsException,
			NoSuchFileException, FileAlreadyExistsException;
}
