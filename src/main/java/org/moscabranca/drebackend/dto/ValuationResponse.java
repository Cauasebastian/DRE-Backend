package org.moscabranca.drebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ValuationResponse {
    private BigDecimal valorPresenteLiquido;
    private BigDecimal valorTerminal;
    private BigDecimal valuationTotal;
}
