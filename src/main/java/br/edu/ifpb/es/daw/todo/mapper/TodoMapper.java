package br.edu.ifpb.es.daw.todo.mapper;

import org.springframework.stereotype.Component;

import br.edu.ifpb.es.daw.todo.model.Todo;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoSalvarRequestDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoResponseDTO;

@Component
public class TodoMapper {

	public Todo from(TodoSalvarRequestDTO from) {
		Todo obj = new Todo();
		obj.setDescrição(from.getDescrição());
		return obj;
	}
	
	public TodoResponseDTO from(Todo from) {
		TodoResponseDTO obj = new TodoResponseDTO();
		obj.setConcluídoEm(from.getConcluídoEm());
		obj.setDescrição(from.getDescrição());
		obj.setLookupId(from.getLookupId());
		return obj;
	}

}
