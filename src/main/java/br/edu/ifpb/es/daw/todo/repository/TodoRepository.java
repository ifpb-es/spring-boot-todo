package br.edu.ifpb.es.daw.todo.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.es.daw.todo.model.Todo;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoBuscarDTO;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
	
	@Query("SELECT t FROM Todo t WHERE (:descrição is null or t.descrição LIKE %:descrição%)"
			+ " AND (:concluído is null or ((t.concluídoEm is null and :concluído = FALSE) "
			+ "									or "
			+ "								(t.concluídoEm is not null and :concluído = TRUE)))"
			)
	Page<Todo> buscarPor(String descrição, Boolean concluído, Pageable pageable);

	@Query("SELECT t FROM Todo t WHERE (:#{#dto.descrição} is null or t.descrição LIKE %:#{#dto.descrição}%)"
			+ " AND (:#{#dto.concluído} is null or ((t.concluídoEm is null and :#{#dto.concluído} = FALSE) "
			+ "											or "
			+ "										(t.concluídoEm is not null and :#{#dto.concluído} = TRUE)))"
			)
	Page<Todo> buscarPor(TodoBuscarDTO dto, Pageable pageable);
	
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("UPDATE Todo todo SET todo.concluídoEm = :concluídoEm WHERE todo.lookupId = :lookupId")
	int atualizarCampoConcluídoEm(UUID lookupId, LocalDateTime concluídoEm);
	
}
