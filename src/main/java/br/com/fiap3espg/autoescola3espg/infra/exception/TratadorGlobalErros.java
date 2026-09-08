package br.com.fiap3espg.autoescola3espg.infra.exception;

import br.com.fiap3espg.autoescola3espg.domain.aluno.AlunoNotFoundException;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.InstrucaoNotFoundException;
import br.com.fiap3espg.autoescola3espg.domain.instrucao.ValidacaoException;
import br.com.fiap3espg.autoescola3espg.domain.instrutor.InstrutorNotFoundException;
import br.com.fiap3espg.autoescola3espg.domain.usuario.UsuarioNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorGlobalErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({
            InstrutorNotFoundException.class,
            AlunoNotFoundException.class,
            InstrucaoNotFoundException.class,
            UsuarioNotFoundException.class
    })
    public ResponseEntity<DadosMessage> tratarRecursoNaoEncontrado(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new DadosMessage(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosBadRequest>> tratarBadRequest(
            MethodArgumentNotValidException ex) {
        List<FieldError> erros = ex.getFieldErrors();
        return ResponseEntity
                .badRequest()
                .body(erros
                        .stream()
                        .map(DadosBadRequest::new)
                        .toList()
                );
    }

    // Regras de negocio violadas (agendamento e cancelamento de instrucoes,
    // login ja cadastrado, senha atual incorreta, ...).
    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DadosMessage> tratarRegraDeNegocio(ValidacaoException ex) {
        return ResponseEntity
                .badRequest()
                .body(new DadosMessage(ex.getMessage()));
    }

    // JSON malformado ou valor invalido em um enum (ex.: motivo de cancelamento
    // fora das opcoes previstas).
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DadosMessage> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body(new DadosMessage("Requisicao invalida: verifique o formato dos campos enviados."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DadosMessage> tratarErroDeAutenticacao() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new DadosMessage("Credenciais invalidas!"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DadosMessage> tratarAcessoNegado() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new DadosMessage("Acesso negado: voce nao tem permissao para executar esta operacao."));
    }

    private record DadosBadRequest(
            String field,
            String message) {
        public DadosBadRequest(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    private record DadosMessage(String message) {
    }
}
