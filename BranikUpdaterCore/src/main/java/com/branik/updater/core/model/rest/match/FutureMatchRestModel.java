package com.branik.updater.core.model.rest.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@ToString
@Getter
public class FutureMatchRestModel {
    private String date;
    private String time;
    private String pitch;
    private Team[] teams;
    private int kolo;


    @Jacksonized
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Setter
    @ToString
    @Getter
    @Builder
    public static class Team{
        private String name;
        private String color;
    }
}
