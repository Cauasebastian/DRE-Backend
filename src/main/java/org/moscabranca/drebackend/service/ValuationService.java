package org.moscabranca.drebackend.service;

import org.moscabranca.drebackend.dto.DreRequest;
import org.moscabranca.drebackend.dto.ValuationResponse;
import org.moscabranca.drebackend.model.Dre;
import org.moscabranca.drebackend.model.DreAnual;
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
     * Calcula o valuation de uma empresa.
     *
     * @param request Dados de entrada para o cálculo do valuation.
     * @return ValuationResponse com os valores calculados.
     */
    public ValuationResponse calcularValuation(DreRequest request) {
        // Calcular DRE
        Dre dre = dreService.calcularDre(request);
        BigDecimal fluxoCaixaPresente = BigDecimal.ZERO;
        BigDecimal taxaDesconto = request.getTaxaDesconto();

        // Ordena a lista de DREs anuais por ano, caso não esteja ordenada
        dre.getDreAnualList().sort((a, b) -> Integer.compare(a.getAno(), b.getAno()));

        int anoInicial = dre.getDreAnualList().get(0).getAno();

        // Calcula o fluxo de caixa presente para cada ano
        for (DreAnual dreAnual : dre.getDreAnualList()) {
            int ano = dreAnual.getAno();
            int periodo = ano - anoInicial + 1; // Ajusta o período para começar em 1
            BigDecimal fluxoAno = calcularFluxoCaixaAno(dreAnual);
            BigDecimal valorPresente = fluxoAno.divide(
                    BigDecimal.ONE.add(taxaDesconto).pow(periodo), 2, RoundingMode.HALF_UP);
            fluxoCaixaPresente = fluxoCaixaPresente.add(valorPresente);
        }

        // Calcula o valor terminal
        BigDecimal valorTerminalDescontado = calcularValorTerminal(dre, taxaDesconto);
        BigDecimal valuationTotal = fluxoCaixaPresente.add(valorTerminalDescontado);

        return new ValuationResponse(fluxoCaixaPresente, valorTerminalDescontado, valuationTotal);
    }

    /**
     * Calcula o fluxo de caixa para um determinado ano.
     *
     * @param dreAnual Dados da DRE anual.
     * @return Fluxo de caixa para o ano especificado.
     */
    private BigDecimal calcularFluxoCaixaAno(DreAnual dreAnual) {
        // Usando o EBITDA como proxy para o fluxo de caixa operacional
        BigDecimal fluxoCaixa = dreAnual.getEbitda();
        return fluxoCaixa != null ? fluxoCaixa : BigDecimal.ZERO;
    }

    /**
     * Calcula o valor terminal do valuation.
     *
     * @param dre          Objeto Dre contendo a lista de DREs anuais.
     * @param taxaDesconto Taxa de desconto para o valuation.
     * @return Valor terminal descontado.
     */
    private BigDecimal calcularValorTerminal(Dre dre, BigDecimal taxaDesconto) {
        // Calcula o valor terminal com base nos dados do último ano
        DreAnual ultimoAno = dre.getDreAnualList().get(dre.getDreAnualList().size() - 1);
        BigDecimal fluxoUltimoAno = calcularFluxoCaixaAno(ultimoAno);
        BigDecimal taxaCrescimentoPerpetuo = BigDecimal.valueOf(0.03); // Exemplo de taxa de crescimento perpétuo

        BigDecimal valorTerminalNaoDescontado = fluxoUltimoAno.multiply(BigDecimal.ONE.add(taxaCrescimentoPerpetuo))
                .divide(taxaDesconto.subtract(taxaCrescimentoPerpetuo), RoundingMode.HALF_UP);

        // Desconta o valor terminal para o valor presente
        int n = dre.getDreAnualList().size();
        BigDecimal fatorDesconto = BigDecimal.ONE.add(taxaDesconto).pow(n, new MathContext(10, RoundingMode.HALF_UP));
        BigDecimal valorTerminalDescontado = valorTerminalNaoDescontado.divide(fatorDesconto, 2, RoundingMode.HALF_UP);

        return valorTerminalDescontado;
    }
}
