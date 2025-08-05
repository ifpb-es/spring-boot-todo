package br.edu.ifpb.es.daw.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoSalvarRequestDTO(@Schema(description = "Descrição da tarefa.")
                                   @NotBlank
                                   @Size(min = 5, max = 255)
                                   String descrição) {
}
