package com.dunzo.assignment.coffeemachine.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Data
@Builder
@JsonDeserialize
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
public class OutletDetails {

    @JsonProperty("count_n")
    private Integer outletCount;

}
