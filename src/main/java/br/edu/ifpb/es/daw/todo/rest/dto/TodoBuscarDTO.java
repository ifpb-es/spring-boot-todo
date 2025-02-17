package br.edu.ifpb.es.daw.todo.rest.dto;

import lombok.Data;

@Data
public class TodoBuscarDTO {

	private String descrição;
	
	private Boolean concluído;
	
	private Integer númeroPágina = 0;
	
	private Integer tamanhoPágina = 10;
	
}
