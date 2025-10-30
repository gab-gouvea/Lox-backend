package br.com.lox.domain.rental.repository;

import br.com.lox.domain.rental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, String> {
}
