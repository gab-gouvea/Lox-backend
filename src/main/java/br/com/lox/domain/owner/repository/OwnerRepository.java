package br.com.lox.domain.owner.repository;

import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, String> {
}
