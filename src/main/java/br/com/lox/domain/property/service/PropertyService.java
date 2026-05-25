package br.com.lox.domain.property.service;

import br.com.lox.domain.locacao.entity.Locacao;
import br.com.lox.domain.locacao.repository.LocacaoRepository;
import br.com.lox.domain.property.dto.CreatePropertyDTO;
import br.com.lox.domain.property.dto.LockTaxaResultDTO;
import br.com.lox.domain.property.dto.UpdatePropertyDTO;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.domain.reservation.entity.Reservation;
import br.com.lox.domain.reservation.repository.ReservationRepository;
import br.com.lox.exceptions.PropertyNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final ReservationRepository reservationRepository;
    private final LocacaoRepository locacaoRepository;

    public PropertyService(PropertyRepository propertyRepository,
                           ReservationRepository reservationRepository,
                           LocacaoRepository locacaoRepository) {
        this.propertyRepository = propertyRepository;
        this.reservationRepository = reservationRepository;
        this.locacaoRepository = locacaoRepository;
    }

    @Transactional
    public Property create(CreatePropertyDTO data) {
        var entity = new Property(
                data.nome(),
                data.endereco(),
                data.proprietarioId(),
                data.tipo(),
                data.quartos(),
                data.fotoCapa(),
                data.percentualComissao(),
                data.taxaLimpeza(),
                data.temHobbyBox(),
                data.acessoPredio(),
                data.acessoApartamento(),
                data.senhaWifi(),
                data.ativo()
        );

        return propertyRepository.save(entity);
    }

    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    public Property findById(String id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não encontrada no sistema: " + id));
    }

    @Transactional
    public Property update(String id, UpdatePropertyDTO data) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não encontrada no sistema: " + id));

        property.updateValues(data);

        return propertyRepository.save(property);
    }

    @Transactional
    public void deleteById(String id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não encontrada no sistema: " + id));

        propertyRepository.delete(property);
    }

    /**
     * Trava a taxaLimpeza atual da propriedade em todas as reservas e locações passadas
     * (checkOut < agora) que ainda não têm taxaLimpeza definida. Após esta operação, essas
     * reservas/locações deixam de depender do valor dinâmico da propriedade nos relatórios.
     */
    @Transactional
    public LockTaxaResultDTO lockTaxaLimpezaPast(String propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não encontrada no sistema: " + propertyId));

        Instant now = Instant.now();
        var taxa = property.getTaxaLimpeza();

        if (taxa == null) {
            return new LockTaxaResultDTO(0, 0, null);
        }

        List<Reservation> reservations = reservationRepository.findByPropriedadeId(propertyId);
        int reservationsLocked = 0;
        for (Reservation r : reservations) {
            if (r.getTaxaLimpeza() == null && r.getCheckOut() != null && r.getCheckOut().isBefore(now)) {
                r.setTaxaLimpeza(taxa);
                reservationsLocked++;
            }
        }

        List<Locacao> locacoes = locacaoRepository.findByPropriedadeId(propertyId);
        int locacoesLocked = 0;
        for (Locacao l : locacoes) {
            if (l.getTaxaLimpeza() == null && l.getCheckOut() != null && l.getCheckOut().isBefore(now)) {
                l.setTaxaLimpeza(taxa);
                locacoesLocked++;
            }
        }

        return new LockTaxaResultDTO(reservationsLocked, locacoesLocked, taxa);
    }
}
