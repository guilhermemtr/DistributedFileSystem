package exceptions;

public class NoSuchServerException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4448734903724329736L;
	private String message;

	public NoSuchServerException(String str) {
		super(str);
		this.message = str;
	}

	@Override
	public String getLocalizedMessage() {
		return message;
	}
}
