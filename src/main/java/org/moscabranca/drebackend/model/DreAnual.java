package org.moscabranca.drebackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class DreAnual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ano;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dre_anual_id")
    private List<Receita> receitas;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dre_anual_id")
    private List<Despesa> despesas;

    private BigDecimal receitaBrutaTotal;
    private BigDecimal cmv;
    private BigDecimal comissoes;
    private BigDecimal receitaLiquida;
    private BigDecimal despesasOperacionais;
    private BigDecimal ebitda;
    private BigDecimal depreciacao;
    private BigDecimal impostos;
    private BigDecimal lucroLiquido;
    private BigDecimal taxaImposto;
}
