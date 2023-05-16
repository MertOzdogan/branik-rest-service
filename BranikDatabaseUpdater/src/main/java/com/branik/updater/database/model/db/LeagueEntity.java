package com.branik.updater.database.model.db;

import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "league")
@Getter
@Builder
@AllArgsConstructor
@Setter
public class LeagueEntity extends BaseEntity {
    @Column(name = "league_year")
    private String leagueYear;
    @Column(name = "league_season")
    private String leagueSeason;
    @Column(name = "league_number")
    private String leagueNumber;
    @Column(name = "league_group")
    private String leagueGroup;
    @Column(name = "is_active")
    private boolean isActive;


    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "leagueEntities")
    private Set<TeamEntity> teamEntities = new HashSet<>();

    public LeagueEntity() {
    }

    @Override
    public String toString() {
        return "LeagueEntity{" +
                "league_year='" + leagueYear + '\'' +
                ", league_season='" + leagueSeason + '\'' +
                ", league_number='" + leagueNumber + '\'' +
                ", league_group='" + leagueGroup + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeagueEntity that = (LeagueEntity) o;
        return Objects.equals(leagueYear, that.leagueYear) &&
                Objects.equals(leagueSeason, that.leagueSeason) &&
                Objects.equals(leagueNumber, that.leagueNumber) &&
                Objects.equals(leagueGroup, that.leagueGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leagueYear, leagueSeason, leagueNumber, leagueGroup);
    }

    public Set<TeamEntity> getTeamEntities() {
        if (teamEntities == null) {
            teamEntities = new HashSet<>();
        }
        return teamEntities;
    }
}
