package br.edu.ifpb.es.daw.todo.rest.dto;

import lombok.Data;

@Data
public class UsuarioSalvarRequestDTO {
	private String nome;
	private String email;
	private String senha;
}
