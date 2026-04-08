package br.com.lox.domain.reservation.service;

import br.com.lox.domain.locacao.repository.LocacaoRepository;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.domain.reservation.dto.CreateReservationDTO;
import br.com.lox.domain.reservation.dto.UpdateReservationDTO;
import br.com.lox.domain.reservation.entity.Despesa;
import br.com.lox.domain.reservation.entity.Reservation;
import br.com.lox.domain.reservation.entity.ReservationStatus;
import br.com.lox.domain.reservation.repository.ReservationRepository;
import br.com.lox.exceptions.BusinessRuleException;
import br.com.lox.exceptions.ReservationNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PropertyRepository propertyRepository;
    private final LocacaoRepository locacaoRepository;

    public ReservationService(ReservationRepository reservationRepository, PropertyRepository propertyRepository, LocacaoRepository locacaoRepository) {
        this.reservationRepository = reservationRepository;
        this.propertyRepository = propertyRepository;
        this.locacaoRepository = locacaoRepository;
    }

    @Transactional
    public Reservation create(CreateReservationDTO data) {
        if (data.checkOut().isBefore(data.checkIn()) || data.checkOut().equals(data.checkIn())) {
            throw new BusinessRuleException("Check-out deve ser após o check-in");
        }

        checkOverlap(data.propriedadeId(), data.checkIn(), data.checkOut(), null);

        List<Despesa> despesas = new ArrayList<>();
        if (data.despesas() != null) {
            despesas = data.despesas().stream()
                    .map(d -> new Despesa(d.descricao(), d.valor(), d.reembolsavel(), d.mes(), d.ano()))
                    .toList();
        }

        var entity = new Reservation(
                data.propriedadeId(),
                data.nomeHospede(),
                data.checkIn(),
                data.checkOut(),
                data.status(),
                data.precoTotal(),
                data.notas(),
                data.fonte(),
                data.numHospedes(),
                data.faxinaStatus(),
                data.faxinaPorMim(),
                data.custoEmpresaFaxina(),
                data.faxinaPaga(),
                data.faxinaData(),
                despesas
        );

        propertyRepository.findById(data.propriedadeId())
                .ifPresent(property -> entity.setPercentualComissao(property.getPercentualComissao()));

        return reservationRepository.save(entity);
    }

    public List<Reservation> findAll(String propertyId, Instant start, Instant end) {
        if (propertyId != null && start != null && end != null) {
            return reservationRepository.findByPropertyIdAndDateRange(propertyId, start, end);
        }
        if (propertyId != null) {
            return reservationRepository.findByPropriedadeId(propertyId);
        }
        if (start != null && end != null) {
            return reservationRepository.findByDateRange(start, end);
        }
        return reservationRepository.findAll();
    }

    public Reservation findById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada no sistema: " + id));
    }

    @Transactional
    public Reservation update(String id, UpdateReservationDTO data) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada no sistema: " + id));

        // Verificar overlap apenas quando datas, propriedade ou status mudam
        boolean datesChanged = data.checkIn() != null || data.checkOut() != null || data.propriedadeId() != null;
        boolean statusChanged = data.status() != null;
        if (datesChanged || statusChanged) {
            var statusAfterUpdate = data.status() != null ? data.status() : reservation.getStatus();
            if (statusAfterUpdate != ReservationStatus.cancelada) {
                var checkIn = data.checkIn() != null ? data.checkIn() : reservation.getCheckIn();
                var checkOut = data.checkOut() != null ? data.checkOut() : reservation.getCheckOut();
                var propId = data.propriedadeId() != null ? data.propriedadeId() : reservation.getPropriedadeId();
                checkOverlap(propId, checkIn, checkOut, id);
            }
        }

        reservation.updateValues(data);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteById(String id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada no sistema: " + id));

        reservationRepository.delete(reservation);
    }

    private void checkOverlap(String propertyId, Instant checkIn, Instant checkOut, String excludeId) {
        // Checar conflito com outras reservas
        var overlapping = reservationRepository.findByPropertyIdAndDateRange(propertyId, checkIn, checkOut);
        var hasConflict = overlapping.stream()
                .filter(r -> r.getStatus() != ReservationStatus.cancelada)
                .filter(r -> excludeId == null || !r.getId().equals(excludeId))
                .findAny()
                .isPresent();
        if (hasConflict) {
            throw new BusinessRuleException("Já existe uma reserva neste período para esta propriedade");
        }

        // Checar conflito com locações
        var overlappingLocacoes = locacaoRepository.findByPropertyIdAndDateRange(propertyId, checkIn, checkOut);
        var hasLocacaoConflict = overlappingLocacoes.stream()
                .findAny()
                .isPresent();
        if (hasLocacaoConflict) {
            throw new BusinessRuleException("Já existe uma locação neste período para esta propriedade");
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // Roda todo dia à meia-noite
    @Transactional
    public void updateReservationStatuses() {
        var now = LocalDate.now(ZoneId.of("America/Sao_Paulo"))
                .atStartOfDay(ZoneId.of("America/Sao_Paulo"))
                .toInstant();

        // confirmada -> em_andamento quando hoje >= checkIn
        var toStart = reservationRepository.findByStatusAndCheckInLessThanEqual(
                ReservationStatus.confirmada, now);
        for (var r : toStart) {
            r.setStatus(ReservationStatus.em_andamento);
        }
        if (!toStart.isEmpty()) {
            reservationRepository.saveAll(toStart);
        }

        // em_andamento -> concluida quando hoje >= checkOut
        var toComplete = reservationRepository.findByStatusAndCheckOutLessThanEqual(
                ReservationStatus.em_andamento, now);
        for (var r : toComplete) {
            r.setStatus(ReservationStatus.concluida);
        }
        if (!toComplete.isEmpty()) {
            reservationRepository.saveAll(toComplete);
        }
    }
}
