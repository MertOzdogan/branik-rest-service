package com.branik.updater.database.repository;

import com.branik.updater.database.model.db.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Integer> {
    List<MatchEntity> getMatchEntitiesByHomeTeamNameOrAwayTeamNameAndLeagueEntityLeagueYearAndLeagueEntityLeagueSeasonAndLeagueEntityLeagueNumberAndLeagueEntityLeagueGroup(
            String homeTeam, String awayTeam, String leagueYear, String leagueSeason, String leagueNumber, String leagueGroup);

    @Query("SELECT m FROM matches m WHERE (m.homeTeam.name = :homeTeamName AND m.awayTeam.name = :awayTeamName) " +
            "AND m.leagueEntity.leagueYear = :leagueYear " +
            "AND m.leagueEntity.leagueSeason = :leagueSeason " +
            "AND m.leagueEntity.leagueNumber = :leagueNumber " +
            "AND m.leagueEntity.leagueGroup = :leagueGroup")
    MatchEntity getMatchEntity(
            @Param("homeTeamName") String homeTeamName,
            @Param("awayTeamName") String awayTeamName,
            @Param("leagueYear") String leagueYear,
            @Param("leagueSeason") String leagueSeason,
            @Param("leagueNumber") String leagueNumber,
            @Param("leagueGroup") String leagueGroup);

    /**
     * Query to fetch a specific match for given time, and given teams.
     */
    @Query("SELECT m FROM matches m WHERE (m.time=:time AND m.homeTeam.name = :homeTeamName AND m.awayTeam.name = :awayTeamName) ")
    MatchEntity getMatch(@Param("time") LocalDateTime time,
                         @Param("homeTeamName") String homeTeam,
                         @Param("awayTeamName") String awayTeam);
}
