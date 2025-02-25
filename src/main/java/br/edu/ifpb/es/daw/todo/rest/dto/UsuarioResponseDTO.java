package br.edu.ifpb.es.daw.todo.rest.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UsuarioResponseDTO {

	private UUID lookupId;
	
	private String nome;
	
	private String email;
}
