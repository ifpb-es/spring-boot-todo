package br.edu.ifpb.es.daw.todo.rest;

import br.edu.ifpb.es.daw.todo.rest.dto.TodoBuscarDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoSalvarRequestDTO;
import br.edu.ifpb.es.daw.todo.service.TodoService;
import tools.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ TodoRestController.class })
@AutoConfigureMockMvc
public class TodoRestControllerUTTests {

    @TestConfiguration
    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static class ContextConfiguration {

    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private TodoService todoService;

    @Test
    void deveRetornarTodasAsTarefasAoListarTodos() throws Exception {
        TodoResponseDTO dto1 = TodoResponseDTO.builder()
                .descrição("Tarefa 1")
                .lookupId(UUID.fromString("3de02457-04a9-4c22-9ed2-7399ec0ae6c2"))
                .concluídoEm(null)
                .build();
        String dto2ConcluídoEm = "2025-12-10T21:03:00";
        TodoResponseDTO dto2 = TodoResponseDTO.builder()
                .descrição("Tarefa 2")
                .lookupId(UUID.fromString("78549a58-14d4-4253-bc8d-ca6fa8082277"))
                .concluídoEm(LocalDateTime.parse(dto2ConcluídoEm))
                .build();

        List<TodoResponseDTO> dtos = Arrays.asList(dto1, dto2);
        when(todoService.recuperarTodos()).thenReturn(dtos);

        mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(dtos.size()))
                .andExpect(jsonPath("$[0].descrição").value(dto1.descrição()))
                .andExpect(jsonPath("$[0].lookupId").value(dto1.lookupId().toString()))
                .andExpect(jsonPath("$[0].concluídoEm").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[1].descrição").value(dto2.descrição()))
                .andExpect(jsonPath("$[1].lookupId").value(dto2.lookupId().toString()))
                .andExpect(jsonPath("$[1].concluídoEm").value(dto2ConcluídoEm));
    }

    @Test
    void deveRecuperarUmaTarefaComSucesso() throws Exception {
        TodoResponseDTO dto = TodoResponseDTO.builder()
                .descrição("Tarefa 1")
                .lookupId(UUID.fromString("3de02457-04a9-4c22-9ed2-7399ec0ae6c2"))
                .concluídoEm(null)
                .build();
        when(todoService.buscarPor(dto.lookupId())).thenReturn(dto);

        mockMvc.perform(get("/todo/{lookupId}", dto.lookupId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descrição").value(dto.descrição()))
                .andExpect(jsonPath("$.lookupId").value(dto.lookupId().toString()))
                .andExpect(jsonPath("$.concluídoEm").value(IsNull.nullValue()));
    }

    @Test
    void deveAdicionarUmaTarefaVálida() throws Exception {

        TodoSalvarRequestDTO payload = new TodoSalvarRequestDTO("Tarefa 1");
        TodoResponseDTO resultado = TodoResponseDTO.builder()
                .concluídoEm(null)
                .descrição(payload.descrição())
                .lookupId(UUID.fromString("110f1e82-c577-45cf-9d18-8e38d7db6e6b"))
                .build();
        when(todoService.criar(payload)).thenReturn(resultado);

        mockMvc.perform(post("/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload))
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lookupId").value(matchesPattern("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(jsonPath("$.descrição").value(payload.descrição()))
                .andExpect(jsonPath("$.concluídoEm").value(IsNull.nullValue()));
    }

    @Test
    void deveAtualizarUmaTarefaComSucesso() throws Exception {
        TodoSalvarRequestDTO payload = new TodoSalvarRequestDTO("Tarefa 1");
        UUID lookupId = UUID.fromString("110f1e82-c577-45cf-9d18-8e38d7db6e6b");
        TodoResponseDTO resultado = TodoResponseDTO.builder()
                .concluídoEm(null)
                .descrição(payload.descrição())
                .lookupId(lookupId)
                .build();
        when(todoService.atualizar(lookupId, payload)).thenReturn(resultado);

        mockMvc.perform(patch("/todo/{lookupId}", resultado.lookupId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lookupId").value(lookupId.toString()))
                .andExpect(jsonPath("$.descrição").value(payload.descrição()))
                .andExpect(jsonPath("$.concluídoEm").value(IsNull.nullValue()));
    }

    @Test
    void deveRemoverUmaTarefaExistenteComSucesso() throws Exception {
        UUID lookupId = UUID.fromString("110f1e82-c577-45cf-9d18-8e38d7db6e6b");

        mockMvc.perform(delete("/todo/{lookupId}", lookupId.toString()))
                .andExpect(status().isNoContent());

        verify(todoService).remover(lookupId);
    }

    @Test
    void deveBuscarAsTarefasDeFormaPaginada() throws Exception {
        TodoResponseDTO dto1 = TodoResponseDTO.builder()
                .descrição("Tarefa 1")
                .lookupId(UUID.fromString("3de02457-04a9-4c22-9ed2-7399ec0ae6c2"))
                .concluídoEm(null)
                .build();
        String dto2ConcluídoEm = "2025-12-10T21:03:00";
        TodoResponseDTO dto2 = TodoResponseDTO.builder()
                .descrição("Tarefa 2")
                .lookupId(UUID.fromString("78549a58-14d4-4253-bc8d-ca6fa8082277"))
                .concluídoEm(LocalDateTime.parse(dto2ConcluídoEm))
                .build();

        List<TodoResponseDTO> dtos = Arrays.asList(dto1, dto2);
        Page<TodoResponseDTO> pagina =
                new PageImpl<>(dtos, PageRequest.of(0, 2), 10);

        TodoBuscarDTO buscarDTO = new TodoBuscarDTO("Tarefa", null, 0, 2);
        when(todoService.buscar(buscarDTO)).thenReturn(pagina);

        mockMvc.perform(get("/todo/buscar")
                        .param("descrição", buscarDTO.descrição())
                        .param("númeroPágina", buscarDTO.númeroPágina().toString())
                        .param("tamanhoPágina", buscarDTO.tamanhoPágina().toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(dtos.size()))
                .andExpect(jsonPath("$.content[0].descrição").value(dto1.descrição()))
                .andExpect(jsonPath("$.content[0].lookupId").value(dto1.lookupId().toString()))
                .andExpect(jsonPath("$.content[0].concluídoEm").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[1].descrição").value(dto2.descrição()))
                .andExpect(jsonPath("$.content[1].lookupId").value(dto2.lookupId().toString()))
                .andExpect(jsonPath("$.content[1].concluídoEm").value(dto2ConcluídoEm));
    }

    @Test
    void deveFazerUmaTarefaComSucesso() throws Exception {
        String dtoConcluídoEm = "2025-12-10T21:03:00";
        TodoResponseDTO resultado = TodoResponseDTO.builder()
                .descrição("Tarefa 1")
                .lookupId(UUID.fromString("3de02457-04a9-4c22-9ed2-7399ec0ae6c2"))
                .concluídoEm(LocalDateTime.parse(dtoConcluídoEm))
                .build();

        when(todoService.fazerTarefa(resultado.lookupId())).thenReturn(resultado);

        mockMvc.perform(patch("/todo/{lookupId}/fazer", resultado.lookupId().toString())
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lookupId").value(resultado.lookupId().toString()))
                .andExpect(jsonPath("$.descrição").value(resultado.descrição()))
                .andExpect(jsonPath("$.concluídoEm").value(dtoConcluídoEm));
    }

    @Test
    void deveDesfazerUmaTarefaComSucesso() throws Exception {
        TodoResponseDTO resultado = TodoResponseDTO.builder()
                .descrição("Tarefa 1")
                .lookupId(UUID.fromString("3de02457-04a9-4c22-9ed2-7399ec0ae6c2"))
                .concluídoEm(null)
                .build();

        when(todoService.desfazerTarefa(resultado.lookupId())).thenReturn(resultado);

        mockMvc.perform(patch("/todo/{lookupId}/desfazer", resultado.lookupId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lookupId").value(resultado.lookupId().toString()))
                .andExpect(jsonPath("$.descrição").value(resultado.descrição()))
                .andExpect(jsonPath("$.concluídoEm").value(IsNull.nullValue()));
    }
}
