package br.com.fiap3espg.autoescola3espg.domain.instrucao;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoInstrucao(
        @NotNull
        @JsonProperty("id_instrucao")
        Long idInstrucao,

        // Obrigatorio. Aceita apenas ALUNO_DESISTIU, INSTRUTOR_CANCELOU ou OUTROS.
        @NotNull
        @JsonProperty("motivo_cancelamento")
        MotivoCancelamento motivoCancelamento) {
}
