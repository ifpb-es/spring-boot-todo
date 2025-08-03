package br.edu.ifpb.es.daw.todo.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record TodoResponseDTO(UUID lookupId, String descrição, LocalDateTime concluídoEm) { }
