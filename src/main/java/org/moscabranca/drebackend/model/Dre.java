package org.moscabranca.drebackend.model;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class Dre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dre_id")
    private List<Receita> receitas;          // Lista de diferentes receitas

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dre_id")
    private List<Despesa> despesas;          // Lista de diferentes despesas

    private BigDecimal receitaBrutaTotal;     // Receita bruta total (soma das receitas)
    private BigDecimal cmv;                   // Custo das Mercadorias Vendidas (soma dos CMVs)
    private BigDecimal comissoes;             // Exemplo: 5%
    private BigDecimal receitaLiquida;        // Receita Líquida calculada
    private BigDecimal despesasOperacionais;  // Soma de despesas operacionais
    private BigDecimal ebitda;                // EBITDA calculado
    private BigDecimal depreciacao;           // Depreciação
    private BigDecimal impostos;              // Percentual de Impostos (15.5%)
    private BigDecimal lucroLiquido;          // Lucro Líquido
}
