package br.com.fiap3espg.autoescola3espg.domain.instrucao;

import br.com.fiap3espg.autoescola3espg.domain.instrutor.Especialidade;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoInstrucao(
        @NotNull
        @JsonProperty("id_aluno")
        Long idAluno,

        // Opcional: quando nao informado, o sistema sorteia um instrutor ativo e
        // disponivel na data/hora, dentro da especialidade escolhida.
        @JsonProperty("id_instrutor")
        Long idInstrutor,
        Especialidade especialidade,

        @NotNull
        @Future
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm")
        @JsonProperty("data_hora")
        LocalDateTime dataHora) {
}
