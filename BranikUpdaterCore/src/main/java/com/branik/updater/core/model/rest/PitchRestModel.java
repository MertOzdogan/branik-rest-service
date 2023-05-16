package com.branik.updater.core.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@ToString
@Getter
public class PitchRestModel {
    @JsonProperty("Název hřiště")
    private String name;

    @JsonProperty("Zkratka hřiště")
    private String abbvr;

    @JsonProperty("Adresa areálů (hřišť) a další informace")
    private String details;
}
