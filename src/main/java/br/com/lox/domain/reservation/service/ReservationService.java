package br.com.lox.domain.reservation.service;

import br.com.lox.domain.reservation.dto.CreateReservationDTO;
import br.com.lox.domain.reservation.dto.UpdateReservationDTO;
import br.com.lox.domain.reservation.entity.Despesa;
import br.com.lox.domain.reservation.entity.Reservation;
import br.com.lox.domain.reservation.repository.ReservationRepository;
import br.com.lox.exceptions.ReservationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation create(CreateReservationDTO data) {
        List<Despesa> despesas = new ArrayList<>();
        if (data.despesas() != null) {
            despesas = data.despesas().stream()
                    .map(d -> new Despesa(d.descricao(), d.valor(), d.reembolsavel()))
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

        reservation.updateValues(data);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteById(String id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada no sistema: " + id));

        reservationRepository.delete(reservation);
    }
}
