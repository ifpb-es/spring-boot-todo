package br.edu.ifpb.es.daw.todo.service;

import br.edu.ifpb.es.daw.todo.rest.dto.LoginRequestDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private AuthenticationManager authenticationManager;
    private JwtService jwtUtils;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponseDTO logar(LoginRequestDTO request) {
        // Validate credentials using AuthenticationManager
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

        // only get save value inside token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate JWT token
        String jwtToken = jwtUtils.generateJwtToken(userDetails);

        return new LoginResponseDTO("Autenticação realizada com sucesso!", jwtToken);
    }

}
