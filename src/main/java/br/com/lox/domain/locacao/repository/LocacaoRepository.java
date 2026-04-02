package br.com.lox.domain.locacao.repository;

import br.com.lox.domain.locacao.entity.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.lox.domain.locacao.entity.LocacaoStatus;

import java.time.Instant;
import java.util.List;

public interface LocacaoRepository extends JpaRepository<Locacao, String> {

    List<Locacao> findByStatusAndCheckOutLessThanEqual(LocacaoStatus status, Instant checkOut);

    List<Locacao> findByPropriedadeId(String propriedadeId);

    @Query("SELECT l FROM Locacao l WHERE l.checkOut > :start AND l.checkIn < :end")
    List<Locacao> findByDateRange(@Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT l FROM Locacao l WHERE l.propriedadeId = :propertyId AND l.checkOut > :start AND l.checkIn < :end")
    List<Locacao> findByPropertyIdAndDateRange(@Param("propertyId") String propertyId,
                                               @Param("start") Instant start,
                                               @Param("end") Instant end);
}
