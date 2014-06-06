package exceptions;

public class NoSuchPathException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9214706838139476275L;
	
	private String message;
	
	public NoSuchPathException(String str) {
		super(str);
		this.message = str;
	}
	
	@Override
	public String getLocalizedMessage() {
		return message;
	}


}
