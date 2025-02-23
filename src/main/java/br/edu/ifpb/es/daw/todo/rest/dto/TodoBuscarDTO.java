package br.edu.ifpb.es.daw.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TodoBuscarDTO {

	@Schema(description = "Descrição da tarefa.")
	private String descrição;
	
	@Schema(description = "'true' caso a tarefa de interesse seja concluída, 'false' caso contrário.")
	private Boolean concluído;
	
	@Schema(description = "Número da página a ser retornada na paginação. Começa com zero.")
	private Integer númeroPágina = 0;
	
	@Schema(description = "Quantidade de registros a serem retornados por página.")
	private Integer tamanhoPágina = 10;
	
}
