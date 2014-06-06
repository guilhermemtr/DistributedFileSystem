package utils;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class RemoteUtils {

	public static final int RMIPORT = 1099;

	public static final int RETRYTIME = 1000;
	
	public static final int LONGRETRYTIME = 5000;

	public static final int NTRIES = 3;

	/**
	 * Method to sleep for a retry.
	 */
	public static void sleepToRetry() {
		Logger.log("Sleeping for another retry");
		try {
			Thread.sleep(RETRYTIME);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Method to sleep for a retry.
	 */
	public static void sleepToRetry(String message, int retryTime) {
		Logger.log(message);
		try {
			Thread.sleep(retryTime);
		} catch (InterruptedException e) {
		}
	}

	public static void createNodeRegistry() {
		try { // start rmiregistry
			Logger.log("Creating Local registry on port " + RemoteUtils.RMIPORT);
			LocateRegistry.createRegistry(RemoteUtils.RMIPORT);
		} catch (RemoteException e) {
			Logger.log(e.getMessage());
		}
	}
	
}
