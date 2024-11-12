package org.moscabranca.drebackend.dto;

import lombok.Data;
import org.moscabranca.drebackend.model.Despesa;
import org.moscabranca.drebackend.model.Receita;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DreAnualRequest {
    private int ano;
    private List<Receita> receitas;
    private List<Despesa> despesas;
    private BigDecimal cmv;
    private BigDecimal depreciacao;
    private BigDecimal taxaImposto;
}
