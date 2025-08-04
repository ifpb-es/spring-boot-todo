package br.edu.ifpb.es.daw.todo.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank String email, @NotBlank String senha) { }
