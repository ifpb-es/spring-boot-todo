package br.edu.ifpb.es.daw.todo.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import br.edu.ifpb.es.daw.todo.exception.TodoException;
import br.edu.ifpb.es.daw.todo.mapper.TodoMapper;
import br.edu.ifpb.es.daw.todo.model.Todo;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoBuscarDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoSalvarRequestDTO;
import br.edu.ifpb.es.daw.todo.service.TodoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/todo")
public class TodoRestController implements TodoRestControllerApi {

	@Autowired
	private TodoMapper mapper;
	
	@Autowired
	private TodoService service;
	
	@Override
	@GetMapping
	public ResponseEntity<List<TodoResponseDTO>> listar() throws TodoException {
		List<Todo> objs = service.recuperarTodos();
		List<TodoResponseDTO> resultado = objs.stream()
													.map(mapper::from)
													.toList();
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PostMapping
	public ResponseEntity<TodoResponseDTO> adicionar(@RequestBody @Valid TodoSalvarRequestDTO dto) throws TodoException {
		Todo objNovo = mapper.from(dto);
		Todo objCriado = service.criar(objNovo);
		TodoResponseDTO resultado = mapper.from(objCriado);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@GetMapping("/{lookupId}")
	public ResponseEntity<TodoResponseDTO> recuperarPor(@PathVariable UUID lookupId) throws TodoException {
		// Recuperar
		Todo obj = validarExiste(lookupId);
		TodoResponseDTO resultado = mapper.from(obj);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PatchMapping("/{lookupId}")
	public ResponseEntity<TodoResponseDTO> atualizar(@PathVariable UUID lookupId, @RequestBody @Valid TodoSalvarRequestDTO dto) throws TodoException {
		// Atualizar entidade existente
		Todo objExistente = validarExiste(lookupId);
		objExistente.setDescrição(dto.getDescrição());
		Todo objAtualizado = service.atualizar(objExistente);
		TodoResponseDTO resultado = mapper.from(objAtualizado);
		
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	
	@Override
	@DeleteMapping("/{lookupId}")
	public ResponseEntity<Void> remover(@PathVariable UUID lookupId) throws TodoException {
		// Remover
		Todo obj = validarExiste(lookupId);
		service.remover(obj);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@GetMapping("/buscar")
	public ResponseEntity<Page<TodoResponseDTO>> buscar(TodoBuscarDTO dto) throws TodoException {
		Page<Todo> objs = service.buscar(dto);
		
		Page<TodoResponseDTO> resultado = objs
				.map(mapper::from);
		
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PatchMapping("/{lookupId}/fazer")
	public ResponseEntity<TodoResponseDTO> fazerTarefa(@PathVariable UUID lookupId) throws TodoException {
		// Atualizar entidade existente
		Todo objExistente = validarExiste(lookupId);
		Todo objAtualizado = service.fazerTarefa(objExistente);
		TodoResponseDTO resultado = mapper.from(objAtualizado);
		
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PatchMapping("/{lookupId}/desfazer")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
	public ResponseEntity<TodoResponseDTO> desfazerTarefa(@PathVariable UUID lookupId) throws TodoException {
		// Atualizar entidade existente
		Todo objExistente = validarExiste(lookupId);
		Todo objAtualizado = service.desfazerTarefa(objExistente);
		TodoResponseDTO resultado = mapper.from(objAtualizado);
		
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	private Todo validarExiste(UUID lookupId) {
		// Checar se entidade existe
		Optional<Todo> opt = service.buscarPor(lookupId);
		if (!opt.isPresent()) {
			// Lançar erro porque não encontrou
			throw new IllegalArgumentException(String.format("Entidade 'Todo' de lookupId '%s' não foi encontrada!", lookupId));
		}
		// Retornar entidade encontrada
		return opt.get();
	}
}
