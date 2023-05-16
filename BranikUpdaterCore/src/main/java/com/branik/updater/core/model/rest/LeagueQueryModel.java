package com.branik.updater.core.model.rest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeagueQueryModel {
    private String year;
    private String league;
    private String group;
    private String season;
    private String team;
}
