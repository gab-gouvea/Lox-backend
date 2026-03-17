package br.com.lox.domain.reservation.entity;

import br.com.lox.domain.reservation.dto.UpdateReservationDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservas")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String propriedadeId;

    @Column(nullable = false)
    private String nomeHospede;

    @Column(nullable = false)
    private Instant checkIn;

    @Column(nullable = false)
    private Instant checkOut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private BigDecimal precoTotal;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationSource fonte;

    @Column(nullable = false)
    private Integer numHospedes;

    @Enumerated(EnumType.STRING)
    private FaxinaStatus faxinaStatus;

    @Column(nullable = false)
    private Boolean faxinaPorMim;

    private BigDecimal custoEmpresaFaxina;

    private Boolean faxinaPaga;

    private Instant faxinaData;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reservation_id")
    private List<Despesa> despesas = new ArrayList<>();

    private BigDecimal valorRecebidoCancelamento;

    private BigDecimal percentualComissao;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant atualizadoEm;

    public Reservation(String propriedadeId, String nomeHospede, Instant checkIn, Instant checkOut,
                       ReservationStatus status, BigDecimal precoTotal, String notas,
                       ReservationSource fonte, Integer numHospedes, FaxinaStatus faxinaStatus,
                       Boolean faxinaPorMim, BigDecimal custoEmpresaFaxina, Boolean faxinaPaga,
                       Instant faxinaData, List<Despesa> despesas) {
        this.propriedadeId = propriedadeId;
        this.nomeHospede = nomeHospede;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.precoTotal = precoTotal;
        this.notas = notas;
        this.fonte = fonte;
        this.numHospedes = numHospedes;
        this.faxinaStatus = faxinaStatus;
        this.faxinaPorMim = faxinaPorMim;
        this.custoEmpresaFaxina = custoEmpresaFaxina;
        this.faxinaPaga = faxinaPaga;
        this.faxinaData = faxinaData;
        this.despesas = despesas != null ? despesas : new ArrayList<>();
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setPercentualComissao(BigDecimal percentualComissao) {
        this.percentualComissao = percentualComissao;
    }

    public void updateValues(UpdateReservationDTO data) {
        if (data.propriedadeId() != null) this.propriedadeId = data.propriedadeId();
        if (data.nomeHospede() != null) this.nomeHospede = data.nomeHospede();
        if (data.checkIn() != null) this.checkIn = data.checkIn();
        if (data.checkOut() != null) this.checkOut = data.checkOut();
        if (data.status() != null) this.status = data.status();
        if (data.precoTotal() != null) this.precoTotal = data.precoTotal();
        if (data.notas() != null) this.notas = data.notas();
        if (data.fonte() != null) this.fonte = data.fonte();
        if (data.numHospedes() != null) this.numHospedes = data.numHospedes();
        if (data.faxinaStatus() != null) this.faxinaStatus = data.faxinaStatus();
        if (data.faxinaPorMim() != null) this.faxinaPorMim = data.faxinaPorMim();
        if (data.custoEmpresaFaxina() != null) this.custoEmpresaFaxina = data.custoEmpresaFaxina();
        if (data.faxinaPaga() != null) this.faxinaPaga = data.faxinaPaga();
        if (data.faxinaData() != null) this.faxinaData = data.faxinaData();
        if (data.valorRecebidoCancelamento() != null) this.valorRecebidoCancelamento = data.valorRecebidoCancelamento();

        if (data.despesas() != null) {
            this.despesas.clear();
            List<Despesa> novasDespesas = data.despesas().stream()
                    .map(d -> new Despesa(d.descricao(), d.valor(), d.reembolsavel()))
                    .toList();
            this.despesas.addAll(novasDespesas);
        }
    }
}
