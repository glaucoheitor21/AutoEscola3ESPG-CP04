package br.com.fiap3espg.autoescola3espg.domain.instrucao;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DadosDetalhamentoCancelamento(
        Long id,
        String nomeAluno,
        String nomeInstrutor,

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm")
        LocalDateTime dataHora,

        MotivoCancelamento motivoCancelamento,

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm")
        LocalDateTime dataCancelamento) {
    public DadosDetalhamentoCancelamento(Instrucao instrucao) {
        this(
                instrucao.getId(),
                instrucao.getAluno().getNome(),
                instrucao.getInstrutor().getNome(),
                instrucao.getDataHora(),
                instrucao.getMotivoCancelamento(),
                instrucao.getDataCancelamento()
        );
    }
}
