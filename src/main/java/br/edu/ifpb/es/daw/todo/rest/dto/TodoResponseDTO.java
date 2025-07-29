package br.edu.ifpb.es.daw.todo.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
public record TodoResponseDTO(UUID lookupId, String descrição, LocalDateTime concluídoEm) { }
