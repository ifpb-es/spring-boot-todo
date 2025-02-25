package br.edu.ifpb.es.daw.todo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.es.daw.todo.exception.TodoException;
import br.edu.ifpb.es.daw.todo.mapper.UsuarioMapper;
import br.edu.ifpb.es.daw.todo.model.Usuario;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioResponseDTO;
import br.edu.ifpb.es.daw.todo.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioMapper mapper;

	@Autowired
	private UsuarioService service;

	@GetMapping
	public ResponseEntity<List<UsuarioResponseDTO>> listar() throws TodoException {
		List<Usuario> objs = service.recuperarTodos();
		List<UsuarioResponseDTO> resultado = objs.stream().map(mapper::from).toList();
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
}
