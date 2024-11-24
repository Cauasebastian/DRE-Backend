package org.moscabranca.drebackend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DreRequest {
    private List<DreAnualRequest> dreAnualRequests;
    private BigDecimal taxaDesconto; // Discount rate for valuation
    private int anosProjecao;        // Number of years to project cash flow
}
