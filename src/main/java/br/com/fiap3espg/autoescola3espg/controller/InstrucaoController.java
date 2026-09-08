package br.com.fiap3espg.autoescola3espg.controller;

import br.com.fiap3espg.autoescola3espg.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.DadosCancelamentoInstrucao;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.DadosDetalhamentoAgendamento;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.DadosDetalhamentoCancelamento;
import br.com.fiap3espg.autoescola3espg.service.InstrucaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instrucoes")
@RequiredArgsConstructor
public class InstrucaoController {
    private final InstrucaoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<DadosDetalhamentoAgendamento> agendarInstrucao(
            @RequestBody @Valid DadosAgendamentoInstrucao dados) {
        return ResponseEntity.ok(service.agendarInstrucao(dados));
    }

    // DELETE porque a operacao cancela um recurso existente, mas com corpo: o motivo
    // do cancelamento e obrigatorio pela regra de negocio.
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<DadosDetalhamentoCancelamento> cancelarInstrucao(
            @RequestBody @Valid DadosCancelamentoInstrucao dados) {
        return ResponseEntity.ok(service.cancelarInstrucao(dados));
    }
}
