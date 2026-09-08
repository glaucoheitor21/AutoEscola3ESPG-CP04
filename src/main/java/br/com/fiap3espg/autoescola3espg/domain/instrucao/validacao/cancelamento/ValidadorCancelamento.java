package br.com.fiap3espg.autoescola3espg.domain.instrucao.validacao.cancelamento;

import br.com.fiap3espg.autoescola3espg.domain.instrucao.Instrucao;

// Mesmo padrao dos validadores de agendamento: cada regra e um @Component isolado
// e o service recebe a lista completa por injecao de dependencia.
public interface ValidadorCancelamento {
    void validar(Instrucao instrucao);
}
