package com.branik.restservice.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FutureGameTeamRestModel {
    private String name;
    private String color;
}
