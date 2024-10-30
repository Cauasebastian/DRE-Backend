package org.moscabranca.drebackend.dto;

import lombok.Data;
import org.moscabranca.drebackend.model.Receita;
import org.moscabranca.drebackend.model.Despesa;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DreRequest {
    //Removidos campos desnecessários: receitaBrutaTotal, despesasOperacionais, impostos, receitaLiquida foram removidos, pois são calculados pelo DreService e não precisam ser fornecidos na requisição.
    private List<Receita> receitas;
    private List<Despesa> despesas;

    private BigDecimal cmv;
    private BigDecimal depreciacao;
    private BigDecimal taxaImposto;  // Exemplo: 0.155 para 15,5%

    // Campos específicos para o cálculo do valuation
    private BigDecimal taxaDesconto; // Taxa de desconto para valuation
    private int anosProjecao;        // Número de anos para projetar o fluxo de caixa
}
