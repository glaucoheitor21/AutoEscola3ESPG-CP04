package br.com.fiap3espg.autoescola3espg.domain.instrucao.validacao.cancelamento;

import br.com.fiap3espg.autoescola3espg.domain.instrucao.Instrucao;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

// Regra: uma instrucao somente pode ser cancelada com antecedencia minima de 24 horas.
@Component
public class ValidadorAntecedenciaCancelamento implements ValidadorCancelamento {
    private static final long ANTECEDENCIA_MINIMA_EM_HORAS = 24;

    @Override
    public void validar(Instrucao instrucao) {
        long antecedencia = Duration
                .between(LocalDateTime.now(), instrucao.getDataHora())
                .toHours();

        if (antecedencia < ANTECEDENCIA_MINIMA_EM_HORAS) {
            throw new ValidacaoException(
                    "Instrucao so pode ser cancelada com antecedencia minima de "
                            + ANTECEDENCIA_MINIMA_EM_HORAS + " horas!");
        }
    }
}
