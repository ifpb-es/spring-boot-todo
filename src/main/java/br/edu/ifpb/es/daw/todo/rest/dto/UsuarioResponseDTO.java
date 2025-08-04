package br.edu.ifpb.es.daw.todo.rest.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UsuarioResponseDTO(UUID lookupId, String nome, String email) { }
