package br.edu.ifpb.es.daw.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UsuarioSalvarRequestDTO(@Schema(description = "Nome do usuário.")
                                      @NotBlank
                                      String nome,
                                      @NotBlank
                                      @Schema(description = "E-mail do usuário (login).")
                                      String email,
                                      @Schema(description = "Senha do usuário.")
                                      @NotBlank
                                      String senha) {
}
