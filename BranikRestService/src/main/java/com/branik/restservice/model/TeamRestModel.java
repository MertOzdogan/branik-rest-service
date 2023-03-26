package com.branik.restservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Jacksonized
@Getter
public class TeamRestModel {
    private String name;
    private String mvp;
    private String captain;
    private List<String> players;
    private List<String> yellowCards;
    private List<GoalRestModel> goalRestModels;

}
