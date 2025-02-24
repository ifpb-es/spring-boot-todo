package br.edu.ifpb.es.daw.todo.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private TodoMapper todoMapper;
	
	@Autowired
	private TodoService todoService;
	
	@Override
	@GetMapping
	public ResponseEntity<List<TodoResponseDTO>> listar() throws TodoException {
		List<Todo> objs = todoService.recuperarTodos();
		List<TodoResponseDTO> resultado = objs.stream()
													.map(todoMapper::from)
													.toList();
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PostMapping
	public ResponseEntity<TodoResponseDTO> adicionar(@RequestBody @Valid TodoSalvarRequestDTO dto) throws TodoException {
		Todo objNovo = todoMapper.from(dto);
		Todo objCriado = todoService.criar(objNovo);
		TodoResponseDTO resultado = todoMapper.from(objCriado);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@GetMapping("/{lookupId}")
	public ResponseEntity<TodoResponseDTO> recuperarPor(@PathVariable UUID lookupId) throws TodoException {
		// Recuperar
		Todo obj = validarExiste(lookupId);
		TodoResponseDTO resultado = todoMapper.from(obj);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PatchMapping("/{lookupId}")
	public ResponseEntity<TodoResponseDTO> atualizar(@PathVariable UUID lookupId, @RequestBody @Valid TodoSalvarRequestDTO dto) throws TodoException {
		// Atualizar entidade existente
		Todo objExistente = validarExiste(lookupId);
		objExistente.setDescrição(dto.getDescrição());
		Todo objAtualizado = todoService.atualizar(objExistente);
		TodoResponseDTO resultado = todoMapper.from(objAtualizado);
		
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	
	@Override
	@DeleteMapping("/{lookupId}")
	public ResponseEntity<Void> remover(@PathVariable UUID lookupId) throws TodoException {
		// Remover
		Todo obj = validarExiste(lookupId);
		todoService.remover(obj);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@GetMapping("/buscar")
	public ResponseEntity<Page<TodoResponseDTO>> buscar(TodoBuscarDTO dto) throws TodoException {
		Page<Todo> objs = todoService.buscar(dto);
		
		Page<TodoResponseDTO> resultado = objs
				.map(todoMapper::from);
		
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PatchMapping("/{lookupId}/fazer")
	public ResponseEntity<TodoResponseDTO> fazerTarefa(@PathVariable UUID lookupId) throws TodoException {
		// Atualizar entidade existente
		Todo objExistente = validarExiste(lookupId);
		Todo objAtualizado = todoService.fazerTarefa(objExistente);
		TodoResponseDTO resultado = todoMapper.from(objAtualizado);
		
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}
	
	@Override
	@PatchMapping("/{lookupId}/desfazer")
	public ResponseEntity<TodoResponseDTO> desfazerTarefa(@PathVariable UUID lookupId) throws TodoException {
		// Atualizar entidade existente
		Todo objExistente = validarExiste(lookupId);
		Todo objAtualizado = todoService.desfazerTarefa(objExistente);
		TodoResponseDTO resultado = todoMapper.from(objAtualizado);
		
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	private Todo validarExiste(UUID lookupId) {
		// Checar se entidade existe
		Optional<Todo> opt = todoService.buscarPor(lookupId);
		if (!opt.isPresent()) {
			// Lançar erro porque não encontrou
			throw new IllegalArgumentException(String.format("Entidade 'Todo' de lookupId '%s' não foi encontrada!", lookupId));
		}
		// Retornar entidade encontrada
		return opt.get();
	}
}
