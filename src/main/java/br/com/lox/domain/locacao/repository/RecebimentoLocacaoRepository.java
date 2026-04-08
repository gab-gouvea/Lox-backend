package br.com.lox.domain.locacao.repository;

import br.com.lox.domain.locacao.entity.RecebimentoLocacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecebimentoLocacaoRepository extends JpaRepository<RecebimentoLocacao, String> {

    List<RecebimentoLocacao> findByLocacaoId(String locacaoId);

    List<RecebimentoLocacao> findByMesAndAno(Integer mes, Integer ano);

    Optional<RecebimentoLocacao> findByLocacaoIdAndMesAndAno(String locacaoId, Integer mes, Integer ano);
}
