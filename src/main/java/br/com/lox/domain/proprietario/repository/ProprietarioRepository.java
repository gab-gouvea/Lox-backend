package br.com.lox.domain.proprietario.repository;

import br.com.lox.domain.proprietario.entity.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProprietarioRepository extends JpaRepository<Proprietario, String> {
    boolean existsByCpf(String cpf);
}
