package br.edu.ifpb.es.daw.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TodoBuscarDTO(@Schema(description = "Descrição da tarefa.")
							String descrição,
							@Schema(description = "'true' caso a tarefa de interesse seja concluída, 'false' caso contrário.")
							Boolean concluído,
							@Schema(description = "Número da página a ser retornada na paginação. Começa com zero.")
							Integer númeroPágina,
							@Schema(description = "Quantidade de registros a serem retornados por página.")
							Integer tamanhoPágina) {

	public TodoBuscarDTO {
		if (númeroPágina == null) {
			númeroPágina = 0;
		}
		if (tamanhoPágina == null) {
			tamanhoPágina = 10;
		}
	}
}
