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
import java.util.Comparator;

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
        BigDecimal taxaCrescimento = BigDecimal.valueOf(0.05); // Pode ser parametrizado

        // Ordena a lista de DREs anuais por ano
        dre.getDreAnualList().sort(Comparator.comparingInt(DreAnual::getAno));

        int anosProjecao = request.getAnosProjecao();
        int anosDisponiveis = dre.getDreAnualList().size();

        // Fluxo de caixa do último ano disponível
        DreAnual ultimoAnoDisponivel = dre.getDreAnualList().get(anosDisponiveis - 1);
        BigDecimal fluxoCaixaBase = calcularFluxoCaixaAno(ultimoAnoDisponivel);

        for (int i = 1; i <= anosProjecao; i++) {
            BigDecimal fluxoAno;
            int periodo = i;

            if (i <= anosDisponiveis) {
                // Usar dados reais
                DreAnual dreAnual = dre.getDreAnualList().get(i - 1);
                fluxoAno = calcularFluxoCaixaAno(dreAnual);
            } else {
                // Projetar fluxo de caixa
                int anosDesdeUltimoAno = i - anosDisponiveis;
                fluxoAno = fluxoCaixaBase.multiply(
                        BigDecimal.ONE.add(taxaCrescimento).pow(anosDesdeUltimoAno, new MathContext(10, RoundingMode.HALF_UP)));
            }

            BigDecimal valorPresente = fluxoAno.divide(
                    BigDecimal.ONE.add(taxaDesconto).pow(periodo), 2, RoundingMode.HALF_UP);
            fluxoCaixaPresente = fluxoCaixaPresente.add(valorPresente);
        }

        // Calcula o valor terminal
        BigDecimal taxaCrescimentoPerpetuo = BigDecimal.valueOf(0.03); // Pode ser parametrizado
        BigDecimal valorTerminalDescontado = calcularValorTerminal(fluxoCaixaBase, taxaDesconto, taxaCrescimentoPerpetuo, anosProjecao, anosDisponiveis);
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
     * @param fluxoCaixaBase           Fluxo de caixa base do último ano disponível.
     * @param taxaDesconto             Taxa de desconto para o valuation.
     * @param taxaCrescimentoPerpetuo  Taxa de crescimento perpétuo.
     * @param anosProjecao             Número total de anos de projeção.
     * @return Valor terminal descontado.
     */
    private BigDecimal calcularValorTerminal(BigDecimal fluxoCaixaBase, BigDecimal taxaDesconto,
                                             BigDecimal taxaCrescimentoPerpetuo, int anosProjecao, int anosDisponiveis) {
        // Fluxo de caixa no último ano projetado
        BigDecimal fluxoAnoTerminal = fluxoCaixaBase.multiply(
                BigDecimal.ONE.add(taxaCrescimentoPerpetuo).pow(1, new MathContext(10, RoundingMode.HALF_UP)));

        // Valor Terminal não descontado
        BigDecimal valorTerminalNaoDescontado = fluxoAnoTerminal.multiply(BigDecimal.ONE.add(taxaCrescimentoPerpetuo))
                .divide(taxaDesconto.subtract(taxaCrescimentoPerpetuo), RoundingMode.HALF_UP);

        // Descontar o valor terminal para o valor presente
        BigDecimal fatorDesconto = BigDecimal.ONE.add(taxaDesconto).pow(anosProjecao, new MathContext(10, RoundingMode.HALF_UP));
        BigDecimal valorTerminalDescontado = valorTerminalNaoDescontado.divide(fatorDesconto, 2, RoundingMode.HALF_UP);

        return valorTerminalDescontado;
    }
}
