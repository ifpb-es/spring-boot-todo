package br.edu.ifpb.es.daw.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UsuarioResponseDTO(@Schema(description = "Lookup ID da tarefa.")
                                 UUID lookupId,
                                 @Schema(description = "Nome do usuário.")
                                 String nome,
                                 @Schema(description = "E-mail do usuário (login).")
                                 String email) {
}
