package org.moscabranca.drebackend.controller;

import org.moscabranca.drebackend.dto.DreRequest;
import org.moscabranca.drebackend.dto.ValuationResponse;
import org.moscabranca.drebackend.model.Dre;
import org.moscabranca.drebackend.service.DreService;
import org.moscabranca.drebackend.service.ValuationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dre")
public class DreController {

    @Autowired
    private DreService dreService;

    @Autowired
    private ValuationService valuationService;

    @PostMapping
    public ResponseEntity<Dre> criarDre(@RequestBody Dre dre) {
        Dre dreSalva = dreService.salvarDre(dre);
        return ResponseEntity.ok(dreSalva);
    }

    @GetMapping
    public ResponseEntity<List<Dre>> listarDres() {
        return ResponseEntity.ok(dreService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dre> buscarDre(@PathVariable Long id) {
        Dre dre = dreService.buscarPorId(id);
        if (dre != null) {
            return ResponseEntity.ok(dre);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDre(@PathVariable Long id) {
        dreService.deletarDre(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/valuation")
    public ResponseEntity<ValuationResponse> calcularValuation(@RequestBody DreRequest request) {
        ValuationResponse response = valuationService.calcularValuation(request);
        return ResponseEntity.ok(response);
    }
}
