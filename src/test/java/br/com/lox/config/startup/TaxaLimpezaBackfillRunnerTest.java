package br.com.lox.config.startup;

import br.com.lox.domain.locacao.entity.Locacao;
import br.com.lox.domain.locacao.entity.LocacaoStatus;
import br.com.lox.domain.locacao.repository.LocacaoRepository;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.entity.PropertyType;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.domain.reservation.entity.Reservation;
import br.com.lox.domain.reservation.entity.ReservationSource;
import br.com.lox.domain.reservation.entity.ReservationStatus;
import br.com.lox.domain.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxaLimpezaBackfillRunnerTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private LocacaoRepository locacaoRepository;

    @Mock
    private ApplicationArguments applicationArguments;

    @InjectMocks
    private TaxaLimpezaBackfillRunner runner;

    private Property makeProperty(String id, BigDecimal taxa) {
        Property p = new Property("Apto " + id, "Rua", "owner", PropertyType.apartamento, 2, null,
                new BigDecimal("20"), taxa, false, null, null, null, true);
        ReflectionTestUtils.setField(p, "id", id);
        return p;
    }

    private Reservation makeReservation(String id, String propId, BigDecimal taxa) {
        Reservation r = new Reservation(
                propId, "João",
                Instant.now().minus(30, ChronoUnit.DAYS),
                Instant.now().minus(25, ChronoUnit.DAYS),
                ReservationStatus.concluida, new BigDecimal("1000"), null,
                ReservationSource.airbnb, 2,
                null, false, null, null, null, null, null
        );
        ReflectionTestUtils.setField(r, "id", id);
        if (taxa != null) r.setTaxaLimpeza(taxa);
        return r;
    }

    private Locacao makeLocacao(String id, String propId, BigDecimal taxa) {
        Locacao l = new Locacao(
                propId, "temporada", "Maria", "12345678901", null,
                null, null, null, null, null,
                Instant.now().minus(60, ChronoUnit.DAYS),
                Instant.now().minus(30, ChronoUnit.DAYS),
                1, new BigDecimal("2000"), "mensal",
                null, new BigDecimal("20"), null,
                null, null, LocacaoStatus.encerrada
        );
        ReflectionTestUtils.setField(l, "id", id);
        if (taxa != null) l.setTaxaLimpeza(taxa);
        return l;
    }

    @Test
    void copiaTaxaDaPropriedadeParaReservaSemTaxa() {
        Property prop = makeProperty("p1", new BigDecimal("150"));
        Reservation res = makeReservation("r1", "p1", null);

        when(propertyRepository.findAll()).thenReturn(List.of(prop));
        when(reservationRepository.findAll()).thenReturn(List.of(res));
        when(locacaoRepository.findAll()).thenReturn(List.of());

        runner.run(applicationArguments);

        assertEquals(new BigDecimal("150"), res.getTaxaLimpeza());
    }

    @Test
    void copiaTaxaParaLocacaoSemTaxa() {
        Property prop = makeProperty("p1", new BigDecimal("150"));
        Locacao loc = makeLocacao("l1", "p1", null);

        when(propertyRepository.findAll()).thenReturn(List.of(prop));
        when(reservationRepository.findAll()).thenReturn(List.of());
        when(locacaoRepository.findAll()).thenReturn(List.of(loc));

        runner.run(applicationArguments);

        assertEquals(new BigDecimal("150"), loc.getTaxaLimpeza());
    }

    @Test
    void naoAlteraReservasJaComTaxa() {
        Property prop = makeProperty("p1", new BigDecimal("150"));
        BigDecimal taxaOriginal = new BigDecimal("80");
        Reservation res = makeReservation("r1", "p1", taxaOriginal);

        when(propertyRepository.findAll()).thenReturn(List.of(prop));
        when(reservationRepository.findAll()).thenReturn(List.of(res));
        when(locacaoRepository.findAll()).thenReturn(List.of());

        runner.run(applicationArguments);

        // preserva valor antigo, não sobrescreve
        assertEquals(taxaOriginal, res.getTaxaLimpeza());
    }

    @Test
    void naoAlteraLocacoesJaComTaxa() {
        Property prop = makeProperty("p1", new BigDecimal("150"));
        BigDecimal taxaOriginal = new BigDecimal("80");
        Locacao loc = makeLocacao("l1", "p1", taxaOriginal);

        when(propertyRepository.findAll()).thenReturn(List.of(prop));
        when(reservationRepository.findAll()).thenReturn(List.of());
        when(locacaoRepository.findAll()).thenReturn(List.of(loc));

        runner.run(applicationArguments);

        assertEquals(taxaOriginal, loc.getTaxaLimpeza());
    }

    @Test
    void naoFazNadaQuandoPropriedadeTemTaxaNull() {
        Property prop = makeProperty("p1", null);
        Reservation res = makeReservation("r1", "p1", null);

        when(propertyRepository.findAll()).thenReturn(List.of(prop));
        when(reservationRepository.findAll()).thenReturn(List.of(res));
        when(locacaoRepository.findAll()).thenReturn(List.of());

        runner.run(applicationArguments);

        // Sem taxa na propriedade, não há o que copiar
        assertNull(res.getTaxaLimpeza());
    }

    @Test
    void ignoraReservaQuandoPropriedadeNaoExiste() {
        Reservation res = makeReservation("r1", "p_inexistente", null);

        when(propertyRepository.findAll()).thenReturn(List.of());
        when(reservationRepository.findAll()).thenReturn(List.of(res));
        when(locacaoRepository.findAll()).thenReturn(List.of());

        runner.run(applicationArguments);

        assertNull(res.getTaxaLimpeza());
    }

    @Test
    void usaTaxaCorretaParaCadaPropriedade() {
        Property p1 = makeProperty("p1", new BigDecimal("100"));
        Property p2 = makeProperty("p2", new BigDecimal("200"));
        Reservation r1 = makeReservation("r1", "p1", null);
        Reservation r2 = makeReservation("r2", "p2", null);

        when(propertyRepository.findAll()).thenReturn(List.of(p1, p2));
        when(reservationRepository.findAll()).thenReturn(List.of(r1, r2));
        when(locacaoRepository.findAll()).thenReturn(List.of());

        runner.run(applicationArguments);

        assertEquals(new BigDecimal("100"), r1.getTaxaLimpeza());
        assertEquals(new BigDecimal("200"), r2.getTaxaLimpeza());
    }

    @Test
    void rodaIdempotenteEmListaVazia() {
        when(propertyRepository.findAll()).thenReturn(List.of());
        when(reservationRepository.findAll()).thenReturn(List.of());
        when(locacaoRepository.findAll()).thenReturn(List.of());

        runner.run(applicationArguments);
        // Sem exceção, idempotente
    }

    @Test
    void processaMixDeReservasComEsemTaxa() {
        Property prop = makeProperty("p1", new BigDecimal("150"));
        Reservation semTaxa = makeReservation("r1", "p1", null);
        Reservation comTaxa = makeReservation("r2", "p1", new BigDecimal("99"));

        when(propertyRepository.findAll()).thenReturn(List.of(prop));
        when(reservationRepository.findAll()).thenReturn(List.of(semTaxa, comTaxa));
        when(locacaoRepository.findAll()).thenReturn(List.of());

        runner.run(applicationArguments);

        assertEquals(new BigDecimal("150"), semTaxa.getTaxaLimpeza());
        assertEquals(new BigDecimal("99"), comTaxa.getTaxaLimpeza()); // preservada
    }
}
