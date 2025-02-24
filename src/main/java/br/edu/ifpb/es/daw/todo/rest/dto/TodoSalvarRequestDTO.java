package br.edu.ifpb.es.daw.todo.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TodoSalvarRequestDTO {
	
	@NotBlank
	private String descrição;

}
