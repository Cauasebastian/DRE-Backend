package org.moscabranca.drebackend.service;

import org.moscabranca.drebackend.dto.DreAnualRequest;
import org.moscabranca.drebackend.dto.DreRequest;
import org.moscabranca.drebackend.model.Dre;
import org.moscabranca.drebackend.model.DreAnual;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class DreService {

    /**
     * Calcula a DRE (Demonstração do Resultado do Exercício) para múltiplos anos.
     *
     * @param request Dados de entrada para o cálculo da DRE.
     * @return Objeto Dre com os valores calculados para cada ano.
     */
    public Dre calcularDre(DreRequest request) {
        Dre dre = new Dre();
        List<DreAnual> dreAnualList = new ArrayList<>();

        for (DreAnualRequest anualRequest : request.getDreAnualRequests()) {
            DreAnual dreAnual = calcularDreAnual(anualRequest);
            dreAnualList.add(dreAnual);
        }

        dre.setDreAnualList(dreAnualList);
        return dre;
    }

    /**
     * Calcula a DRE para um único ano.
     *
     * @param request Dados de entrada para o cálculo da DRE anual.
     * @return Objeto DreAnual com os valores calculados.
     */
    private DreAnual calcularDreAnual(DreAnualRequest request) {
        DreAnual dreAnual = new DreAnual();
        dreAnual.setAno(request.getAno());
        dreAnual.setReceitas(request.getReceitas());
        dreAnual.setDespesas(request.getDespesas());
        dreAnual.setCmv(request.getCmv());
        dreAnual.setDepreciacao(request.getDepreciacao());
        dreAnual.setTaxaImposto(request.getTaxaImposto());

        // Realiza os cálculos para o ano
        calcularValoresDreAnual(dreAnual);

        return dreAnual;
    }

    /**
     * Realiza os cálculos dos valores da DRE para um ano específico.
     *
     * @param dreAnual Objeto DreAnual a ser preenchido com os cálculos.
     */
    private void calcularValoresDreAnual(DreAnual dreAnual) {
        BigDecimal receitaBrutaTotal = calcularReceitaBrutaTotal(dreAnual);
        BigDecimal cmv = dreAnual.getCmv() != null ? dreAnual.getCmv() : BigDecimal.ZERO;
        BigDecimal comissoes = calcularComissoes(dreAnual);
        BigDecimal despesasOperacionais = calcularDespesasOperacionais(dreAnual);

        dreAnual.setReceitaBrutaTotal(receitaBrutaTotal);
        dreAnual.setCmv(cmv);
        dreAnual.setComissoes(comissoes);

        // Receita Líquida = Receita Bruta Total - CMV - Comissões
        BigDecimal receitaLiquida = receitaBrutaTotal.subtract(cmv).subtract(comissoes);
        dreAnual.setReceitaLiquida(receitaLiquida);

        dreAnual.setDespesasOperacionais(despesasOperacionais);

        // EBITDA = Receita Líquida - Despesas Operacionais
        BigDecimal ebitda = receitaLiquida.subtract(despesasOperacionais);
        dreAnual.setEbitda(ebitda);

        // Definir taxa de imposto padrão se não estiver especificada
        BigDecimal taxaImposto = dreAnual.getTaxaImposto() != null ? dreAnual.getTaxaImposto() : BigDecimal.valueOf(0.08);
        dreAnual.setTaxaImposto(taxaImposto);

        // Impostos = EBITDA * Taxa de Imposto
        BigDecimal impostos = ebitda.multiply(taxaImposto).setScale(2, RoundingMode.HALF_UP);
        dreAnual.setImpostos(impostos);

        // Depreciação
        BigDecimal depreciacao = dreAnual.getDepreciacao() != null ? dreAnual.getDepreciacao() : BigDecimal.ZERO;
        dreAnual.setDepreciacao(depreciacao);

        // Lucro Líquido = EBITDA - Depreciação - Impostos
        BigDecimal lucroLiquido = ebitda.subtract(depreciacao).subtract(impostos);
        dreAnual.setLucroLiquido(lucroLiquido);
    }

    /**
     * Calcula a Receita Bruta Total a partir das receitas.
     *
     * @param dreAnual Objeto DreAnual contendo a lista de receitas.
     * @return Receita Bruta Total.
     */
    private BigDecimal calcularReceitaBrutaTotal(DreAnual dreAnual) {
        return dreAnual.getReceitas().stream()
                .map(receita -> receita.getReceitaBrutaTotal() != null ? receita.getReceitaBrutaTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o total de comissões a partir das receitas.
     *
     * @param dreAnual Objeto DreAnual contendo a lista de receitas.
     * @return Total de Comissões.
     */
    private BigDecimal calcularComissoes(DreAnual dreAnual) {
        return dreAnual.getReceitas().stream()
                .map(receita -> receita.getComissoes() != null ? receita.getComissoes() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula as Despesas Operacionais excluindo CMV e comissões.
     *
     * @param dreAnual Objeto DreAnual contendo a lista de despesas.
     * @return Despesas Operacionais.
     */
    private BigDecimal calcularDespesasOperacionais(DreAnual dreAnual) {
        return dreAnual.getDespesas().stream()
                .filter(despesa -> despesa.getCmv() == null && despesa.getComissoes() == null)
                .map(despesa -> despesa.getValor() != null ? despesa.getValor() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
