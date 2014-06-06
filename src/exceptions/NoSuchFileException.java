package exceptions;

public class NoSuchFileException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4357314613829594444L;
	
	private String message;
	
	public NoSuchFileException(String str) {
		super(str);
		this.message = str;
	}
	
	@Override
	public String getLocalizedMessage() {
		return message;
	}


}
