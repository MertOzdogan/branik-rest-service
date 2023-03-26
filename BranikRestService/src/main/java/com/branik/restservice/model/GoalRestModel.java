package com.branik.restservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Setter
@AllArgsConstructor
public class GoalRestModel {
    private String minute;
    private String name;
}
