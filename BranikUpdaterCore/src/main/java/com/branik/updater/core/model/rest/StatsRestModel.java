package com.branik.updater.core.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsRestModel {
    @JsonProperty("Hráč")
    private String name;
    @JsonProperty("Zápasů")
    private String gamesPlayed;
    @JsonProperty("Gólů")
    private String numberOfGoals;
}
