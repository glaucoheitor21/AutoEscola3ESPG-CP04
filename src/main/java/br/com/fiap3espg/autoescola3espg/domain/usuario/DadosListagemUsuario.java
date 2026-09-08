package br.com.fiap3espg.autoescola3espg.domain.usuario;

// A senha (mesmo criptografada) nunca e devolvida pela API.
public record DadosListagemUsuario(
        Long id,
        String login,
        Role perfil) {
    public DadosListagemUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getPerfil()
        );
    }
}
