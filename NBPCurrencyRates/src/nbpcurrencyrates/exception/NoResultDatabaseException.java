package nbpcurrencyrates.exception;

public class NoResultDatabaseException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoResultDatabaseException (String message) {
		super(message);
	}

}
