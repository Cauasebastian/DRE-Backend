package org.moscabranca.drebackend.model;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modeloReceita;              // Ex.: SaaS, Consultoria
    private String tipoReceita;                // Ex.: Inbound, Outbound
    private BigDecimal ticketMedio;            // Valor médio por cliente
    private BigDecimal cac;                    // Custo de Aquisição de Cliente
    private BigDecimal investimentoMkt;        // Investimento em Marketing
    private BigDecimal conversaoInbound;       // Taxa de Conversão Inbound
    private Integer vendasInbound;             // Número de Vendas Inbound
    private Integer clientesTotais;            // Total de Clientes Ativos
    private BigDecimal cancelamento;           // Taxa de Cancelamento (Churn)
    private Integer consultorias;              // Quantidade de Consultorias
    private BigDecimal ticketMedioConsultorias;// Ticket Médio das Consultorias
    private BigDecimal receitaBrutaTotal;      // Receita Bruta Total
    //comissao
    private BigDecimal comissoes;              // Comissões (ex: 5%)


}
