package org.moscabranca.drebackend.service;

import org.moscabranca.drebackend.dto.DreRequest;
import org.moscabranca.drebackend.dto.ValuationResponse;
import org.moscabranca.drebackend.model.Dre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class ValuationService {

    @Autowired
    private DreService dreService;

    /**
     * Calcula o valuation de uma empresa
     * @param request Dados de entrada para o cálculo do valuation
     * @return ValuationResponse com os valores calculados
     */
    public ValuationResponse calcularValuation(DreRequest request) {
        // Calcular DRE
        Dre dre = dreService.calcularDre(request);

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

        //calcular o valor terminal, é necessário descontando o valor presente antes de somá-lo ao valuation total.
        BigDecimal valorTerminalNaoDescontado = calcularValorTerminal(receitaLiquida, despesasOperacionais, taxaDesconto, anosProjecao);
        BigDecimal fatorDesconto = BigDecimal.ONE.add(taxaDesconto).pow(anosProjecao, new MathContext(10, RoundingMode.HALF_UP));
        BigDecimal valorTerminalDescontado = valorTerminalNaoDescontado.divide(fatorDesconto, 2, RoundingMode.HALF_UP);
        BigDecimal valuationTotal = fluxoCaixaPresente.add(valorTerminalDescontado);

        return new ValuationResponse(fluxoCaixaPresente, valorTerminalDescontado, valuationTotal);
    }

    /**
     * Calcula o fluxo de caixa para um determinado ano
     * @param receitaLiquida
     * @param despesasOperacionais
     * @param ano
     * @return Fluxo de caixa para o ano especificado
     */
    private BigDecimal calcularFluxoCaixaAno(BigDecimal receitaLiquida, BigDecimal despesasOperacionais, int ano) {
        BigDecimal taxaCrescimentoReceita = BigDecimal.valueOf(0.05); // Exemplo de taxa de crescimento

        BigDecimal fluxoAno = receitaLiquida.subtract(despesasOperacionais)
                .multiply(BigDecimal.ONE.add(taxaCrescimentoReceita).pow(ano));

        return fluxoAno;
    }

    /**
     * Calcula o valor terminal do valuation
     * @param receitaLiquida
     * @param despesasOperacionais
     * @param taxaDesconto
     * @param anosProjecao
     * @return Valor terminal do valuation
     */
    private BigDecimal calcularValorTerminal(BigDecimal receitaLiquida, BigDecimal despesasOperacionais, BigDecimal taxaDesconto, int anosProjecao) {
        BigDecimal taxaCrescimento = BigDecimal.valueOf(0.03); // Ajustável conforme o setor
        BigDecimal fluxoUltimoAno = calcularFluxoCaixaAno(receitaLiquida, despesasOperacionais, anosProjecao);
        return fluxoUltimoAno.multiply(BigDecimal.ONE.add(taxaCrescimento))
                .divide(taxaDesconto.subtract(taxaCrescimento), RoundingMode.HALF_UP);
    }
}
