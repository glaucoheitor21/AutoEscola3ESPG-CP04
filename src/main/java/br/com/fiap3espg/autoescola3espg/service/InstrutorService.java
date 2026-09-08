package br.com.fiap3espg.autoescola3espg.service;

import br.com.fiap3espg.autoescola3espg.domain.instrutor.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstrutorService {
    private final InstrutorRepository repository;

    @Transactional
    public DadosDetalhamentoInstrutor cadastrarInstrutor(DadosCadastroInstrutor dados) {
        Instrutor instrutor = new Instrutor(dados);
        Instrutor salvo = repository.save(instrutor);
        return new DadosDetalhamentoInstrutor(salvo);
    }

    public Page<DadosListagemInstrutor> listarInstrutores(Pageable paginacao) {
        return repository
                .findAllByAtivoTrue(paginacao)
                .map(DadosListagemInstrutor::new);
    }

    public DadosDetalhamentoInstrutor detalharInstrutor(Long id) {
        return new DadosDetalhamentoInstrutor(buscarInstrutor(id));
    }

    @Transactional
    public DadosDetalhamentoInstrutor atualizarInstrutor(DadosAtualizacaoInstrutor dados) {
        Instrutor instrutor = buscarInstrutor(dados.id());
        instrutor.atualizarInformacoes(dados);
        return new DadosDetalhamentoInstrutor(repository.save(instrutor));
    }

    // Exclusao logica: o registro permanece na base, apenas marcado como inativo.
    @Transactional
    public void excluirInstrutor(Long id) {
        Instrutor instrutor = buscarInstrutor(id);
        instrutor.excluir();
        repository.save(instrutor);
    }

    private Instrutor buscarInstrutor(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado nao existe!"));
    }
}
