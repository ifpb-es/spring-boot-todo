package br.edu.ifpb.es.daw.todo.rest;

import br.edu.ifpb.es.daw.todo.rest.dto.LoginRequestDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

@Tag(name = "auth")
public interface AuthRestControllerApi {

    @Operation(summary = "Autenticar usuário.",
               description = "Realiza a autenticação do usuári ocom base no login e senha fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "Operação realizada com sucesso.",
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401",
                         description = "Login ou senha inválidos.",
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500",
                         description = "Erro inesperado.",
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetail.class))),
    })
    ResponseEntity<LoginResponseDTO> login(@RequestBody(description = "Login e senha do usuário para autenticar.")
                                           LoginRequestDTO request);

    @Operation(summary = "Retorna o login do usuário logado.",
               description = "Recupera o login do usuário logado com base no token de autorização fornecido no cabeçalho da requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "Operação realizada com sucesso.",
                         content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500",
                         description = "Erro inesperado.",
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetail.class))),
    })
    ResponseEntity<String> currentUserName(@Parameter(hidden = true, description = "Parâmetro injetado pelo Spring")
                           String username);

}
