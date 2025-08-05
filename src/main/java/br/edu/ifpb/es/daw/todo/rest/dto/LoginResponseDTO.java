package br.edu.ifpb.es.daw.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(@Schema(description = "Descrição do resultado da operação.")
                               String message,
                               @Schema(description = "Token JWT.")
                               String token) { }
