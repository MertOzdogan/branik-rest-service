package com.branik.updater.database.model.db;

import lombok.*;
import javax.persistence.*;

@Entity(name = "goals")
@Getter
@Setter
@AllArgsConstructor
public class GoalEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerEntity playerEntity;

    @ManyToOne
    @JoinColumn(name = "MATCH_ID", nullable = false)
    private MatchEntity match;

    private String type;
    private String minute;

    public GoalEntity() {
    }
}
