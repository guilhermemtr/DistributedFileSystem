import java.io.IOException;
import java.util.Scanner;

import utils.Domains;
import utils.Logger;
import exceptions.FileAlreadyExistsException;
import exceptions.NoPermissionsException;
import exceptions.NoSuchFileException;
import exceptions.NoSuchPathException;
import exceptions.ServerOfflineException;

/**
 * Classe base do cliente
 * 
 * @author nmp
 */
public class FileClient {

	public static final String SUCCESS = "Success";
	public static final String ERROR = "Error.";
	public static final String INVALIDARGS = "Invalid Arguments";
	public static final String SERVEROFFLINE = "Failed to contact server.";

	public enum OPTIONS {
		SERVERS, ADDPERM, RMPERM, LS, MKDIR, RMDIR, CP, RM, SYNC, INFO, HELP, EXIT, QUIT, Q, CLEAR, WHO;
	}

	private String username;
	private IRemoteFileSystemClient serverConnection;

	protected FileClient(String username, String contactServer) {
		this.username = username;
		this.serverConnection = new RemoteFileSystemClient(username,
				contactServer);
	}

	/**
	 * Devolve um array com os servidores a que o utilizador tem acesso.
	 */
	protected String[] servers() {
		try {
			return this.serverConnection.servers();
		} catch (ServerOfflineException e) {
			System.out.println(SERVEROFFLINE);
			return null;
		}
	}

	/**
	 * Adiciona o utilizador user a lista de utilizadores com autorizaçao para
	 * aceder ao servidor server. Devolve false em caso de erro. NOTA: nao deve
	 * lancar excepcao.
	 */
	protected boolean addPermission(String server, String user) {
		try {
			return this.serverConnection.addPermission(server, user);
		} catch (ServerOfflineException e) {
			System.out.println(SERVEROFFLINE);
			return false;
		}
	}

	/**
	 * Remove o utilizador user da lista de utilizadores com autorizacao para
	 * aceder ao servidor server. Devolve false em caso de erro. NOTA: nao deve
	 * lancar excepcao.
	 */
	protected boolean remPermission(String server, String user) {
		try {
			return this.serverConnection.remPermission(server, user);
		} catch (ServerOfflineException e) {
			System.out.println(SERVEROFFLINE);
			return false;
		}
	}

	/**
	 * Synchronizes a local folder with a remote folder.
	 * 
	 * @param dir
	 *            the local directory to be synched.
	 * @param server
	 *            the remote file server.
	 * @param user
	 *            the owner of the file server.
	 * @param path
	 *            the path of the folder to be synced in the remote file server.
	 * @return a <code>boolean</code> if the folder was or not synced.
	 */
	private boolean sync(String localPath, String fileServer,
			String fileServerOwner, String remotePath) {
		try {
			this.serverConnection.sync(localPath, fileServer, fileServerOwner,
					remotePath);
		} catch (ServerOfflineException e) {
			System.out.println(SERVEROFFLINE);
			return false;
		} catch (NoPermissionsException e) {
			System.out.println(e.getLocalizedMessage());
			return false;
		} catch (NoSuchPathException e) {
			System.out.println(e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	/**
	 * Devolve um array com os ficheiros/directoria na directoria dir no
	 * servidor server@user (ou no sistema de ficheiros do cliente caso server
	 * == null). Devolve null em caso de erro. NOTA: nao deve lancar excepcao.
	 */
	protected String[] dir(String server, String user, String dir) {
		try {
			return this.serverConnection.dir(server, user, dir);
		} catch (NoPermissionsException e) {
			System.out.println(e.getLocalizedMessage());
			return null;
		} catch (NoSuchPathException e) {
			System.out.println(e.getLocalizedMessage());
			return null;
		} catch (ServerOfflineException e) {
			System.out.println(SERVEROFFLINE);
			return null;
		}
	}

	/**
	 * Cria a directoria dir no servidor server@user (ou no sistema de ficheiros
	 * do cliente caso server == null). Devolve false em caso de erro. NOTA: nao
	 * deve lancar excepcao.
	 */
	protected boolean mkdir(String server, String user, String dir) {
		try {
			this.serverConnection.mkdir(server, user, dir);
			return true;
		} catch (NoSuchPathException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (NoPermissionsException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (FileAlreadyExistsException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (ServerOfflineException e) {
			System.out.println(SERVEROFFLINE);
		}
		return false;

	}

	/**
	 * Remove a directoria dir no servidor server@user (ou no sistema de
	 * ficheiros do cliente caso server == null). Devolve false em caso de erro.
	 * NOTA: nao deve lancar excepcao.
	 */
	protected boolean rmdir(String server, String user, String dir) {
		try {
			this.serverConnection.rmdir(server, user, dir);
			return true;
		} catch (NoPermissionsException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (NoSuchPathException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (ServerOfflineException e) {
			System.out.println(SERVEROFFLINE);
		}
		return false;
	}

	/**
	 * Remove o ficheiro path no servidor server@user. (ou no sistema de
	 * ficheiros do cliente caso server == null). Devolve false em caso de erro.
	 * NOTA: nao deve lancar excepcao.
	 */
	protected boolean rm(String server, String user, String path) {
		try {
			this.serverConnection.rm(server, user, path);
			return true;
		} catch (NoPermissionsException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (NoSuchFileException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (ServerOfflineException e) {
			System.out.println(SERVEROFFLINE);
		}
		return false;
	}

	/**
	 * Devolve informacao sobre o ficheiro/directoria path no servidor
	 * server@user. (ou no sistema de ficheiros do cliente caso server == null).
	 * Devolve false em caso de erro. NOTA: nao deve lancar excepcao.
	 */
	protected String getAttr(String server, String user, String path) {
		try {
			return this.serverConnection.getAttr(server, user, path).toString();
		} catch (NoPermissionsException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (NoSuchFileException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (ServerOfflineException e) {
			System.out.println(SERVEROFFLINE);
		} catch (NoSuchPathException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Copia ficheiro de fromPath no servidor fromServer@fromUser para o
	 * ficheiro toPath no servidor toServer@toUser. (caso fromServer/toServer ==
	 * local, corresponde ao sistema de ficheiros do cliente). Devolve false em
	 * caso de erro. NOTA: nao deve lancar excepcao.
	 */
	protected boolean cp(String fromServer, String fromUser, String fromPath,
			String toServer, String toUser, String toPath) {
		System.err.println("exec: cp " + fromPath + " no servidor "
				+ fromServer + "@" + fromUser + " para " + toPath
				+ " no servidor " + toServer + "@" + toUser);
		try {
			this.serverConnection.cp(fromServer, fromUser, fromPath, toServer,
					toUser, toPath);
			return true;
		} catch (NoPermissionsException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (NoSuchFileException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (FileAlreadyExistsException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (ServerOfflineException e) {
			System.out.println(SERVEROFFLINE);
		}
		return false;
	}

	@SuppressWarnings("resource")
	protected void doit() throws IOException {
		System.out.println("Your session won't be recorded, We are not NSA.");
		Scanner in = new Scanner(System.in);
		String ipAddress = Domains.getIP();
		for (;;) {
			System.out.print("[" + ipAddress + "@" + this.username + "~]$ ");
			String line = in.nextLine();
			if (line == null)
				break;
			String[] cmd = line.split(" ");
			OPTIONS opt = null;
			try {
				opt = OPTIONS.valueOf(cmd[0].toUpperCase());
			} catch (IllegalArgumentException e) {
				if (!line.isEmpty())
					System.out.println("Illegal operation token.");
				continue;
			}
			switch (opt) {
			case SERVERS:
				listServers();
				break;
			case ADDPERM:
				addPermission(cmd);
				break;
			case RMPERM:
				rmPermission(cmd);
				break;
			case LS:
				listDirectory(cmd);
				break;
			case MKDIR:
				makeDirectory(cmd);
				break;
			case RMDIR:
				removeDirectory(cmd);
				break;
			case CP:
				copyFile(cmd);
				break;
			case RM:
				removeFile(cmd);
				break;
			case SYNC:
				sync(cmd);
				break;
			case INFO:
				getFileAttributes(cmd);
				break;
			case HELP:
				help();
				break;
			case CLEAR:
				clear();
				break;
			case WHO:
				who();
				break;
			case EXIT:
			case QUIT:
			case Q:
				System.out.println("Good bye " + username + "!");
				System.exit(0);
				break;
			default:
				System.err.println("Unknown command: " + cmd[0]);
				break;
			}
		}
	}

	private void who() {
		System.out.println(this.username);
	}

	private void clear() {
		System.out.print("\033[2J");
	}

	private void help() {
		System.out
				.println("servers - lista URLs dos servidores a que tem acesso");
		System.out
				.println("addperm server user - adiciona user a lista de utilizadores com permissoes para aceder a server");
		System.out
				.println("rmperm server user - remove user da lista de utilizadores com permissoes para aceder a server");
		System.out
				.println("ls server@user:dir - lista ficheiros/directorias presentes na directoria dir (. e .. tem o significado habitual), caso existam ficheiros com o mesmo nome devem ser apresentados como nome@server;");
		System.out
				.println("mkdir server@user:dir - cria a directoria dir no servidor server@user");
		System.out
				.println("rmdir server@user:dir - remove a directoria dir no servidor server@user");
		System.out
				.println("cp path1 path2 - copia o ficheiro path1 para path2; quando path representa um ficheiro num servidor deve ter a forma server@user:path, quando representa um ficheiro local deve ter a forma path");
		System.out.println("rm path - remove o ficheiro path");
		System.out
				.println("info path - apresenta informacao sobre o ficheiro/directoria path, incluindo: nome, boolean indicando se e ficheiro, data da criacao, data da ultima modificacao");

	}

	private void sync(String[] cmd) {
		if (cmd.length != 3) {
			System.out.println(INVALIDARGS);
			return;
		}

		String dir = cmd[1];

		String[] dirserver = cmd[2].split(":");
		String[] serveruser = dirserver[0].split("@");

		String server = dirserver.length == 1 ? null : serveruser[0];
		String user = dirserver.length == 1 || serveruser.length == 1 ? null
				: serveruser[1];

		String path = dirserver.length == 1 ? dirserver[0] : dirserver[1];

		Logger.log("Synchronizing folder " + path + " request to server "
				+ server + " by " + user);
		boolean synced = false;
		synced = sync(dir, server, user, path);
		if (synced) {
			System.out.println(SUCCESS);
		} else
			System.out.println(ERROR);

	}

	private void copyFile(String[] cmd) {
		if (cmd.length != 3) {
			System.out.println(INVALIDARGS);
			return;
		}
		String[] dirserver1 = cmd[1].split(":");
		String[] serveruser1 = dirserver1[0].split("@");

		String fromServer = dirserver1.length == 1 ? null : serveruser1[0];
		String fromUser = dirserver1.length == 1 || serveruser1.length == 1 ? null
				: serveruser1[1];
		String fromPath = dirserver1.length == 1 ? dirserver1[0]
				: dirserver1[1];

		String[] dirserver2 = cmd[2].split(":");
		String[] serveruser2 = dirserver2[0].split("@");

		String toServer = dirserver2.length == 1 ? null : serveruser2[0];
		String toUser = dirserver2.length == 1 || serveruser2.length == 1 ? null
				: serveruser2[1];
		String toPath = dirserver2.length == 1 ? dirserver2[0] : dirserver2[1];
		Logger.log("Copying file " + fromServer + "@" + fromUser + ":"
				+ fromPath + "to " + toServer + "@" + toUser + ":" + toPath);
		if (cp(fromServer, fromUser, fromPath, toServer, toUser, toPath))
			System.out.println(SUCCESS);
		else
			System.out.println(ERROR);
	}

	private void getFileAttributes(String[] cmd) {
		if (cmd.length != 2) {
			System.out.println(INVALIDARGS);
			return;
		}
		String[] dirserver = cmd[1].split(":");
		String[] serveruser = dirserver[0].split("@");

		String server = dirserver.length == 1 ? null : serveruser[0];
		String user = dirserver.length == 1 || serveruser.length == 1 ? null
				: serveruser[1];
		String path = dirserver.length == 1 ? dirserver[0] : dirserver[1];
		Logger.log("Getting attributes for file " + path
				+ " request to server " + server + " by " + user);
		String info = getAttr(server, user, path);
		if (info != null) {
			System.out.println(info);
			System.out.println(SUCCESS);
		} else
			System.out.println(ERROR);
	}

	private void removeFile(String[] cmd) {
		if (cmd.length != 2) {
			System.out.println(INVALIDARGS);
			return;
		}
		String[] dirserver = cmd[1].split(":");
		String[] serveruser = dirserver[0].split("@");

		String server = dirserver.length == 1 ? null : serveruser[0];
		String user = dirserver.length == 1 || serveruser.length == 1 ? null
				: serveruser[1];
		String path = dirserver.length == 1 ? dirserver[0] : dirserver[1];
		Logger.log("Remove file " + path + " request to server " + server
				+ " by " + user);
		if (rm(server, user, path))
			System.out.println(SUCCESS);
		else
			System.out.println(ERROR);
	}

	private void removeDirectory(String[] cmd) {
		if (cmd.length != 2) {
			System.out.println(INVALIDARGS);
			return;
		}
		String[] dirserver = cmd[1].split(":");
		String[] serveruser = dirserver[0].split("@");

		String server = dirserver.length == 1 ? null : serveruser[0];
		String user = dirserver.length == 1 || serveruser.length == 1 ? null
				: serveruser[1];
		String dir = dirserver.length == 1 ? dirserver[0] : dirserver[1];
		Logger.log("Remove directory " + dir + " request to server " + server
				+ " by " + user);
		if (rmdir(server, user, dir))
			System.out.println(SUCCESS);
		else
			System.out.println(ERROR);
	}

	private void makeDirectory(String[] cmd) {
		if (cmd.length != 2) {
			System.out.println(INVALIDARGS);
			return;
		}
		String[] dirserver = cmd[1].split(":");
		String[] serveruser = dirserver[0].split("@");

		String server = dirserver.length == 1 ? null : serveruser[0];
		String user = dirserver.length == 1 || serveruser.length == 1 ? null
				: serveruser[1];
		String dir = dirserver.length == 1 ? dirserver[0] : dirserver[1];
		Logger.log("Make directory " + dir + " request to server " + server
				+ " by " + user);
		if (mkdir(server, user, dir))
			System.out.println(SUCCESS);
		else
			System.out.println(ERROR);
	}

	private void listDirectory(String[] cmd) {
		if (cmd.length != 2) {
			System.out.println(INVALIDARGS);
			return;
		}
		String[] dirserver = cmd[1].split(":");
		String[] serveruser = dirserver[0].split("@");

		String server = dirserver.length == 1 ? null : serveruser[0];
		String user = dirserver.length == 1 || serveruser.length == 1 ? null
				: serveruser[1];
		String dir = dirserver.length == 1 ? dirserver[0] : dirserver[1];
		Logger.log("Listing directory " + dir + " request to server " + server
				+ " by " + user);
		String[] res = dir(server, user, dir);
		if (res != null) {
			Logger.log("" + res.length);
			for (int i = 0; i < res.length; i++)
				System.out.println(res[i]);
		} else
			System.out.println(ERROR);
	}

	private void rmPermission(String[] cmd) {
		if (cmd.length != 3) {
			System.out.println(INVALIDARGS);
			return;
		}
		String server = cmd[1];
		String user = cmd[2];
		Logger.log("Revoking permissions to " + user + " on server " + server);
		if (remPermission(server, user))
			System.out.println(SUCCESS);
		else
			System.out.println(ERROR);
	}

	private void addPermission(String[] cmd) {
		if (cmd.length != 3) {
			System.out.println(INVALIDARGS);
			return;
		}
		String server = cmd[1];
		String user = cmd[2];
		Logger.log("Adding permissions to " + user + " on server " + server);

		if (addPermission(server, user))
			System.out.println(SUCCESS);
		else
			System.out.println(ERROR);
	}

	private void listServers() {
		String[] s = servers();
		if (s == null)
			System.out.println(ERROR);
		else {
			Logger.log("" + s.length);
			for (int i = 0; i < s.length; i++)
				System.out.println(s[i]);
		}
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String username = null;
		String contactServer = null;
		Logger.setupLogger(System.err, true);
		try {
			if (args.length == 0) {
				System.out.println("username:");
				System.out.print(">$ ");
				Scanner in = new Scanner(System.in);
				username = in.next();
				in.nextLine();
			} else if (args.length == 1) {
				username = args[0];
			} else if (args.length == 2) {
				contactServer = args[1];
			}
			new FileClient(username, contactServer).doit();
		} catch (IOException e) {
			System.out.println("Something has gone wrong.");
		}
	}
}
