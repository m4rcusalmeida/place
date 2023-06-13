package br.com.place.exceptions;

public class PlaceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlaceNotFoundException() {
		super("Place not found!");
	}

	public PlaceNotFoundException(String message) {
		super(message);
	}

}
