package org.moscabranca.drebackend.service;

import org.moscabranca.drebackend.dto.DreRequest;
import org.moscabranca.drebackend.dto.ValuationResponse;
import org.moscabranca.drebackend.model.Dre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ValuationService {
    @Autowired
    private DreService dreService;
    /**
     * Calcula o valuation de uma empresa
     * @param request
     * @return
     */
    public ValuationResponse calcularValuation(DreRequest request) {
        // Converter DreRequest para Dre
        Dre dre = new Dre();
        dre.setReceitas(request.getReceitas());
        dre.setDespesas(request.getDespesas());
        dre.setCmv(request.getCmv());
        dre.setDepreciacao(request.getDepreciacao());
        dre.setTaxaImposto(request.getTaxaImposto());

        // Calcular DRE
        dreService.calcularDre(dre);  // Torne o método calcularDre público ou refatore conforme necessário

        // Usar os valores calculados
        BigDecimal receitaLiquida = dre.getReceitaLiquida();
        BigDecimal despesasOperacionais = dre.getDespesasOperacionais();

        BigDecimal fluxoCaixaPresente = BigDecimal.ZERO;
        BigDecimal taxaDesconto = request.getTaxaDesconto();
        int anosProjecao = request.getAnosProjecao();

        for (int i = 1; i <= anosProjecao; i++) {
            BigDecimal fluxoAno = calcularFluxoCaixaAno(receitaLiquida, despesasOperacionais, i);
            BigDecimal valorPresente = fluxoAno.divide(
                    BigDecimal.ONE.add(taxaDesconto).pow(i), 2, RoundingMode.HALF_UP);
            fluxoCaixaPresente = fluxoCaixaPresente.add(valorPresente);
        }

        BigDecimal valorTerminal = calcularValorTerminal(receitaLiquida, despesasOperacionais, taxaDesconto, anosProjecao);
        BigDecimal valuationTotal = fluxoCaixaPresente.add(valorTerminal);

        return new ValuationResponse(fluxoCaixaPresente, valorTerminal, valuationTotal);
    }


    /**
     * Calcula o fluxo de caixa para um determinado ano
     * @param
     * @param ano
     * @return
     */
    private BigDecimal calcularFluxoCaixaAno(BigDecimal receitaLiquida, BigDecimal despesasOperacionais, int ano) {
        BigDecimal taxaCrescimentoReceita = BigDecimal.valueOf(0.05); // exemplo de taxa de crescimento

        BigDecimal fluxoAno = receitaLiquida.subtract(despesasOperacionais)
                .multiply(BigDecimal.ONE.add(taxaCrescimentoReceita).pow(ano));

        return fluxoAno;
    }
    /**
     * Calcula o valor terminal do valuation
     * @param receitaLiquida
     * @param taxaDesconto
     * @return
     */
    private BigDecimal calcularValorTerminal(BigDecimal receitaLiquida, BigDecimal despesasOperacionais, BigDecimal taxaDesconto, int anosProjecao) {
        BigDecimal taxaCrescimento = BigDecimal.valueOf(0.03); // Ajustável conforme o setor
        BigDecimal fluxoUltimoAno = calcularFluxoCaixaAno(receitaLiquida, despesasOperacionais, anosProjecao);
        return fluxoUltimoAno.multiply(BigDecimal.ONE.add(taxaCrescimento))
                .divide(taxaDesconto.subtract(taxaCrescimento), RoundingMode.HALF_UP);
    }
}
