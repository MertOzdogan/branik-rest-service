package com.branik.updater.core.model.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeagueRestModel {
    private String year;
    private String league;
    private String group;
    private String season;
}
