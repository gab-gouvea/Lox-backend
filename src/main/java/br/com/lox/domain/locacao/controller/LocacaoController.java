package br.com.lox.domain.locacao.controller;

import br.com.lox.domain.locacao.dto.CreateLocacaoDTO;
import br.com.lox.domain.locacao.dto.UpdateLocacaoDTO;
import br.com.lox.domain.locacao.entity.Locacao;
import br.com.lox.domain.locacao.entity.RecebimentoLocacao;
import br.com.lox.domain.locacao.service.LocacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locacoes")
public class LocacaoController {

    private final LocacaoService locacaoService;

    public LocacaoController(LocacaoService locacaoService) {
        this.locacaoService = locacaoService;
    }

    @PostMapping
    public ResponseEntity<Locacao> create(@RequestBody @Valid CreateLocacaoDTO data) {
        Locacao locacao = locacaoService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(locacao);
    }

    @GetMapping
    public ResponseEntity<List<Locacao>> findAll(
            @RequestParam(required = false) String propertyId,
            @RequestParam(required = false) Instant start,
            @RequestParam(required = false) Instant end) {
        List<Locacao> locacoes = locacaoService.findAll(propertyId, start, end);
        return ResponseEntity.ok(locacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locacao> findById(@PathVariable String id) {
        Locacao locacao = locacaoService.findById(id);
        return ResponseEntity.ok(locacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Locacao> update(@PathVariable String id, @RequestBody @Valid UpdateLocacaoDTO data) {
        Locacao locacao = locacaoService.update(id, data);
        return ResponseEntity.ok(locacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        locacaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Recebimentos ----

    @GetMapping("/{id}/recebimentos")
    public ResponseEntity<List<RecebimentoLocacao>> findRecebimentosByLocacao(@PathVariable String id) {
        return ResponseEntity.ok(locacaoService.findRecebimentosByLocacaoId(id));
    }

    @GetMapping("/recebimentos")
    public ResponseEntity<List<RecebimentoLocacao>> findRecebimentosByMes(
            @RequestParam Integer mes, @RequestParam Integer ano) {
        return ResponseEntity.ok(locacaoService.findRecebimentosByMesAndAno(mes, ano));
    }

    @PutMapping("/{id}/recebimentos/{mes}/{ano}")
    public ResponseEntity<RecebimentoLocacao> upsertRecebimento(
            @PathVariable String id,
            @PathVariable Integer mes,
            @PathVariable Integer ano,
            @RequestBody Map<String, Object> body) {
        BigDecimal valorRecebido = new BigDecimal(body.get("valorRecebido").toString());
        return ResponseEntity.ok(locacaoService.upsertRecebimento(id, mes, ano, valorRecebido));
    }

    @DeleteMapping("/{id}/recebimentos/{mes}/{ano}")
    public ResponseEntity<Void> deleteRecebimento(
            @PathVariable String id,
            @PathVariable Integer mes,
            @PathVariable Integer ano) {
        locacaoService.deleteRecebimento(id, mes, ano);
        return ResponseEntity.noContent().build();
    }
}
