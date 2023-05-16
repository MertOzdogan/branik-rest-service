package com.branik.updater.database.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "pitch")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PitchEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pitch_abbr;
    private String pitch_desc;
    private String pitch_address;

}
