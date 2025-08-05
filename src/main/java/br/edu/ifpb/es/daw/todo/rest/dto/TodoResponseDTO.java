package br.edu.ifpb.es.daw.todo.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TodoResponseDTO(@Schema(description = "Lookup ID da tarefa.")
                              UUID lookupId,
                              @Schema(description = "Descrição da tarefa.")
                              String descrição,
                              @Schema(description = "Data em que a tarefa foi concluída.")
                              LocalDateTime concluídoEm) {
}
