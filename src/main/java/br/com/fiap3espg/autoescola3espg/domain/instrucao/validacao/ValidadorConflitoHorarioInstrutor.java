package br.com.fiap3espg.autoescola3espg.domain.instrucao.validacao;

import br.com.fiap3espg.autoescola3espg.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.InstrucaoRepository;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidadorConflitoHorarioInstrutor implements ValidadorAgendamento {
    private final InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        // Instrutor e opcional: quando nao informado, o sorteio ja exclui quem esta
        // ocupado na data/hora, entao nao ha o que validar aqui.
        if (dados.idInstrutor() == null) {
            return;
        }

        boolean instrutorOcupado = repository.existsByInstrutorIdAndDataHoraAndMotivoCancelamentoIsNull(
                dados.idInstrutor(),
                dados.dataHora()
        );

        if (instrutorOcupado) {
            throw new ValidacaoException("Instrutor ocupado na data / hora escolhida!");
        }
    }
}
