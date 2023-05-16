package com.branik.updater.core.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandingsRestModel {
    @JsonProperty("Pořadí")
    private String rank;
    @JsonProperty("Tým")
    private String team;
    @JsonProperty("Odehrané zápasy")
    private String gameCount;
    @JsonProperty("Počet výher")
    private String winCount;
    @JsonProperty("Počet remíz")
    private String drawCount;
    @JsonProperty("Počet proher")
    private String lossCount;
    @JsonProperty("Skóre")
    private String killDeathRatio;
    @JsonProperty("Počet bodů")
    private String points;
}
