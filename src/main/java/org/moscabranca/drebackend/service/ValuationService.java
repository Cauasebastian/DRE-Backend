package org.moscabranca.drebackend.service;

import org.moscabranca.drebackend.dto.DreRequest;
import org.moscabranca.drebackend.dto.ValuationResponse;
import org.moscabranca.drebackend.model.Dre;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ValuationService {
    /**
     * Calcula o valuation de uma empresa
     * @param request
     * @return
     */
    public ValuationResponse calcularValuation(DreRequest request) {
        BigDecimal fluxoCaixaPresente = BigDecimal.ZERO;
        BigDecimal taxaDesconto = request.getTaxaDesconto();
        int anosProjecao = request.getAnosProjecao();

        for (int i = 1; i <= anosProjecao; i++) {
            BigDecimal fluxoAno = calcularFluxoCaixaAno(request, i);
            BigDecimal valorPresente = fluxoAno.divide(
                    BigDecimal.ONE.add(taxaDesconto).pow(i), 2, RoundingMode.HALF_UP);
            fluxoCaixaPresente = fluxoCaixaPresente.add(valorPresente);
        }

        BigDecimal valorTerminal = calcularValorTerminal(request, taxaDesconto);
        BigDecimal valuationTotal = fluxoCaixaPresente.add(valorTerminal);

        return new ValuationResponse(fluxoCaixaPresente, valorTerminal, valuationTotal);
    }

    /**
     * Calcula o fluxo de caixa para um determinado ano
     * @param request
     * @param ano
     * @return
     */
    private BigDecimal calcularFluxoCaixaAno(DreRequest request, int ano) {
        BigDecimal receitaLiquida = request.getReceitaLiquida();
        BigDecimal despesasOperacionais = request.getDespesasOperacionais();
        BigDecimal taxaCrescimentoReceita = BigDecimal.valueOf(0.05); // exemplo de taxa de crescimento

        BigDecimal fluxoAno = receitaLiquida.subtract(despesasOperacionais)
                .multiply(BigDecimal.ONE.add(taxaCrescimentoReceita).pow(ano));

        return fluxoAno;
    }
    /**
     * Calcula o valor terminal do valuation
     * @param request
     * @param taxaDesconto
     * @return
     */
    private BigDecimal calcularValorTerminal(DreRequest request, BigDecimal taxaDesconto) {
        BigDecimal taxaCrescimento = BigDecimal.valueOf(0.03); // Ajustável conforme o setor
        BigDecimal fluxoUltimoAno = calcularFluxoCaixaAno(request, request.getAnosProjecao());
        return fluxoUltimoAno.multiply(BigDecimal.ONE.add(taxaCrescimento))
                .divide(taxaDesconto.subtract(taxaCrescimento), RoundingMode.HALF_UP);
    }
}
