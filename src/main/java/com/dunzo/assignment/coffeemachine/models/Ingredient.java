package com.dunzo.assignment.coffeemachine.models;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Model to store an ingredient
 */
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
     * True if model is running low in quantity
     * @param runningLow
     */
    public void setRunningLow(Boolean runningLow) {
        isRunningLow = runningLow;
    }

    /**
     * Change available quantity of ingredient post usage, compare with threshold percent of max capacity and
     * mark isRunningLow true or false accordingly
     * @param availableQuantity
     */
    public void setAvailableQuantity(BigDecimal availableQuantity) {
        this.availableQuantity = availableQuantity;
        this.isRunningLow = checkForQuantityRunningLow();
    }

    /**
     * Compare with threshold percent of max capacity and mark isRunningLow true or false accordingly
     * @return
     */
    public Boolean checkForQuantityRunningLow(){
        return availableQuantity.compareTo(thresholdPercent.multiply(maxQuantity).divide(BigDecimal.valueOf(100), RoundingMode.FLOOR)) <= 0;
    }
}
