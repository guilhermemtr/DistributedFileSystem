package exceptions;

public class ServerOfflineException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1429540199936520942L;
	private String message;
	
	public ServerOfflineException(String str) {
		super(str);
		this.message = str;
	}
	
	@Override
	public String getLocalizedMessage() {
		return message;
	}
}
