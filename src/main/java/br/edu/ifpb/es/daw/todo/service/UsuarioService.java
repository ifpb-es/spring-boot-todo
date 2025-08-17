package br.edu.ifpb.es.daw.todo.service;

import br.edu.ifpb.es.daw.todo.mapper.UsuarioMapper;
import br.edu.ifpb.es.daw.todo.model.Usuario;
import br.edu.ifpb.es.daw.todo.repository.UsuarioRepository;
import br.edu.ifpb.es.daw.todo.rest.dto.MudarSenhaRequestDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioResponseDTO;
import br.edu.ifpb.es.daw.todo.rest.dto.UsuarioSalvarRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
		encodePassword(objNovo);

		Usuario objCriado = repository.save(objNovo);
		return mapper.from(objCriado);
	}

	private void encodePassword(Usuario obj) {
		String senhaCodificada = passwordEncoder.encode(obj.getSenha());
		obj.setSenha(senhaCodificada);
	}
	
	public List<UsuarioResponseDTO> recuperarTodos() {
		return repository.findAll()
				.stream()
				.map(mapper::from)
				.toList();
	}

	private Usuario ensureExists(UUID lookupId) {
		Optional<Usuario> usuarioOpt = repository.findByLookupId(lookupId);
		Usuario obj = usuarioOpt.orElseThrow(() -> new IllegalArgumentException(String.format("Entidade 'Usuario' de lookupId '%s' não foi encontrada!", lookupId)));
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

    @Transactional
	public void mudarSenha(MudarSenhaRequestDTO dto, String emailUsuarioLogado) {
		Optional<Usuario> usuarioOpt = repository.findByEmail(emailUsuarioLogado);
		Usuario usuario = usuarioOpt.orElseThrow(() -> new IllegalArgumentException(String.format("Entidade 'Usuario' de e-mail '%s' não foi encontrada!", emailUsuarioLogado)));

		// Verificar se a senha informada é correta
		String senhaAtual = usuario.getSenha();
		String senhaInformada = dto.senhaAtual();
		String senhaNova = dto.senhaNova();
		boolean senhaCorreta = passwordEncoder.matches(senhaInformada, senhaAtual);
		if (!senhaCorreta) {
			throw new BadCredentialsException("Senha errada!");
		}

		// Codificar a senha
		usuario.setSenha(senhaNova);
		encodePassword(usuario);

		repository.save(usuario);
	}
}
