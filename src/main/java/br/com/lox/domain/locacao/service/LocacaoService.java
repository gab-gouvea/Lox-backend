package br.com.lox.domain.locacao.service;

import br.com.lox.domain.locacao.dto.CreateLocacaoDTO;
import br.com.lox.domain.locacao.dto.UpdateLocacaoDTO;
import br.com.lox.domain.locacao.entity.Locacao;
import br.com.lox.domain.locacao.entity.LocacaoStatus;
import br.com.lox.domain.locacao.entity.RecebimentoLocacao;
import br.com.lox.domain.locacao.repository.LocacaoRepository;
import br.com.lox.domain.locacao.repository.RecebimentoLocacaoRepository;
import br.com.lox.domain.reservation.entity.ReservationStatus;
import br.com.lox.domain.reservation.repository.ReservationRepository;
import br.com.lox.exceptions.BusinessRuleException;
import br.com.lox.exceptions.LocacaoNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class LocacaoService {

    private final LocacaoRepository locacaoRepository;
    private final ReservationRepository reservationRepository;
    private final RecebimentoLocacaoRepository recebimentoRepository;

    public LocacaoService(LocacaoRepository locacaoRepository, ReservationRepository reservationRepository,
                          RecebimentoLocacaoRepository recebimentoRepository) {
        this.locacaoRepository = locacaoRepository;
        this.reservationRepository = reservationRepository;
        this.recebimentoRepository = recebimentoRepository;
    }

    @Transactional
    public Locacao create(CreateLocacaoDTO data) {
        if (data.checkOut().isBefore(data.checkIn()) || data.checkOut().equals(data.checkIn())) {
            throw new BusinessRuleException("Data de saída deve ser após a data de entrada");
        }

        checkOverlap(data.propriedadeId(), data.checkIn(), data.checkOut(), null);

        var entity = new Locacao(
                data.propriedadeId(),
                data.tipoLocacao() != null ? data.tipoLocacao() : "temporada",
                data.nomeCompleto(),
                data.cpf(),
                data.rg(),
                data.dataNascimento(),
                data.profissao(),
                data.estadoCivil(),
                data.endereco(),
                data.email(),
                data.checkIn(),
                data.checkOut(),
                data.numMoradores(),
                data.valorMensal(),
                data.tipoPagamento(),
                data.valorTotal(),
                data.percentualComissao(),
                data.garantia(),
                data.faxinaIntervaloDias(),
                data.notas(),
                data.status()
        );

        return locacaoRepository.save(entity);
    }

    public List<Locacao> findAll(String propertyId, Instant start, Instant end) {
        if (propertyId != null && start != null && end != null) {
            return locacaoRepository.findByPropertyIdAndDateRange(propertyId, start, end);
        }
        if (propertyId != null) {
            return locacaoRepository.findByPropriedadeId(propertyId);
        }
        if (start != null && end != null) {
            return locacaoRepository.findByDateRange(start, end);
        }
        return locacaoRepository.findAll();
    }

    public Locacao findById(String id) {
        return locacaoRepository.findById(id)
                .orElseThrow(() -> new LocacaoNotFoundException("Locação não encontrada: " + id));
    }

    @Transactional
    public Locacao update(String id, UpdateLocacaoDTO data) {
        Locacao locacao = locacaoRepository.findById(id)
                .orElseThrow(() -> new LocacaoNotFoundException("Locação não encontrada: " + id));

        boolean datesChanged = data.checkIn() != null || data.checkOut() != null || data.propriedadeId() != null;
        if (datesChanged) {
            var checkIn = data.checkIn() != null ? data.checkIn() : locacao.getCheckIn();
            var checkOut = data.checkOut() != null ? data.checkOut() : locacao.getCheckOut();
            var propId = data.propriedadeId() != null ? data.propriedadeId() : locacao.getPropriedadeId();
            checkOverlap(propId, checkIn, checkOut, id);
        }

        locacao.updateValues(data);

        return locacaoRepository.save(locacao);
    }

    @Transactional
    public void deleteById(String id) {
        Locacao locacao = locacaoRepository.findById(id)
                .orElseThrow(() -> new LocacaoNotFoundException("Locação não encontrada: " + id));
        locacaoRepository.delete(locacao);
    }

    // ---- Recebimentos ----

    public List<RecebimentoLocacao> findRecebimentosByLocacaoId(String locacaoId) {
        return recebimentoRepository.findByLocacaoId(locacaoId);
    }

    public List<RecebimentoLocacao> findRecebimentosByMesAndAno(Integer mes, Integer ano) {
        return recebimentoRepository.findByMesAndAno(mes, ano);
    }

    @Transactional
    public RecebimentoLocacao upsertRecebimento(String locacaoId, Integer mes, Integer ano, BigDecimal valorRecebido) {
        var existing = recebimentoRepository.findByLocacaoIdAndMesAndAno(locacaoId, mes, ano);
        if (existing.isPresent()) {
            var rec = existing.get();
            rec.setValorRecebido(valorRecebido);
            return recebimentoRepository.save(rec);
        }
        var rec = new RecebimentoLocacao(locacaoId, mes, ano, valorRecebido);
        return recebimentoRepository.save(rec);
    }

    @Transactional
    public void deleteRecebimento(String locacaoId, Integer mes, Integer ano) {
        var existing = recebimentoRepository.findByLocacaoIdAndMesAndAno(locacaoId, mes, ano);
        existing.ifPresent(recebimentoRepository::delete);
    }

    private void checkOverlap(String propertyId, Instant checkIn, Instant checkOut, String excludeId) {
        // Checar conflito com outras locações
        var overlappingLocacoes = locacaoRepository.findByPropertyIdAndDateRange(propertyId, checkIn, checkOut);
        var hasLocacaoConflict = overlappingLocacoes.stream()
                .filter(l -> excludeId == null || !l.getId().equals(excludeId))
                .findAny()
                .isPresent();
        if (hasLocacaoConflict) {
            throw new BusinessRuleException("Já existe uma locação neste período para esta propriedade");
        }

        // Checar conflito com reservas
        var overlappingReservations = reservationRepository.findByPropertyIdAndDateRange(propertyId, checkIn, checkOut);
        var hasReservationConflict = overlappingReservations.stream()
                .filter(r -> r.getStatus() != ReservationStatus.cancelada)
                .findAny()
                .isPresent();
        if (hasReservationConflict) {
            throw new BusinessRuleException("Já existe uma reserva neste período para esta propriedade");
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // Roda todo dia à meia-noite
    @Transactional
    public void updateLocacaoStatuses() {
        var now = LocalDate.now(ZoneId.of("America/Sao_Paulo"))
                .atStartOfDay(ZoneId.of("America/Sao_Paulo"))
                .toInstant();

        // ativa -> encerrada quando hoje >= checkOut
        var toEnd = locacaoRepository.findByStatusAndCheckOutLessThanEqual(
                LocacaoStatus.ativa, now);
        for (var l : toEnd) {
            l.setStatus(LocacaoStatus.encerrada);
        }
        if (!toEnd.isEmpty()) {
            locacaoRepository.saveAll(toEnd);
        }
    }
}
