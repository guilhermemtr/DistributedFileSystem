package exceptions;

public class NoPermissionsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -882315970611031435L;

	private String message;
	
	public NoPermissionsException(String str) {
		super(str);
		this.message = str;
	}
	
	@Override
	public String getLocalizedMessage() {
		return message;
	}

	
}
