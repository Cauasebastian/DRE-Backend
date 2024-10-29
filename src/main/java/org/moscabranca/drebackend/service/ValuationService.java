package org.moscabranca.drebackend.service;

import org.moscabranca.drebackend.dto.DreRequest;
import org.moscabranca.drebackend.dto.ValuationResponse;
import org.moscabranca.drebackend.model.Dre;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ValuationService {

    // Método que calcula o valuation baseado em dados da DRE
    public ValuationResponse calcularValuation(DreRequest request) {
        BigDecimal fluxoCaixaPresente = BigDecimal.ZERO;
        BigDecimal taxaDesconto = request.getTaxaDesconto();
        int anosProjecao = request.getAnosProjecao();

        for (int i = 1; i <= anosProjecao; i++) {
            BigDecimal fluxoAno = calcularFluxoCaixaAno(request, i);
            BigDecimal valorPresente = fluxoAno.divide(
                    BigDecimal.ONE.add(taxaDesconto).pow(i), 2, RoundingMode.HALF_UP
            );
            fluxoCaixaPresente = fluxoCaixaPresente.add(valorPresente);
        }

        BigDecimal valorTerminal = calcularValorTerminal(request, taxaDesconto);
        BigDecimal valuationTotal = fluxoCaixaPresente.add(valorTerminal);

        return new ValuationResponse(fluxoCaixaPresente, valorTerminal, valuationTotal);
    }

    // Simulação do cálculo do fluxo de caixa anual
    private BigDecimal calcularFluxoCaixaAno(DreRequest request, int ano) {
        // Para este exemplo, consideramos apenas a receita líquida menos despesas operacionais
        return request.getReceitaLiquida().subtract(request.getDespesasOperacionais());
    }

    // Cálculo do valor terminal usando o modelo de perpetuidade (Gordon Growth Model)
    private BigDecimal calcularValorTerminal(DreRequest request, BigDecimal taxaDesconto) {
        BigDecimal taxaCrescimento = BigDecimal.valueOf(0.03); // Taxa de crescimento estimada em 3%
        BigDecimal fluxoUltimoAno = calcularFluxoCaixaAno(request, request.getAnosProjecao());
        return fluxoUltimoAno.multiply(BigDecimal.ONE.add(taxaCrescimento))
                .divide(taxaDesconto.subtract(taxaCrescimento), RoundingMode.HALF_UP);
    }
}
