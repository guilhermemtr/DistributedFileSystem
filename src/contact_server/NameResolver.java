package contact_server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utils.Logger;

public class NameResolver implements INameResolver {

	// The fileservers
	// key is the server name. The value is its URL.
	private Map<String, String> fileServers;

	public NameResolver() {
		Logger.log("Creating name resolver");
		this.fileServers = new ConcurrentHashMap<String,String>();
	}
	
	@Override
	public void registerServer(String name, String url) {
		Logger.log("Adding server to the resolver");
		this.fileServers.put(name, url);
	}

	@Override
	public void removeServer(String name) {
		Logger.log("Removing server entry");
		this.fileServers.remove(name);
	}

	@Override
	public String getUrl(String name) {
		return this.fileServers.get(name);
	}

}
