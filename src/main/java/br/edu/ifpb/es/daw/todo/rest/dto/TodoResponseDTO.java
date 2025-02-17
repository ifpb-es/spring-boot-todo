package br.edu.ifpb.es.daw.todo.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class TodoResponseDTO {

	private UUID lookupId;

	private String descrição;

	private LocalDateTime concluídoEm;

}
