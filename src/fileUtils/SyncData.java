package fileUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

public class SyncData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5042554636562258914L;
	
	private String server;
	private String path;
	private String user;
	private Date localSyncDate;
	private Date serverSyncDate;
	
	private Collection<String> files;
	
	public SyncData(String server, String path, String user, Collection<String> files, Date serverSyncDate) {
		this.server = server;
		this.path = path;
		this.user = user;
		this.localSyncDate = new Date();
		this.serverSyncDate = serverSyncDate;
		this.files = new LinkedList<String>();
		this.files.addAll(files);
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public Collection<String> getFiles() {
		return files;
	}

	public void setFiles(Collection<String> files) {
		this.files = files;
	}

	public Date getLocalSyncDate() {
		return localSyncDate;
	}

	public void setLocalSyncDate(Date localSyncDate) {
		this.localSyncDate = localSyncDate;
	}

	public Date getServerSyncDate() {
		return serverSyncDate;
	}

	public void setServerSyncDate(Date serverSyncDate) {
		this.serverSyncDate = serverSyncDate;
	}

	@Override
	public String toString() {
		return "SyncData [server=" + server + ", path=" + path + ", user="
				+ user + ", localSyncDate=" + localSyncDate
				+ ", serverSyncDate=" + serverSyncDate + ", files=" + files
				+ "]";
	}
}
