package br.com.fiap3espg.autoescola3espg.domain.usuario;

// A senha (mesmo criptografada) nunca e devolvida pela API.
public record DadosDetalhamentoUsuario(
        Long id,
        String login,
        Role perfil) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getPerfil()
        );
    }
}
