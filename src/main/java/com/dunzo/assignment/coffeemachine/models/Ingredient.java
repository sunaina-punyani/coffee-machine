package com.dunzo.assignment.coffeemachine.models;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    private String name;
    private BigDecimal maxQuantity;
    private BigDecimal availableQuantity;
    private BigDecimal thresholdPercent;
    private Boolean isRunningLow;

    /**
     *
     * @param runningLow
     */
    public void setRunningLow(Boolean runningLow) {
        isRunningLow = runningLow;
    }

    /**
     *
     * @param availableQuantity
     */
    public void setAvailableQuantity(BigDecimal availableQuantity) {
        this.availableQuantity = availableQuantity;
        this.isRunningLow = checkForQuantityRunningLow();
    }

    /**
     *
     * @return
     */
    public Boolean checkForQuantityRunningLow(){
        return availableQuantity.compareTo(thresholdPercent.multiply(maxQuantity).divide(BigDecimal.valueOf(100), RoundingMode.FLOOR)) <= 0;
    }
}
