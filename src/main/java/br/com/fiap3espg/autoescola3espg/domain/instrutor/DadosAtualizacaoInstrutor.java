package br.com.fiap3espg.autoescola3espg.domain.instrutor;

import br.com.fiap3espg.autoescola3espg.domain.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

// E-mail, CNH e especialidade nao aparecem neste record de proposito: a regra de
// negocio proibe a alteracao desses campos.
public record DadosAtualizacaoInstrutor(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {
}
