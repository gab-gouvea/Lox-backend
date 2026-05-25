package br.com.lox.domain.property.service;

import br.com.lox.domain.locacao.entity.Locacao;
import br.com.lox.domain.locacao.entity.LocacaoStatus;
import br.com.lox.domain.locacao.repository.LocacaoRepository;
import br.com.lox.domain.property.dto.LockTaxaResultDTO;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.entity.PropertyType;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.domain.reservation.entity.Reservation;
import br.com.lox.domain.reservation.entity.ReservationSource;
import br.com.lox.domain.reservation.entity.ReservationStatus;
import br.com.lox.domain.reservation.repository.ReservationRepository;
import br.com.lox.exceptions.PropertyNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropertyServiceLockTaxaTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private LocacaoRepository locacaoRepository;

    @InjectMocks
    private PropertyService propertyService;

    private static final String PROPERTY_ID = "p1";
    private static final BigDecimal TAXA_ATUAL = new BigDecimal("150.00");

    private Property makeProperty(BigDecimal taxa) {
        Property p = new Property("Apto", "Rua A", "owner1", PropertyType.apartamento, 2, null,
                new BigDecimal("20"), taxa, false, null, null, null, true);
        ReflectionTestUtils.setField(p, "id", PROPERTY_ID);
        return p;
    }

    private Reservation makeReservation(String id, String propId, Instant checkOut, BigDecimal taxa) {
        Reservation r = new Reservation(
                propId, "João",
                checkOut.minus(2, ChronoUnit.DAYS), checkOut,
                ReservationStatus.confirmada, new BigDecimal("1000"), null,
                ReservationSource.airbnb, 2,
                null, false, null, null, null, null
        );
        ReflectionTestUtils.setField(r, "id", id);
        if (taxa != null) r.setTaxaLimpeza(taxa);
        return r;
    }

    private Locacao makeLocacao(String id, String propId, Instant checkOut, BigDecimal taxa) {
        Locacao l = new Locacao(
                propId, "temporada", "Maria", "12345678901", null,
                null, null, null, null, null,
                checkOut.minus(30, ChronoUnit.DAYS), checkOut,
                1, new BigDecimal("2000"), "mensal",
                null, new BigDecimal("20"), null,
                null, null, LocacaoStatus.encerrada
        );
        ReflectionTestUtils.setField(l, "id", id);
        if (taxa != null) l.setTaxaLimpeza(taxa);
        return l;
    }

    private Instant pastDate() {
        return Instant.now().minus(30, ChronoUnit.DAYS);
    }

    private Instant futureDate() {
        return Instant.now().plus(30, ChronoUnit.DAYS);
    }

    // --------------- Casos felizes ---------------

    @Test
    void travaTaxaEmReservaPassadaComTaxaNull() {
        Property prop = makeProperty(TAXA_ATUAL);
        Reservation res = makeReservation("r1", PROPERTY_ID, pastDate(), null);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));
        when(reservationRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of(res));
        when(locacaoRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertEquals(1, result.reservationsLocked());
        assertEquals(0, result.locacoesLocked());
        assertEquals(TAXA_ATUAL, result.taxaLimpezaAplicada());
        assertEquals(TAXA_ATUAL, res.getTaxaLimpeza());
    }

    @Test
    void travaTaxaEmLocacaoPassadaComTaxaNull() {
        Property prop = makeProperty(TAXA_ATUAL);
        Locacao loc = makeLocacao("l1", PROPERTY_ID, pastDate(), null);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));
        when(reservationRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());
        when(locacaoRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of(loc));

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertEquals(0, result.reservationsLocked());
        assertEquals(1, result.locacoesLocked());
        assertEquals(TAXA_ATUAL, loc.getTaxaLimpeza());
    }

    @Test
    void travaTaxaEmMixDeReservasELocacoes() {
        Property prop = makeProperty(TAXA_ATUAL);
        Reservation r1 = makeReservation("r1", PROPERTY_ID, pastDate(), null);
        Reservation r2 = makeReservation("r2", PROPERTY_ID, pastDate(), null);
        Locacao l1 = makeLocacao("l1", PROPERTY_ID, pastDate(), null);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));
        when(reservationRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of(r1, r2));
        when(locacaoRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of(l1));

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertEquals(2, result.reservationsLocked());
        assertEquals(1, result.locacoesLocked());
        assertEquals(TAXA_ATUAL, r1.getTaxaLimpeza());
        assertEquals(TAXA_ATUAL, r2.getTaxaLimpeza());
        assertEquals(TAXA_ATUAL, l1.getTaxaLimpeza());
    }

    // --------------- Filtros: não trava em casos errados ---------------

    @Test
    void naoTravaReservasFuturas() {
        Property prop = makeProperty(TAXA_ATUAL);
        Reservation res = makeReservation("r1", PROPERTY_ID, futureDate(), null);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));
        when(reservationRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of(res));
        when(locacaoRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertEquals(0, result.reservationsLocked());
        assertNull(res.getTaxaLimpeza());
    }

    @Test
    void naoTravaLocacoesFuturas() {
        Property prop = makeProperty(TAXA_ATUAL);
        Locacao loc = makeLocacao("l1", PROPERTY_ID, futureDate(), null);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));
        when(reservationRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());
        when(locacaoRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of(loc));

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertEquals(0, result.locacoesLocked());
        assertNull(loc.getTaxaLimpeza());
    }

    @Test
    void naoTravaReservasJaComTaxaDefinida() {
        Property prop = makeProperty(TAXA_ATUAL);
        BigDecimal taxaOriginal = new BigDecimal("80.00");
        Reservation res = makeReservation("r1", PROPERTY_ID, pastDate(), taxaOriginal);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));
        when(reservationRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of(res));
        when(locacaoRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertEquals(0, result.reservationsLocked());
        // preserva valor original
        assertEquals(taxaOriginal, res.getTaxaLimpeza());
    }

    @Test
    void naoTravaLocacoesJaComTaxaDefinida() {
        Property prop = makeProperty(TAXA_ATUAL);
        BigDecimal taxaOriginal = new BigDecimal("100.00");
        Locacao loc = makeLocacao("l1", PROPERTY_ID, pastDate(), taxaOriginal);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));
        when(reservationRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());
        when(locacaoRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of(loc));

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertEquals(0, result.locacoesLocked());
        assertEquals(taxaOriginal, loc.getTaxaLimpeza());
    }

    @Test
    void filtraCorretamenteMisturandoPassadasFuturasETravadas() {
        Property prop = makeProperty(TAXA_ATUAL);
        Reservation passadaNull = makeReservation("r1", PROPERTY_ID, pastDate(), null);
        Reservation passadaTravada = makeReservation("r2", PROPERTY_ID, pastDate(), new BigDecimal("50"));
        Reservation futuraNull = makeReservation("r3", PROPERTY_ID, futureDate(), null);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));
        when(reservationRepository.findByPropriedadeId(PROPERTY_ID))
                .thenReturn(List.of(passadaNull, passadaTravada, futuraNull));
        when(locacaoRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertEquals(1, result.reservationsLocked());
        assertEquals(TAXA_ATUAL, passadaNull.getTaxaLimpeza());
        assertEquals(new BigDecimal("50"), passadaTravada.getTaxaLimpeza());
        assertNull(futuraNull.getTaxaLimpeza());
    }

    // --------------- Edge cases ---------------

    @Test
    void retornaZeroQuandoPropriedadeSemTaxaLimpeza() {
        Property prop = makeProperty(null);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertEquals(0, result.reservationsLocked());
        assertEquals(0, result.locacoesLocked());
        assertNull(result.taxaLimpezaAplicada());
    }

    @Test
    void lancaExcecaoQuandoPropriedadeNaoExiste() {
        when(propertyRepository.findById("inexistente")).thenReturn(Optional.empty());

        assertThrows(PropertyNotFoundException.class,
                () -> propertyService.lockTaxaLimpezaPast("inexistente"));
    }

    @Test
    void retornaZeroQuandoNaoHaReservasNemLocacoes() {
        Property prop = makeProperty(TAXA_ATUAL);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));
        when(reservationRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());
        when(locacaoRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertEquals(0, result.reservationsLocked());
        assertEquals(0, result.locacoesLocked());
        assertEquals(TAXA_ATUAL, result.taxaLimpezaAplicada());
    }

    @Test
    void retornaTaxaAplicadaParaConfirmacaoNoUI() {
        Property prop = makeProperty(TAXA_ATUAL);

        when(propertyRepository.findById(PROPERTY_ID)).thenReturn(Optional.of(prop));
        when(reservationRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());
        when(locacaoRepository.findByPropriedadeId(PROPERTY_ID)).thenReturn(List.of());

        LockTaxaResultDTO result = propertyService.lockTaxaLimpezaPast(PROPERTY_ID);

        assertNotNull(result.taxaLimpezaAplicada());
        assertEquals(0, TAXA_ATUAL.compareTo(result.taxaLimpezaAplicada()));
    }
}
