package br.com.fiap3espg.autoescola3espg.domain.usuario;

import jakarta.validation.constraints.NotNull;

// Somente o perfil do usuario pode ser atualizado por um administrador. A senha
// e alterada exclusivamente pelo proprio usuario, em PUT /usuarios/senha.
public record DadosAtualizacaoUsuario(
        @NotNull
        Long id,

        @NotNull
        Role perfil) {
}
