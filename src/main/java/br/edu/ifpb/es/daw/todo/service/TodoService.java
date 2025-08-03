package br.edu.ifpb.es.daw.todo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.edu.ifpb.es.daw.todo.exception.EstadoInválidoException;
import br.edu.ifpb.es.daw.todo.exception.TodoException;
import br.edu.ifpb.es.daw.todo.mapper.TodoMapper;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoSalvarRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.es.daw.todo.model.Todo;
import br.edu.ifpb.es.daw.todo.repository.TodoRepository;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoBuscarDTO;

@Service
public class TodoService {

	private final TodoRepository repository;
	private final TodoMapper todoMapper;

	@Autowired
	public TodoService(TodoMapper todoMapper, TodoRepository repository) {
        this.todoMapper = todoMapper;
        this.repository = repository;
    }

	@Transactional
	public TodoResponseDTO criar(TodoSalvarRequestDTO dto) throws TodoException {
		Todo objNovo = todoMapper.from(dto);
		validarDescrição(objNovo);
		Todo objCriado = repository.save(objNovo);
		return todoMapper.from(objCriado);
	}
	
	public List<TodoResponseDTO> recuperarTodos() {
		return repository.findAll()
				.stream()
				.map(todoMapper::from)
				.toList();
	}

	private Todo ensureExists(UUID lookupId) {
		Optional<Todo> todoOpt = repository.findByLookupId(lookupId);
		Todo obj = todoOpt.orElseThrow(() -> new IllegalArgumentException(String.format("Entidade 'Todo' de lookupId '%s' não foi encontrada!", lookupId)));
		return obj;
	}

	public TodoResponseDTO buscarPor(UUID lookupId) {
		Todo obj = ensureExists(lookupId);
		return todoMapper.from(obj);
	}

	@Transactional
	public TodoResponseDTO atualizar(UUID lookupId, TodoSalvarRequestDTO dto) throws TodoException {
		Todo objExistente = ensureExists(lookupId);
		objExistente.setDescrição(dto.descrição());
		validarEdição(objExistente);
		Todo objAtualizado = repository.save(objExistente);
		return todoMapper.from(objAtualizado);
	}

	@Transactional
	public void remover(UUID lookupId) {
		Optional<Todo> todoOpt = repository.findByLookupId(lookupId);
		// Operação idempotente
		todoOpt.ifPresent(obj -> repository.delete(obj));
	}
	
	public Page<TodoResponseDTO> buscar(TodoBuscarDTO dto) {
		Page<Todo> todoPage = repository.buscarPor(dto.descrição(), dto.concluído(), PageRequest.of(dto.númeroPágina(), dto.tamanhoPágina()));
		return todoPage.map(todoMapper::from);
	}
	
	@Transactional
	public TodoResponseDTO fazerTarefa(UUID lookupId) {
		Todo objExistente = ensureExists(lookupId);
		if (objExistente.feito()) {
			// Operação idempotente
			return todoMapper.from(objExistente);
		}
		return atualizarCampoConcluídoEm(objExistente, LocalDateTime.now());
	}

	@Transactional
	public TodoResponseDTO desfazerTarefa(UUID lookupId) {
		Todo objExistente = ensureExists(lookupId);
		if (!objExistente.feito()) {
			// Operação idempotente
			return todoMapper.from(objExistente);
		}
		validarDescrição(objExistente);
		return atualizarCampoConcluídoEm(objExistente, null);
	}

	private TodoResponseDTO atualizarCampoConcluídoEm(Todo objExistente, LocalDateTime concluídoEm) {
		objExistente.setConcluídoEm(concluídoEm);
		Todo objAtualizado = repository.save(objExistente);
		return todoMapper.from(objAtualizado);
	}

	private void validarDescrição(Todo obj) throws TodoException {
		// XXX: A) Não pode haver mais de uma Tarefa NÃO concluída com a mesma descrição.
		//  OBS: essa abordagem de validação utilizada é suscetível a condição de corrida, mas é suficiente para fins
		//  didáticos de demonstrar tratamento de erro personalizado no Spring Boot. Na situação em que requisições
		//  simultâneas tentam inserir uma nova Tarefa com a mesma descrição, essa verificação pode falhar devido a
		//  concorrência na execução, resultando no armazenamento de duas tarefas com a mesma descrição. Uma solução
		//  mais robusta requer a criação de restrições no BD, mas o JPA puro não dá suporte a criação das restrições
		//  que seriam necessárias:
		//  CREATE UNIQUE INDEX uc_descricao ON todo (descrição, COALESCE(concluído_em, TIMESTAMP '9999-12-31 23:59:59'));
		Optional<Todo> objExistenteMesmaDescrição = repository.findByIdIsNotAndDescriçãoIgnoreCaseAndConcluídoEmIsNull(obj.getId(), obj.getDescrição());
		if (objExistenteMesmaDescrição.isPresent()) {
			throw new EstadoInválidoException(String.format("Já existe tarefa com a seguinte descrição: %s.", obj.getDescrição()));
		}
	}

	private void validarEdição(Todo obj) throws TodoException {
		// XXX: B) Não pode editar uma Tarefa já concluída.
		if (obj.feito()) {
			throw new EstadoInválidoException(String.format("Não pode editar uma tarefa já concluída."));
		}

		validarDescrição(obj);
	}
}
