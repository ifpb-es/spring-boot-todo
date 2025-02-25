package br.edu.ifpb.es.daw.todo.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
	private String message;
    private String token;
}
