package br.edu.ifpb.es.daw.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record MudarSenhaRequestDTO(@Schema(description = "Senha atual.")
                                   @NotBlank
                                   String senhaAtual,
                                   @Schema(description = "Nova senha.")
                                   @NotBlank
                                   String senhaNova) { }
