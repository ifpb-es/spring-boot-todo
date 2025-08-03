package br.edu.ifpb.es.daw.todo.rest.dto;

public record TodoBuscarDTO(String descrição,
							Boolean concluído,
							Integer númeroPágina,
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
