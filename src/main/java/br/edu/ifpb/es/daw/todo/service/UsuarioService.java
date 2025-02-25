package br.edu.ifpb.es.daw.todo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.es.daw.todo.model.Usuario;
import br.edu.ifpb.es.daw.todo.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public Usuario criar(Usuario obj) {
		obj.setSenha(passwordEncoder.encode(obj.getSenha()));
		return repository.save(obj);
	}
	
	public List<Usuario> recuperarTodos() {
		return repository.findAll();
	}

	public Optional<Usuario> buscarPor(UUID lookupId) {
		Usuario objExemplo = Usuario.builder()
							.lookupId(lookupId)
							.build();
		Example<Usuario> exemplo = Example.of(objExemplo);
		return repository.findOne(exemplo);
	}

	@Transactional
	public Usuario atualizar(Usuario obj) {
		Usuario resultado = repository.save(obj);
		return resultado;
	}

	@Transactional
	public void remover(Usuario obj) {
		repository.delete(obj);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));	
	}

}
