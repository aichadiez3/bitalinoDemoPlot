package Bitalino;

public class BitalinoException extends java.lang.Exception {
	
	private static final long serialVersionUID = 1L;

	public BitalinoException(BitalinoErrorTypes errorType) {
		super(errorType.getName());
	    code = errorType.getValue();
	}
	
	public int code;

}
