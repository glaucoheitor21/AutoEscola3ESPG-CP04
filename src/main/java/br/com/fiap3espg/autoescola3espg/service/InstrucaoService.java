package br.com.fiap3espg.autoescola3espg.service;

import br.com.fiap3espg.autoescola3espg.domain.aluno.Aluno;
import br.com.fiap3espg.autoescola3espg.domain.aluno.AlunoNotFoundException;
import br.com.fiap3espg.autoescola3espg.domain.aluno.AlunoRepository;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.*;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.validacao.ValidadorAgendamento;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.validacao.cancelamento.ValidadorCancelamento;
import br.com.fiap3espg.autoescola3espg.domain.instrutor.Instrutor;
import br.com.fiap3espg.autoescola3espg.domain.instrutor.InstrutorNotFoundException;
import br.com.fiap3espg.autoescola3espg.domain.instrutor.InstrutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstrucaoService {
    private final InstrucaoRepository repository;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final List<ValidadorAgendamento> validadoresAgendamento;
    private final List<ValidadorCancelamento> validadoresCancelamento;

    @Transactional
    public DadosDetalhamentoAgendamento agendarInstrucao(DadosAgendamentoInstrucao dados) {
        if (!alunoRepository.existsById(dados.idAluno())) {
            throw new AlunoNotFoundException("ID do aluno informado nao existe!");
        }
        if (dados.idInstrutor() != null && !instrutorRepository.existsById(dados.idInstrutor())) {
            throw new InstrutorNotFoundException("ID do instrutor informado nao existe!");
        }

        validadoresAgendamento.forEach(validador -> validador.validar(dados));

        Aluno aluno = alunoRepository.getReferenceById(dados.idAluno());
        Instrutor instrutor = escolherInstrutor(dados);
        if (instrutor == null) {
            throw new ValidacaoException("Nenhum instrutor disponivel para a data/hora informada!");
        }

        Instrucao instrucao = new Instrucao(aluno, instrutor, dados.dataHora());
        return new DadosDetalhamentoAgendamento(repository.save(instrucao));
    }

    // O cancelamento nao remove a instrucao da base: grava o motivo e a data em que
    // ela foi cancelada, liberando o horario do instrutor para novos agendamentos.
    @Transactional
    public DadosDetalhamentoCancelamento cancelarInstrucao(DadosCancelamentoInstrucao dados) {
        Instrucao instrucao = repository.findById(dados.idInstrucao())
                .orElseThrow(() ->
                        new InstrucaoNotFoundException("ID da instrucao informado nao existe!"));

        validadoresCancelamento.forEach(validador -> validador.validar(instrucao));

        instrucao.cancelar(dados.motivoCancelamento());
        return new DadosDetalhamentoCancelamento(repository.save(instrucao));
    }

    private Instrutor escolherInstrutor(DadosAgendamentoInstrucao dados) {
        if (dados.idInstrutor() != null) {
            return instrutorRepository.getReferenceById(dados.idInstrutor());
        }
        if (dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade e obrigatoria se o instrutor nao for informado!");
        }
        return instrutorRepository.escolherInstrutorAleatorioDisponivel(
                dados.especialidade(),
                dados.dataHora()
        );
    }
}
