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
public class GoalRestModel {
    private String minute;
    private String name;
}
