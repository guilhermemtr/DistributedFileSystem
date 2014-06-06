package exceptions;

public class FileAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2021040397690531757L;
	
	private String message;
	
	public FileAlreadyExistsException(String str) {
		super(str);
		this.message = str;
	}
	
	@Override
	public String getLocalizedMessage() {
		return message;
	}

}
