package br.edu.ifpb.es.daw.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TodoSalvarRequestDTO(@Schema(description = "Descrição da tarefa.")
                                   String descrição) {
}
