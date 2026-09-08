package br.com.fiap3espg.autoescola3espg.domain.instrucao.validacao;

import br.com.fiap3espg.autoescola3espg.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

// Funcionamento de segunda a sabado, das 06:00 as 21:00. Como a instrucao dura
// 1 hora, o ultimo horario possivel de inicio e 20:00.
@Component
public class ValidadorHorarioFuncionamento implements ValidadorAgendamento {
    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        LocalDateTime dataEscolhida = dados.dataHora();

        boolean domingo = dataEscolhida.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        boolean preAbertura = dataEscolhida.getHour() < 6;
        boolean posFechamento = dataEscolhida.getHour() > 20;

        if (domingo || preAbertura || posFechamento) {
            throw new ValidacaoException("Tentativa de agendamento fora do horario de funcionamento!");
        }
    }
}
