package br.com.fiap3espg.autoescola3espg.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
        @NotBlank
        @Size(min = 3, max = 100)
        String login,

        @NotBlank
        @Size(min = 4, max = 100)
        String senha,

        @NotNull
        Role perfil) {
}
