package org.moscabranca.drebackend.dto;

import lombok.Data;
import org.moscabranca.drebackend.model.Receita;
import org.moscabranca.drebackend.model.Despesa;

import java.math.BigDecimal; // Added import for BigDecimal
import java.util.List;

@Data
public class DreRequest {
    private BigDecimal receitaBrutaTotal;
    private BigDecimal cmv;
    private BigDecimal despesasOperacionais;
    private BigDecimal depreciacao;
    private BigDecimal impostos;

    private List<Receita> receitas;
    private List<Despesa> despesas;

    private BigDecimal receitaLiquida;
    private BigDecimal taxaDesconto; // Taxa de desconto para valuation
    private int anosProjecao;        // Número de anos para projetar o fluxo de caixa

    private BigDecimal taxaImposto;  // Exemplo: 0.155 para 15,5%
}