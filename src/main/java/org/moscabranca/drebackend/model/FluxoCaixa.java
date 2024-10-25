package org.moscabranca.drebackend.model;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class FluxoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal fco; // Fluxo de Caixa Operacional
    private BigDecimal fci; // Fluxo de Caixa de Investimento
    private BigDecimal fcf; // Fluxo de Caixa de Financiamento
    private BigDecimal fcl; // Fluxo de Caixa Livre
}
