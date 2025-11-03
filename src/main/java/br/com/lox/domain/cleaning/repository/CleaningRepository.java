package br.com.lox.domain.cleaning.repository;

import br.com.lox.domain.cleaning.entity.Cleaning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CleaningRepository extends JpaRepository<Cleaning, String> {
}
