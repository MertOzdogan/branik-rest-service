package com.branik.updater.database.model.db;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "teams")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamEntity extends BaseEntity {
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "teamEntity")
    private Set<PlayerEntity> players = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "homeTeam")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<MatchEntity> match;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "teams_league",
            joinColumns = {@JoinColumn(name = "team_id")},
            inverseJoinColumns = {@JoinColumn(name = "league_id")})
    private Set<LeagueEntity> leagueEntities = new HashSet<>();

    public TeamEntity(String trim) {
        this.name = trim;
    }

    public TeamEntity(Long id, String name, Set<PlayerEntity> players, Set<MatchEntity> match, Set<LeagueEntity> leagueEntities) {
        this.id = id;
        this.name = name;
        this.players = players;
        this.match = match;
        this.leagueEntities = leagueEntities;
    }

    public void addLeague(LeagueEntity league) {
        Hibernate.initialize(getLeagueEntities());
        if (!getLeagueEntities().contains(league)) {
            getLeagueEntities().add(league);
            league.getTeamEntities().add(this);
        }
    }

    public void setPlayers(PlayerEntity playerEntity) {
        Hibernate.initialize(getPlayers());
        if (!players.contains(playerEntity)) {
            this.players.add(playerEntity);
            playerEntity.setTeamEntity(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamEntity that = (TeamEntity) o;
        return Objects.equals(name, that.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "TeamEntity{" +
                "name='" + name + '\'' +
                ", leagueEntities=" + leagueEntities +
                '}';
    }
}
