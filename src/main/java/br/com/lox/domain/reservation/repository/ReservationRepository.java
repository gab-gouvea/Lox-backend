package br.com.lox.domain.reservation.repository;

import br.com.lox.domain.reservation.entity.Reservation;
import br.com.lox.domain.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findByPropriedadeId(String propriedadeId);

    @Query("SELECT r FROM Reservation r WHERE r.checkOut > :start AND r.checkIn < :end")
    List<Reservation> findByDateRange(@Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT r FROM Reservation r WHERE r.propriedadeId = :propertyId AND r.checkOut > :start AND r.checkIn < :end")
    List<Reservation> findByPropertyIdAndDateRange(@Param("propertyId") String propertyId,
                                                    @Param("start") Instant start,
                                                    @Param("end") Instant end);

    List<Reservation> findByStatusAndCheckInLessThanEqual(ReservationStatus status, Instant checkIn);

    List<Reservation> findByStatusAndCheckOutLessThanEqual(ReservationStatus status, Instant checkOut);
}
