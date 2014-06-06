package sync;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import utils.Domains;
import utils.Logger;

public class SyncUtils {

	
	
	public static void saveSync(SyncManager data, String localPath) {
		if(!localPath.endsWith("/")) localPath += "/";
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(localPath + Domains.SYNCFILENAME));
			out.writeObject(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			Logger.log(e.getLocalizedMessage());
		}
	}
	
	public static SyncManager loadSync(String localPath) {
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(localPath + Domains.SYNCFILENAME));
			SyncManager sm = (SyncManager) in.readObject();
			in.close();
			return sm;
		} catch (IOException e) {
			Logger.log(e.getLocalizedMessage());
		} catch (ClassNotFoundException e) {
		}
		return null;
	}
	
	
	
}
