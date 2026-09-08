package br.com.fiap3espg.autoescola3espg.service;

import br.com.fiap3espg.autoescola3espg.domain.instrucao.ValidacaoException;
import br.com.fiap3espg.autoescola3espg.domain.usuario.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    // A senha recebida em texto puro e imediatamente convertida em hash BCrypt;
    // o valor original nunca chega ao banco de dados.
    @Transactional
    public DadosDetalhamentoUsuario cadastrarUsuario(DadosCadastroUsuario dados) {
        if (repository.existsByLogin(dados.login())) {
            throw new ValidacaoException("Ja existe um usuario cadastrado com o login informado!");
        }

        Usuario usuario = new Usuario(
                dados.login(),
                passwordEncoder.encode(dados.senha()),
                dados.perfil()
        );

        return new DadosDetalhamentoUsuario(repository.save(usuario));
    }

    public Page<DadosListagemUsuario> listarUsuarios(Pageable paginacao) {
        return repository
                .findAll(paginacao)
                .map(DadosListagemUsuario::new);
    }

    public DadosDetalhamentoUsuario detalharUsuario(Long id) {
        return new DadosDetalhamentoUsuario(buscarUsuario(id));
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizarPerfil(DadosAtualizacaoUsuario dados) {
        Usuario usuario = buscarUsuario(dados.id());
        usuario.atualizarPerfil(dados.perfil());
        return new DadosDetalhamentoUsuario(repository.save(usuario));
    }

    @Transactional
    public void excluirUsuario(Long id, String loginAutenticado) {
        Usuario usuario = buscarUsuario(id);

        // Evita que um administrador remova a propria conta e fique sem acesso.
        if (usuario.getLogin().equals(loginAutenticado)) {
            throw new ValidacaoException("Um usuario nao pode excluir a propria conta!");
        }

        repository.delete(usuario);
    }

    // Alteracao da propria senha: exige a senha atual como confirmacao.
    @Transactional
    public void alterarPropriaSenha(String loginAutenticado, DadosAlteracaoSenha dados) {
        Usuario usuario = repository.findByLogin(loginAutenticado)
                .orElseThrow(() ->
                        new UsuarioNotFoundException("Usuario autenticado nao encontrado!"));

        if (!passwordEncoder.matches(dados.senhaAtual(), usuario.getPassword())) {
            throw new ValidacaoException("Senha atual incorreta!");
        }

        if (passwordEncoder.matches(dados.novaSenha(), usuario.getPassword())) {
            throw new ValidacaoException("A nova senha deve ser diferente da senha atual!");
        }

        usuario.alterarSenha(passwordEncoder.encode(dados.novaSenha()));
        repository.save(usuario);
    }

    private Usuario buscarUsuario(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNotFoundException("ID do usuario informado nao existe!"));
    }
}
