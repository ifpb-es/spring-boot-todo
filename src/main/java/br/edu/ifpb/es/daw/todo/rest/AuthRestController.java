package br.edu.ifpb.es.daw.todo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.es.daw.todo.mapper.UsuarioMapper;
import br.edu.ifpb.es.daw.todo.model.Usuario;
import br.edu.ifpb.es.daw.todo.rest.dto.LoginRequestDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.LoginResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioSalvarRequestDTO;
import br.edu.ifpb.es.daw.todo.service.UsuarioService;
import br.edu.ifpb.es.daw.todo.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

	@Autowired
	private UsuarioMapper mapper;

	@Autowired
	private UsuarioService service;

	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private JwtUtil jwtUtils;

	// Forma mais prática de liberar acesso a todos, mas não funciona em 
	// conjunto com a configuração de segurança sendo usada no momento (ver SecurityConfig)
	// @PreAuthorize("permitAll()")
	@PostMapping("/login")
	public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
		// Validate credentials using AuthenticationManager
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));
		
		// only get save value inside token
		Usuario userDetails = (Usuario) authentication.getPrincipal();
		
		// Generate JWT token
		String jwtToken = jwtUtils.generateJwtToken(userDetails.getUsername());

		return new LoginResponseDTO("Autenticação realizada com sucesso!", jwtToken);
	}

	// Forma mais prática de liberar acesso a todos, mas não funciona em 
	// conjunto com a configuração de segurança sendo usada no momento (ver SecurityConfig)
	// @PreAuthorize("permitAll()")
	@PostMapping("/registrar")
	public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody UsuarioSalvarRequestDTO userDto) {
		Usuario objCriado = service.criar(mapper.from(userDto));
		UsuarioResponseDTO resultado = mapper.from(objCriado);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

}
