package com.branik.updater.database.model.db;

import lombok.*;

import javax.persistence.*;

@Entity(name = "goals")
@Getter
@Setter
@AllArgsConstructor
public class GoalEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerEntity playerEntity;

    @ManyToOne
    @JoinColumn(name = "MATCH_ID", nullable = false)
    private MatchEntity match;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    private String type;
    private String minute;

    public GoalEntity() {
    }
}
