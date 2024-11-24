package org.moscabranca.drebackend.controller;

import org.moscabranca.drebackend.dto.DreRequest;
import org.moscabranca.drebackend.dto.ValuationResponse;
import org.moscabranca.drebackend.model.Dre;
import org.moscabranca.drebackend.service.DreService;
import org.moscabranca.drebackend.service.ValuationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dre")
public class DreController {

    @Autowired
    private DreService dreService;

    @Autowired
    private ValuationService valuationService;

    /**
     * Endpoint para calcular a DRE com base nos dados fornecidos.
     *
     * @param request Dados de entrada para o cálculo da DRE.
     * @return DRE calculada.
     */
    @PostMapping("/calcular")
    public ResponseEntity<Dre> calcularDre(@RequestBody DreRequest request) {
        Dre dreCalculada = dreService.calcularDre(request);
        return ResponseEntity.ok(dreCalculada);
    }

    /**
     * Endpoint para calcular o valuation com base nos dados fornecidos.
     *
     * @param request Dados de entrada para o cálculo do valuation.
     * @return Valuation calculado.
     */
    @PostMapping("/valuation")
    public ResponseEntity<ValuationResponse> calcularValuation(@RequestBody DreRequest request) {
        ValuationResponse response = valuationService.calcularValuation(request);
        return ResponseEntity.ok(response);
    }
}
