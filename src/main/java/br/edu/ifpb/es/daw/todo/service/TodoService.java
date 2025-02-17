package br.edu.ifpb.es.daw.todo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.es.daw.todo.model.Todo;
import br.edu.ifpb.es.daw.todo.repository.TodoRepository;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoBuscarDTO;

@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repository;

	@Transactional
	public Todo criar(Todo obj) {
		return repository.save(obj);
	}
	
	public List<Todo> recuperarTodos() {
		return repository.findAll();
	}

	public Optional<Todo> buscarPor(UUID lookupId) {
		Todo objExemplo = Todo.builder()
							.lookupId(lookupId)
							.build();
		Example<Todo> exemplo = Example.of(objExemplo);
		return repository.findOne(exemplo);
	}

	@Transactional
	public Todo atualizar(Todo obj) {
		Todo resultado = repository.save(obj);
		return resultado;
	}

	@Transactional
	public void remover(Todo obj) {
		repository.delete(obj);
	}
	
	public Page<Todo> buscar(TodoBuscarDTO dto) {
		Page<Todo> resultado = repository.buscarPor(dto.getDescrição(), dto.getConcluído(), PageRequest.of(dto.getNúmeroPágina(), dto.getTamanhoPágina()));
		return resultado;
	}
	
	@Transactional
	public Todo fazerTarefa(Todo obj) {
		if (obj.feito()) {
			throw new IllegalArgumentException(String.format("Entidade 'Todo' de lookupId '%s' já foi marcada como feita!", obj.getLookupId()));
		}
		repository.atualizarCampoConcluídoEm(obj.getLookupId(), LocalDateTime.now());
		Todo resultado = repository.findById(obj.getId()).get();
		return resultado;
	}

	@Transactional
	public Todo desfazerTarefa(Todo obj) {
		if (!obj.feito()) {
			throw new IllegalArgumentException(String.format("Entidade 'Todo' de lookupId '%s' não foi marcada com feita ainda!", obj.getLookupId()));
		}
		repository.atualizarCampoConcluídoEm(obj.getLookupId(), null);
		Todo resultado = repository.findById(obj.getId()).get();
		return resultado;
	}

}
