package com.branik.updater.database.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "standings")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class StandingEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @ManyToOne
    @JoinColumn(name = "league_id", nullable = false)
    private LeagueEntity league;

    private int team_rank;
    private int wins;
    private int draws;
    private int losses;
    private int goalsScored;
    private int goalsConceded;
    private int points;
}
