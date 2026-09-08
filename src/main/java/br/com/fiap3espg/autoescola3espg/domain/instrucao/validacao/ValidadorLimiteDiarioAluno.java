package br.com.fiap3espg.autoescola3espg.domain.instrucao.validacao;

import br.com.fiap3espg.autoescola3espg.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.InstrucaoRepository;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// Regra: nao permitir o agendamento de MAIS DE DUAS instrucoes no mesmo dia para
// um mesmo aluno. Ou seja, a terceira do dia e que deve ser barrada.
@Component
@RequiredArgsConstructor
public class ValidadorLimiteDiarioAluno implements ValidadorAgendamento {
    private static final long LIMITE_DIARIO = 2;

    private final InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        LocalDateTime inicioDoDia = dados.dataHora().toLocalDate().atStartOfDay();
        LocalDateTime fimDoDia = inicioDoDia.plusDays(1).minusSeconds(1);

        long instrucoesNoDia = repository.countByAlunoIdAndDataHoraBetweenAndMotivoCancelamentoIsNull(
                dados.idAluno(),
                inicioDoDia,
                fimDoDia
        );

        if (instrucoesNoDia >= LIMITE_DIARIO) {
            throw new ValidacaoException(
                    "Permitido o agendamento de no maximo " + LIMITE_DIARIO + " instrucoes por dia para o mesmo aluno!");
        }
    }
}
