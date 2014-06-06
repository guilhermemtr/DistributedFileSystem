package exceptions;

public class ServerAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6973069243796092169L;
	
	private String message;

	public ServerAlreadyExistsException(String str) {
		super(str);
		this.message = str;
	}

	@Override
	public String getLocalizedMessage() {
		return message;
	}
}
