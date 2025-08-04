package br.edu.ifpb.es.daw.todo.mapper;

import org.springframework.stereotype.Component;

import br.edu.ifpb.es.daw.todo.model.Todo;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoSalvarRequestDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoResponseDTO;

@Component
public class TodoMapper {

	public Todo from(TodoSalvarRequestDTO from) {
		return Todo.builder()
				.descrição(from.descrição())
				.build();
	}
	
	public TodoResponseDTO from(Todo from) {
		return TodoResponseDTO.builder()
				.lookupId(from.getLookupId())
				.descrição(from.getDescrição())
				.concluídoEm(from.getConcluídoEm())
				.build();
	}

}
