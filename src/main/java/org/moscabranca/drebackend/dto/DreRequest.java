package org.moscabranca.drebackend.dto;

import lombok.Data;
import org.moscabranca.drebackend.model.Receita;
import org.moscabranca.drebackend.model.Despesa;

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
}
