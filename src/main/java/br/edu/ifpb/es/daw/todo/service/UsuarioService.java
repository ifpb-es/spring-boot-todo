package br.edu.ifpb.es.daw.todo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.edu.ifpb.es.daw.todo.mapper.UsuarioMapper;
import br.edu.ifpb.es.daw.todo.model.Todo;
import br.edu.ifpb.es.daw.todo.rest.dto.TodoSalvarRequestDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioSalvarRequestDTO;
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

	private final UsuarioRepository repository;
	private final UsuarioMapper mapper;
	private final PasswordEncoder passwordEncoder;

	@Autowired
    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
	public UsuarioResponseDTO criar(UsuarioSalvarRequestDTO dto) {
		Usuario objNovo = mapper.from(dto);

		// Codificar a senha
		String senhaCodificada = passwordEncoder.encode(dto.senha());
		objNovo.setSenha(senhaCodificada);

		Usuario objCriado = repository.save(objNovo);
		return mapper.from(objCriado);
	}
	
	public List<UsuarioResponseDTO> recuperarTodos() {
		return repository.findAll()
				.stream()
				.map(mapper::from)
				.toList();
	}

	private Usuario ensureExists(UUID lookupId) {
		Optional<Usuario> todoOpt = repository.findByLookupId(lookupId);
		Usuario obj = todoOpt.orElseThrow(() -> new IllegalArgumentException(String.format("Entidade 'Usuario' de lookupId '%s' não foi encontrada!", lookupId)));
		return obj;
	}

	public UsuarioResponseDTO buscarPor(UUID lookupId) {
		Usuario obj = ensureExists(lookupId);
		return mapper.from(obj);
	}

	@Transactional
	public UsuarioResponseDTO atualizar(UUID lookupId, UsuarioSalvarRequestDTO dto) {
		Usuario objExistente = ensureExists(lookupId);
		objExistente.setNome(dto.nome());
		objExistente.setEmail(dto.email());
		//objExistente.setSenha(dto.senha()); // XXX: alteração da senha ocorre em outra lógica
		Usuario objAtualizado = repository.save(objExistente);
		return mapper.from(objAtualizado);
	}

	@Transactional
	public void remover(UUID lookupId) {
		Optional<Usuario> usuarioOpt = repository.findByLookupId(lookupId);
		// Operação idempotente
		usuarioOpt.ifPresent(obj -> repository.delete(obj));
	}

	@Override
	public Usuario loadUserByUsername(String email) throws UsernameNotFoundException {
		return repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));	
	}

}
