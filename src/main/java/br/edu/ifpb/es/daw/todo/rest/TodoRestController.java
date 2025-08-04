package br.edu.ifpb.es.daw.todo.rest;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.es.daw.todo.rest.dto.TodoBuscarDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoSalvarRequestDTO;
import br.edu.ifpb.es.daw.todo.service.TodoService;

@RestController
@RequestMapping("/todo")
public class TodoRestController implements TodoRestControllerApi {

	private final TodoService todoService;

	@Autowired
    public TodoRestController(TodoService todoService) {
        this.todoService = todoService;
    }

	@Override
	@GetMapping
	public ResponseEntity<List<TodoResponseDTO>> listar() {
		List<TodoResponseDTO> resultado = todoService.recuperarTodos();
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PostMapping
	public ResponseEntity<TodoResponseDTO> adicionar(@RequestBody @Valid TodoSalvarRequestDTO dto) {
		TodoResponseDTO resultado = todoService.criar(dto);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@GetMapping("/{lookupId}")
	public ResponseEntity<TodoResponseDTO> recuperarPor(@PathVariable UUID lookupId) {
		// Recuperar
		TodoResponseDTO resultado = todoService.buscarPor(lookupId);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PatchMapping("/{lookupId}")
	public ResponseEntity<TodoResponseDTO> atualizar(@PathVariable UUID lookupId, @RequestBody @Valid TodoSalvarRequestDTO dto) {
		// Atualizar entidade existente
		TodoResponseDTO resultado = todoService.atualizar(lookupId, dto);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@DeleteMapping("/{lookupId}")
	public ResponseEntity<Void> remover(@PathVariable UUID lookupId) {
		// Remover
		todoService.remover(lookupId);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@GetMapping("/buscar")
	public ResponseEntity<Page<TodoResponseDTO>> buscar(TodoBuscarDTO dto) {
		Page<TodoResponseDTO> resultado = todoService.buscar(dto);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PatchMapping("/{lookupId}/fazer")
	public ResponseEntity<TodoResponseDTO> fazerTarefa(@PathVariable UUID lookupId) {
		// Atualizar entidade existente
		TodoResponseDTO resultado = todoService.fazerTarefa(lookupId);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PatchMapping("/{lookupId}/desfazer")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
	public ResponseEntity<TodoResponseDTO> desfazerTarefa(@PathVariable UUID lookupId) {
		// Atualizar entidade existente
		TodoResponseDTO resultado = todoService.desfazerTarefa(lookupId);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
}
