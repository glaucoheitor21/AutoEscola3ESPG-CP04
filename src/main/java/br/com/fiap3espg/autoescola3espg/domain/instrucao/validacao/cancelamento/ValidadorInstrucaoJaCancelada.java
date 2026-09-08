package br.com.fiap3espg.autoescola3espg.domain.instrucao.validacao.cancelamento;

import br.com.fiap3espg.autoescola3espg.domain.instrucao.Instrucao;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorInstrucaoJaCancelada implements ValidadorCancelamento {
    @Override
    public void validar(Instrucao instrucao) {
        if (instrucao.isCancelada()) {
            throw new ValidacaoException("Esta instrucao ja foi cancelada anteriormente!");
        }
    }
}
