package com.branik.updater.core.model.rest.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@ToString
@Getter
public class OldMatchRestModel {
    private String date;
    private String time;
    private String pitch;
    private String homeTeam;
    private String awayTeam;
    private String kolo;
    private String homeTeamScore;
    private String awayTeamScore;
}
