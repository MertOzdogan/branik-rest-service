package com.branik.updater.database.model.db;

import lombok.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "matches")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class MatchEntity extends BaseEntity {
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "HOME_TEAM_ID", nullable = false)
    private TeamEntity homeTeam;

    @ManyToOne
    @JoinColumn(name = "AWAY_TEAM_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private TeamEntity awayTeam;
    @ManyToOne
    @JoinColumn(name = "LEAGUE_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private LeagueEntity leagueEntity;

    @ManyToOne
    @JoinColumn(name = "PITCH_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PitchEntity pitchEntity;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GoalEntity> goals;

    private Integer homeTeamScore;
    private Integer awayTeamScore;

    @ManyToOne
    @JoinColumn(name = "HOME_TEAM_MVP")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PlayerEntity homeTeamMVP;

    @ManyToOne
    @JoinColumn(name = "HOME_TEAM_CAPTAIN")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PlayerEntity homeTeamCaptain;

    @ManyToOne
    @JoinColumn(name = "AWAY_TEAM_MVP")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PlayerEntity awayTeamMVP;

    @ManyToOne
    @JoinColumn(name = "AWAY_TEAM_CAPTAIN")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PlayerEntity awayTeamCaptain;

    private String comment;

    private int round;

    public MatchEntity() {
    }

    public void setGoals(List<GoalEntity> goals) {
        this.goals = goals;
        goals.forEach(o -> o.setMatch(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchEntity that = (MatchEntity) o;
        return Objects.equals(time, that.time) && Objects.equals(homeTeam.getName(), that.homeTeam.getName()) && Objects.equals(awayTeam.getName(), that.awayTeam.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, homeTeam.getName(), awayTeam.getName());
    }
}
