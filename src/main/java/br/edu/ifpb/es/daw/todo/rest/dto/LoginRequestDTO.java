package br.edu.ifpb.es.daw.todo.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@Schema(description = "Login do usuário (e-mail).")
                              @NotBlank
                              String email,
                              @Schema(description = "Senha do usuário.")
                              @NotBlank
                              String senha) { }
