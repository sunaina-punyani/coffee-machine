package com.dunzo.assignment.coffeemachine.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Data
@JsonDeserialize
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
public class SystemDetails {
    @JsonProperty("machine")
    private MachineDetails machineDetails;
}
