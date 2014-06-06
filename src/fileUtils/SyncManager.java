package fileUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import utils.Logger;

public class SyncManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5359743365626878134L;
	
	Map<String,SyncData> syncs;
	
	public SyncManager() {
		this.syncs = new HashMap<String,SyncData>();
	}
	
	public SyncData getSyncData(String syncId) {
		Logger.log("Getting sync data for " + syncId);
		return syncs.get(syncId);
	}
	
	public void sync(String syncId, SyncData sd) {
		Logger.log("Adding sync data for " + syncId);
		Logger.log(sd.toString());
		this.syncs.put(syncId, sd);
	}
}
