package org.moscabranca.drebackend.service;

import org.moscabranca.drebackend.dto.DreRequest;
import org.moscabranca.drebackend.model.Dre;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DreService {

    /**
     * Calcula a DRE (Demonstração do Resultado do Exercício)
     * @param request Dados de entrada para o cálculo da DRE
     * @return Objeto Dre com os valores calculados
     */
    public Dre calcularDre(DreRequest request) {
        // Converter DreRequest para Dre
        Dre dre = new Dre();
        dre.setReceitas(request.getReceitas());
        dre.setDespesas(request.getDespesas());
        dre.setCmv(request.getCmv());
        dre.setDepreciacao(request.getDepreciacao());
        dre.setTaxaImposto(request.getTaxaImposto());

        // Calcular os valores da DRE
        calcularValoresDre(dre);

        return dre;
    }

    /**
     * Método principal que calcula todos os valores da DRE
     * @param dre Objeto Dre a ser preenchido com os cálculos
     */
    private void calcularValoresDre(Dre dre) {
        BigDecimal receitaBrutaTotal = calcularReceitaBrutaTotal(dre);
        BigDecimal cmv = dre.getCmv() != null ? dre.getCmv() : BigDecimal.ZERO;
        BigDecimal comissoes = calcularComissoes(dre);
        BigDecimal despesasOperacionais = calcularDespesasOperacionais(dre);

        dre.setReceitaBrutaTotal(receitaBrutaTotal);
        dre.setCmv(cmv);
        dre.setComissoes(comissoes);

        // Receita Líquida = Receita Bruta Total - CMV - Comissões
        BigDecimal receitaLiquida = receitaBrutaTotal.subtract(cmv).subtract(comissoes);
        dre.setReceitaLiquida(receitaLiquida);

        dre.setDespesasOperacionais(despesasOperacionais);

        // EBITDA = Receita Líquida - Despesas Operacionais
        BigDecimal ebitda = receitaLiquida.subtract(despesasOperacionais);
        dre.setEbitda(ebitda);

        // Definir taxa de imposto padrão se não estiver especificada
        BigDecimal taxaImposto = dre.getTaxaImposto() != null ? dre.getTaxaImposto() : BigDecimal.valueOf(0.08);
        dre.setTaxaImposto(taxaImposto);

        // Impostos = EBITDA * Taxa de Imposto
        BigDecimal impostos = ebitda.multiply(taxaImposto).setScale(2, RoundingMode.HALF_UP);
        dre.setImpostos(impostos);

        // Depreciação
        BigDecimal depreciacao = dre.getDepreciacao() != null ? dre.getDepreciacao() : BigDecimal.ZERO;

        // Lucro Líquido = EBITDA - Depreciação - Impostos
        BigDecimal lucroLiquido = ebitda.subtract(depreciacao).subtract(impostos);
        dre.setLucroLiquido(lucroLiquido);
    }

    /**
     * Calcula a Receita Bruta Total a partir das receitas
     * @param dre Objeto Dre contendo a lista de receitas
     * @return Receita Bruta Total
     */
    private BigDecimal calcularReceitaBrutaTotal(Dre dre) {
        return dre.getReceitas().stream()
                .map(receita -> receita.getReceitaBrutaTotal() != null ? receita.getReceitaBrutaTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o total de comissões a partir das receitas
     * @param dre Objeto Dre contendo a lista de receitas
     * @return Total de Comissões
     */
    private BigDecimal calcularComissoes(Dre dre) {
        return dre.getReceitas().stream()
                .map(receita -> receita.getComissoes() != null ? receita.getComissoes() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula as Despesas Operacionais excluindo CMV e comissões
     * @param dre Objeto Dre contendo a lista de despesas
     * @return Despesas Operacionais
     */
    private BigDecimal calcularDespesasOperacionais(Dre dre) {
        return dre.getDespesas().stream()
                .filter(despesa -> despesa.getCmv() == null && despesa.getComissoes() == null)
                .map(despesa -> despesa.getValor() != null ? despesa.getValor() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
