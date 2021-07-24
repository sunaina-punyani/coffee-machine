package com.dunzo.assignment.coffeemachine.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BeverageOrder {
    String beverageName;
    Map<String, BigDecimal> composition;
}
