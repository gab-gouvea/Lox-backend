package br.com.lox.domain.proprietario.service;

import br.com.lox.domain.proprietario.dto.CreateProprietarioDTO;
import br.com.lox.domain.proprietario.dto.UpdateProprietarioDTO;
import br.com.lox.domain.proprietario.entity.Proprietario;
import br.com.lox.domain.proprietario.repository.ProprietarioRepository;
import br.com.lox.exceptions.BusinessRuleException;
import br.com.lox.exceptions.ProprietarioNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProprietarioService {

    private final ProprietarioRepository proprietarioRepository;

    public ProprietarioService(ProprietarioRepository proprietarioRepository) {
        this.proprietarioRepository = proprietarioRepository;
    }

    @Transactional
    public Proprietario create(CreateProprietarioDTO data) {
        if (proprietarioRepository.existsByCpf(data.cpf())) {
            throw new BusinessRuleException("Proprietário já cadastrado com este CPF");
        }

        var entity = new Proprietario(
                data.nomeCompleto(),
                data.cpf(),
                data.rg(),
                data.dataNascimento(),
                data.profissao(),
                data.estadoCivil(),
                data.endereco(),
                data.email()
        );

        return proprietarioRepository.save(entity);
    }

    public List<Proprietario> findAll() {
        return proprietarioRepository.findAll();
    }

    public Proprietario findById(String id) {
        return proprietarioRepository.findById(id)
                .orElseThrow(() -> new ProprietarioNotFoundException("Proprietário não encontrado no sistema: " + id));
    }

    @Transactional
    public Proprietario update(String id, UpdateProprietarioDTO data) {
        Proprietario proprietario = proprietarioRepository.findById(id)
                .orElseThrow(() -> new ProprietarioNotFoundException("Proprietário não encontrado no sistema: " + id));

        proprietario.updateValues(data);

        return proprietarioRepository.save(proprietario);
    }

    @Transactional
    public void deleteById(String id) {
        Proprietario proprietario = proprietarioRepository.findById(id)
                .orElseThrow(() -> new ProprietarioNotFoundException("Proprietário não encontrado no sistema: " + id));

        proprietarioRepository.delete(proprietario);
    }
}
