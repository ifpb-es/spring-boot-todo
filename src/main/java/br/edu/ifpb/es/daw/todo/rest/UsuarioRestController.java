package br.edu.ifpb.es.daw.todo.rest;

import java.util.List;

import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioSalvarRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.es.daw.todo.exception.TodoException;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioResponseDTO;
import br.edu.ifpb.es.daw.todo.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioRestController {

	private final UsuarioService service;

	@Autowired
    public UsuarioRestController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<List<UsuarioResponseDTO>> listar() throws TodoException {
		List<UsuarioResponseDTO> resultado = service.recuperarTodos();
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	// Forma mais prática de liberar acesso a todos, mas não funciona em
	// conjunto com a configuração de segurança sendo usada no momento (ver SecurityConfig)
	// Referência(s):
	// https://github.com/spring-projects/spring-security/issues/12861
	// https://github.com/spring-projects/spring-security/issues/16250
	//@PreAuthorize("permitAll()")
	@PostMapping("/registrar")
	public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody UsuarioSalvarRequestDTO dto) {
		UsuarioResponseDTO resultado = service.criar(dto);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
}
