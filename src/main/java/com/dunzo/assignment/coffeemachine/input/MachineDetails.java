package com.dunzo.assignment.coffeemachine.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@JsonDeserialize
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
public class MachineDetails {

    private OutletDetails outlets;

    @JsonProperty("total_items_quantity")
    private HashMap<String, BigDecimal> totalItemsQuantityMap;

    @JsonProperty("beverages")
    private HashMap<String, Map<String, BigDecimal>> beverageCompositionsMap;
}
