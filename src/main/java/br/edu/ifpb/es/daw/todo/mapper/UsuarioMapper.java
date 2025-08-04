package br.edu.ifpb.es.daw.todo.mapper;

import org.springframework.stereotype.Component;

import br.edu.ifpb.es.daw.todo.model.Usuario;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioSalvarRequestDTO;

@Component
public class UsuarioMapper {
	
	public UsuarioResponseDTO from(Usuario from) {
		return UsuarioResponseDTO.builder()
				.lookupId(from.getLookupId())
				.email(from.getEmail())
				.nome(from.getNome())
				.build();
	}

	public Usuario from(UsuarioSalvarRequestDTO from) {
		return Usuario.builder()
				.nome(from.nome())
				.email(from.email())
				.senha(from.senha())
				.build();
	}

}
