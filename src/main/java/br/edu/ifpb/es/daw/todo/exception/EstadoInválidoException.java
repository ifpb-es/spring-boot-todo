package br.edu.ifpb.es.daw.todo.exception;

import lombok.Getter;

@Getter
public class EstadoInválidoException extends TodoException {

	private static final long serialVersionUID = 1L;

	public EstadoInválidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public EstadoInválidoException(String message) {
		super(message);
	}
	
}
