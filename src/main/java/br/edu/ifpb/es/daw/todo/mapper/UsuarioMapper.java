package br.edu.ifpb.es.daw.todo.mapper;

import org.springframework.stereotype.Component;

import br.edu.ifpb.es.daw.todo.model.Usuario;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioSalvarRequestDTO;

@Component
public class UsuarioMapper {
	
	public UsuarioResponseDTO from(Usuario from) {
		UsuarioResponseDTO obj = new UsuarioResponseDTO();
		obj.setLookupId(from.getLookupId());
		obj.setEmail(from.getEmail());
		obj.setNome(from.getNome());
		return obj;
	}

	public Usuario from(UsuarioSalvarRequestDTO from) {
		Usuario obj = new Usuario();
		obj.setNome(from.getNome());
		obj.setEmail(from.getEmail());
		obj.setSenha(from.getSenha());
		return obj;
	}
	
	

}
