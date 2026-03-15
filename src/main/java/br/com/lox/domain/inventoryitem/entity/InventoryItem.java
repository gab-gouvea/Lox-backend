package br.com.lox.domain.inventoryitem.entity;

import br.com.lox.domain.inventoryitem.dto.UpdateInventoryItemDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "itens_inventario")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String propriedadeId;

    @Column(nullable = false)
    private String comodo;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer quantidade;

    private String descricao;

    private String imagemUrl;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant atualizadoEm;

    public InventoryItem(String propriedadeId, String comodo, String nome, Integer quantidade,
                         String descricao, String imagemUrl) {
        this.propriedadeId = propriedadeId;
        this.comodo = comodo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.descricao = blankToNull(descricao);
        this.imagemUrl = blankToNull(imagemUrl);
    }

    public void updateValues(UpdateInventoryItemDTO data) {
        if (data.comodo() != null) this.comodo = data.comodo();
        if (data.nome() != null) this.nome = data.nome();
        if (data.quantidade() != null) this.quantidade = data.quantidade();
        if (data.descricao() != null) this.descricao = blankToNull(data.descricao());
        if (data.imagemUrl() != null) this.imagemUrl = blankToNull(data.imagemUrl());
    }

    private String blankToNull(String value) {
        return (value != null && value.isBlank()) ? null : value;
    }
}
