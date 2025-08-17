package br.edu.ifpb.es.daw.todo.rest;

import br.edu.ifpb.es.daw.todo.exception.TodoException;
import br.edu.ifpb.es.daw.todo.rest.dto.MudarSenhaRequestDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioSalvarRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "usuario")
public interface UsuarioRestControllerApi {

    @Operation(summary = "Retornar todos os usuários.",
               description = "Retorna todos os usuários que estão armazenados, sem restrição alguma de quantidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "Operação realizada com sucesso.",
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))),
            @ApiResponse(responseCode = "500",
                         description = "Erro inesperado.",
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetail.class))),
    })
    ResponseEntity<List<UsuarioResponseDTO>> listar() throws TodoException;

    @Operation(summary = "Criar um novo usuário.",
               description = "Cria um novo usuário com base na descrição informada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                         description = "Operação realizada com sucesso.",
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "500",
                         description = "Erro inesperado.",
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetail.class))),
    })
    ResponseEntity<UsuarioResponseDTO> registrar(UsuarioSalvarRequestDTO dto);

    @Operation(summary = "Mudar senha do usuário logado.",
               description = "Muda a senha do usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                         description = "Operação realizada com sucesso.",
                         content = @Content),
            @ApiResponse(responseCode = "401",
                         description = "Senha errada ou usuário não logado.",
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500",
                         description = "Erro inesperado.",
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetail.class))),
    })
    ResponseEntity<Void> mudarSenha(MudarSenhaRequestDTO dto,
                                    @Parameter(hidden = true, description = "Parâmetro injetado pelo Spring")
                                    String username);
}
