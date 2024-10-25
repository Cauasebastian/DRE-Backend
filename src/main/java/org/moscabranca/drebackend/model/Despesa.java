package org.moscabranca.drebackend.model;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;               // Ex.: AWS, SG&A, Vendedores
    private String tipoDespesa;             // Ex.: Fixa, Variável
    private BigDecimal valor;               // Valor da Despesa
    private BigDecimal comissoes;           // Comissões (ex: 5%)
    private BigDecimal cmv;                 // Custo das Mercadorias Vendidas
}
