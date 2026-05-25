package br.com.lox.config.startup;

import br.com.lox.domain.locacao.entity.Locacao;
import br.com.lox.domain.locacao.repository.LocacaoRepository;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.domain.reservation.entity.Reservation;
import br.com.lox.domain.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Copia a taxaLimpeza atual da propriedade em todas as reservas e locações que ainda
 * estejam com taxaLimpeza null. Roda a cada startup, idempotente — após a primeira
 * execução, novas reservas já são criadas com a taxa copiada via service e este runner
 * fica como segurança.
 */
@Component
public class TaxaLimpezaBackfillRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(TaxaLimpezaBackfillRunner.class);

    private final PropertyRepository propertyRepository;
    private final ReservationRepository reservationRepository;
    private final LocacaoRepository locacaoRepository;

    public TaxaLimpezaBackfillRunner(PropertyRepository propertyRepository,
                                     ReservationRepository reservationRepository,
                                     LocacaoRepository locacaoRepository) {
        this.propertyRepository = propertyRepository;
        this.reservationRepository = reservationRepository;
        this.locacaoRepository = locacaoRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Map<String, BigDecimal> taxaPorPropriedade = new HashMap<>();
        for (Property p : propertyRepository.findAll()) {
            if (p.getTaxaLimpeza() != null) {
                taxaPorPropriedade.put(p.getId(), p.getTaxaLimpeza());
            }
        }

        int reservasAtualizadas = 0;
        for (Reservation r : reservationRepository.findAll()) {
            if (r.getTaxaLimpeza() == null) {
                BigDecimal taxa = taxaPorPropriedade.get(r.getPropriedadeId());
                if (taxa != null) {
                    r.setTaxaLimpeza(taxa);
                    reservasAtualizadas++;
                }
            }
        }

        int locacoesAtualizadas = 0;
        for (Locacao l : locacaoRepository.findAll()) {
            if (l.getTaxaLimpeza() == null) {
                BigDecimal taxa = taxaPorPropriedade.get(l.getPropriedadeId());
                if (taxa != null) {
                    l.setTaxaLimpeza(taxa);
                    locacoesAtualizadas++;
                }
            }
        }

        if (reservasAtualizadas > 0 || locacoesAtualizadas > 0) {
            log.info("TaxaLimpezaBackfill: travada em {} reservas e {} locações", reservasAtualizadas, locacoesAtualizadas);
        }
    }
}
