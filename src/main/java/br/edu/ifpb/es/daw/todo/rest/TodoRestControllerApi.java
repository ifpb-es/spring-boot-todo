package br.edu.ifpb.es.daw.todo.rest;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import br.edu.ifpb.es.daw.todo.exception.TodoException;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoBuscarDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoSalvarRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "todo", description = "API Todo")
public interface TodoRestControllerApi {

	@Operation(summary = "Retornar todas as tarefas.", 
			   description = "Retorna todas as tarefas que estão armazenadas, sem restrição alguma de quantidade.", 
			   tags = { "todo" }) // XXX: Com a definição deste atributo "tag" você poderia associar o endpoint definido aqui 
								  // em um outro controller. A associação é feita mediante o uso da tag definida no outro controller.
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
						 description = "Operação realizada com sucesso.", 
					 	 content = @Content(mediaType = "application/json",
					 			 			array = @ArraySchema(schema = @Schema(implementation = TodoResponseDTO.class)))),
			@ApiResponse(responseCode = "500", 
						 description = "Erro inesperado.", 
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = ProblemDetail.class))),
	})
	ResponseEntity<List<TodoResponseDTO>> listar() throws TodoException;

	@Operation(summary = "Criar uma nova tarefa.", 
			   description = "Cria uma nova tarefa com base na descrição informada.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
						 description = "Operação realizada com sucesso.", 
					 	 content = @Content(mediaType = "application/json",
					 	 					schema = @Schema(implementation = TodoResponseDTO.class))),
			@ApiResponse(responseCode = "500", 
						 description = "Erro inesperado.", 
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = ProblemDetail.class))),
	})
	ResponseEntity<TodoResponseDTO> adicionar(@RequestBody(description = "Dados da tarefa a ser criada.") 
											  TodoSalvarRequestDTO dto) throws TodoException;

	@Operation(summary = "Recuperar uma tarefa existente.", 
			   description = "Recupera uma tarefa existente com base no seu lookupId.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
						 description = "Operação realizada com sucesso.", 
					 	 content = @Content(mediaType = "application/json",
					 	 					schema = @Schema(implementation = TodoResponseDTO.class))),
			@ApiResponse(responseCode = "400", 
			 			 description = "Tarefa com lookupId NÃO encontrada.", 
			 			 content = @Content(mediaType = "application/json",
			 			 					schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "500", 
						 description = "Erro inesperado.", 
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = ProblemDetail.class))),
	})
	ResponseEntity<TodoResponseDTO> recuperarPor(@Parameter(description = "LookupId da tarefa a ser recuperada.")
												 UUID lookupId) throws TodoException;

	@Operation(summary = "Atualizar uma tarefa existente.", 
			   description = "Atualiza uma tarefa existente com base no seu lookupId, permitindo atualização da descrição.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
						 description = "Operação realizada com sucesso.", 
					 	 content = @Content(mediaType = "application/json",
					 	 					schema = @Schema(implementation = TodoResponseDTO.class))),
			@ApiResponse(responseCode = "400", 
			 			 description = "Tarefa com lookupId NÃO encontrada.", 
			 			 content = @Content(mediaType = "application/json",
			 			 					schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "500", 
						 description = "Erro inesperado.", 
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = ProblemDetail.class))),
	})
	ResponseEntity<TodoResponseDTO> atualizar(@Parameter(description = "LookupId da tarefa a ser atualizada.")
											  UUID lookupId, 
											  @RequestBody(description = "Dados da tarefa a ser atualizada.")
											  TodoSalvarRequestDTO dto) throws TodoException;

	@Operation(summary = "Remover uma tarefa existente.", 
			   description = "Remove uma tarefa existente com base no seu lookupId.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
						 description = "Operação realizada com sucesso.", 
					 	 content = @Content),
			@ApiResponse(responseCode = "400", 
			 			 description = "Tarefa com lookupId NÃO encontrada.", 
			 			 content = @Content(mediaType = "application/json",
			 			 					schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "500", 
						 description = "Erro inesperado.", 
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = ProblemDetail.class))),
	})
	ResponseEntity<Void> remover(@Parameter(description = "LookupId da tarefa a ser removida.")
								 UUID lookupId) throws TodoException;

	@Operation(summary = "Recuperar tarefas existentes.", 
			   description = "Recupera tarefas existentes de forma paginada com base nos seguintes filtros opcionais: situação de conclusão (sim ou não) e descrição.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
						 description = "Operação realizada com sucesso.", 
					 	 content = @Content(mediaType = "application/json",
					 						schema = @Schema(implementation = Page.class, contentSchema = TodoResponseDTO.class))),
			@ApiResponse(responseCode = "500", 
						 description = "Erro inesperado.", 
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = ProblemDetail.class))),
	})
	ResponseEntity<Page<TodoResponseDTO>> buscar(@ParameterObject TodoBuscarDTO dto) throws TodoException;

	@Operation(summary = "Marcar uma tarefa como concluída.", 
			   description = "Marca uma tarefa como concluída, lançando erro caso ela já esteja concluída.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
						 description = "Operação realizada com sucesso.", 
					 	 content = @Content(mediaType = "application/json",
					 	 					schema = @Schema(implementation = TodoResponseDTO.class))),
			@ApiResponse(responseCode = "400", 
			 			 description = "Tarefa com lookupId NÃO encontrada ou tarefa já foi concluída.", 
			 			 content = @Content(mediaType = "application/json",
			 			 					schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "500", 
						 description = "Erro inesperado.", 
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = ProblemDetail.class))),
	})
	ResponseEntity<TodoResponseDTO> fazerTarefa(@Parameter(description = "LookupId da tarefa a ser marcada como concluída.")
												UUID lookupId) throws TodoException;

	@Operation(summary = "Desfazer uma tarefa que foi concluída.", 
			   description = "Desfaz uma tarefa que foi concluída, lançando erro caso ela não tenha sido concluída ainda.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
						 description = "Operação realizada com sucesso.", 
					 	 content = @Content(mediaType = "application/json",
					 	 					schema = @Schema(implementation = TodoResponseDTO.class))),
			@ApiResponse(responseCode = "400", 
			 			 description = "Tarefa com lookupId NÃO encontrada ou tarefa NÃO está marcada como concluída.", 
			 			 content = @Content(mediaType = "application/json",
			 			 					schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "500", 
						 description = "Erro inesperado.", 
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = ProblemDetail.class))),
	})
	ResponseEntity<TodoResponseDTO> desfazerTarefa(@Parameter(description = "LookupId da tarefa a ser desfeita.")
												   UUID lookupId) throws TodoException;

}