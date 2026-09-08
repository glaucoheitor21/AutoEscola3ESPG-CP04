package br.com.fiap3espg.autoescola3espg.domain.instrucao;

import br.com.fiap3espg.autoescola3espg.domain.aluno.Aluno;
import br.com.fiap3espg.autoescola3espg.domain.instrutor.Instrutor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Instrucao")
@Table(name = "instrucoes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Instrucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    // O cancelamento nao apaga a instrucao: ela permanece na base com o motivo e a
    // data em que foi cancelada preenchidos.
    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_cancelamento")
    private MotivoCancelamento motivoCancelamento;

    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;

    public Instrucao(Aluno aluno, Instrutor instrutor, LocalDateTime dataHora) {
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.dataHora = dataHora;
    }

    public void cancelar(MotivoCancelamento motivo) {
        this.motivoCancelamento = motivo;
        this.dataCancelamento = LocalDateTime.now();
    }

    public boolean isCancelada() {
        return motivoCancelamento != null;
    }
}
