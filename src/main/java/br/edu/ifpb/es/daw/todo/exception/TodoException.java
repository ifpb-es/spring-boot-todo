package br.edu.ifpb.es.daw.todo.exception;

public class TodoException extends Exception {

	private static final long serialVersionUID = 1L;

	public TodoException(String message, Throwable cause) {
		super(message, cause);
	}

	public TodoException(String message) {
		super(message);
	}

}
