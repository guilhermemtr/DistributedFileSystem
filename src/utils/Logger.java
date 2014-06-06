package utils;

import java.io.PrintStream;

public class Logger {

	private static boolean debug = false;
	
	private static long startTime = 0;
	
	private static PrintStream logger = System.err;
	
	public static void setupLogger(PrintStream out, boolean toLog) {
		startTime = System.currentTimeMillis();
		logger = out;
		debug = toLog;
	}
	
	/**
	 * Logs and prints to stderr
	 * @param log - message to log
	 */
	public static void log(String log) {
		if (debug) {
			float millis = System.currentTimeMillis() - startTime;
			logger.printf("[%.3f]%s\n", millis / 1000.0f, log);
		}
	}
}
