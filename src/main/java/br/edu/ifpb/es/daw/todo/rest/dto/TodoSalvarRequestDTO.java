package br.edu.ifpb.es.daw.todo.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoSalvarRequestDTO(@NotBlank @Size(min = 5, max = 255) String descrição) { }
