package com.branik.updater.core.model.rest.match;

import com.branik.updater.core.jackson.DateDeserializer;
import com.branik.updater.core.jackson.TimeDeserializer;
import com.branik.updater.core.model.rest.LeagueRestModel;
import com.branik.updater.core.model.rest.TeamRestModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@ToString
@Getter
public class DetailedMatchRestModel {
    @JsonProperty("Datum")
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate date;

    @JsonDeserialize(using = TimeDeserializer.class)
    @JsonProperty("Čas")
    private LocalTime time;

    @JsonProperty("Hřiště")
    private String playground;

    @JsonProperty("Kolo")
    private String round;

    @JsonProperty("Výsledek")
    private String result;

    @JsonProperty("Popis zápasu")
    private String refComment;

    @JsonProperty("Rozhodčí")
    private String referees;

    @JsonProperty("teams")
    private Map<String, TeamRestModel> teams = new HashMap<>();

    @JsonProperty("leagueDetails")
    private LeagueRestModel leagueModel;


}
