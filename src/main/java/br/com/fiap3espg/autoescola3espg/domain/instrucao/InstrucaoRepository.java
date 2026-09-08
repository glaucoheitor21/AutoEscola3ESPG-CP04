package br.com.fiap3espg.autoescola3espg.domain.instrucao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InstrucaoRepository extends JpaRepository<Instrucao, Long> {
    // Uma instrucao cancelada libera a agenda, por isso os metodos abaixo so
    // consideram instrucoes cujo motivo de cancelamento ainda esta nulo.
    boolean existsByInstrutorIdAndDataHoraAndMotivoCancelamentoIsNull(
            Long idInstrutor, LocalDateTime dataHora);

    long countByAlunoIdAndDataHoraBetweenAndMotivoCancelamentoIsNull(
            Long idAluno, LocalDateTime inicioDoDia, LocalDateTime fimDoDia);
}
