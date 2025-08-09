package br.edu.ifpb.es.daw.todo.rest;

import br.edu.ifpb.es.daw.todo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.es.daw.todo.rest.dto.LoginRequestDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.LoginResponseDTO;

import java.security.Principal;

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

//	@GetMapping(value = "/me")
//	public String currentUserName(Authentication authentication) {
//		// UsernamePasswordAuthenticationToken [Principal=fulano1@gmail.com, Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[ROLE_USUARIO]]
//		return authentication.getName();
//	}

//	@GetMapping(value = "/me")
//	public String currentUserName(Principal principal) {
//		// UsernamePasswordAuthenticationToken [Principal=fulano1@gmail.com, Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[ROLE_USUARIO]]
//		return principal.getName();
//	}

	@GetMapping(value = "/me")
	public String currentUserName(@AuthenticationPrincipal(errorOnInvalidType = true) String username) {
		// XXX: O parâmetro setado por @AuthenticationPrincipal corresponde ao atributo "principal" atribuído no
		//  construtor de UsernamePasswordAuthenticationToken em JwtAuthenticationFilter#doFilterInternal.
		return username;
	}
}
