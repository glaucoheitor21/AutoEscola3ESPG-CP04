package br.com.fiap3espg.autoescola3espg.domain.aluno;

import br.com.fiap3espg.autoescola3espg.domain.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

// E-mail e CPF nao aparecem neste record de proposito: a regra de negocio proibe
// a alteracao desses campos.
public record DadosAtualizacaoAluno(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {
}
