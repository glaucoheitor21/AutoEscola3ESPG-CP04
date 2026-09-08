package br.com.fiap3espg.autoescola3espg.service;

import br.com.fiap3espg.autoescola3espg.domain.aluno.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository repository;

    @Transactional
    public DadosDetalhamentoAluno cadastrarAluno(DadosCadastroAluno dados) {
        Aluno aluno = new Aluno(dados);
        Aluno salvo = repository.save(aluno);
        return new DadosDetalhamentoAluno(salvo);
    }

    public Page<DadosListagemAluno> listarAlunos(Pageable paginacao) {
        return repository
                .findAllByAtivoTrue(paginacao)
                .map(DadosListagemAluno::new);
    }

    public DadosDetalhamentoAluno detalharAluno(Long id) {
        return new DadosDetalhamentoAluno(buscarAluno(id));
    }

    @Transactional
    public DadosDetalhamentoAluno atualizarAluno(DadosAtualizacaoAluno dados) {
        Aluno aluno = buscarAluno(dados.id());
        aluno.atualizarInformacoes(dados);
        return new DadosDetalhamentoAluno(repository.save(aluno));
    }

    // Exclusao logica: o registro permanece na base, apenas marcado como inativo.
    @Transactional
    public void excluirAluno(Long id) {
        Aluno aluno = buscarAluno(id);
        aluno.excluir();
        repository.save(aluno);
    }

    private Aluno buscarAluno(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new AlunoNotFoundException("ID do aluno informado nao existe!"));
    }
}
