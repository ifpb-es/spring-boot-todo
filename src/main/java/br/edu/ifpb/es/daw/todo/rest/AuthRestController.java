package br.edu.ifpb.es.daw.todo.rest;

import br.edu.ifpb.es.daw.todo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.es.daw.todo.rest.dto.LoginRequestDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.LoginResponseDTO;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

	private final AuthService authService;

	@Autowired
    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    // Forma mais prática de liberar acesso a todos, mas não funciona em
	// conjunto com a configuração de segurança sendo usada no momento (ver SecurityConfig)
	// Referência(s):
	// https://github.com/spring-projects/spring-security/issues/12861
	// https://github.com/spring-projects/spring-security/issues/16250
	// @PreAuthorize("permitAll()")
	@PostMapping("/login")
	public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO request) {
		return authService.logar(request);
	}

	// TODO: Adicionar endpoint específico para alterar a senha, exigindo senha atual e nova!

}
