package br.com.fiap3espg.autoescola3espg.domain.instrucao.validacao;

import br.com.fiap3espg.autoescola3espg.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.ValidacaoException;
import br.com.fiap3espg.autoescola3espg.domain.instrutor.InstrutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidadorInstrutorAtivo implements ValidadorAgendamento {
    private final InstrutorRepository instrutorRepository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        // Instrutor e opcional: quando nao informado, o sorteio ja considera apenas
        // instrutores ativos, entao nao ha o que validar aqui.
        if (dados.idInstrutor() == null) {
            return;
        }

        if (instrutorRepository.existsByIdAndAtivoFalse(dados.idInstrutor())) {
            throw new ValidacaoException("Nao e possivel agendar instrucao com instrutor inativo!");
        }
    }
}
