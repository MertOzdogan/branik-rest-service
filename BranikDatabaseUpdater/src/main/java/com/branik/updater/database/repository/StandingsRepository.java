package com.branik.updater.database.repository;

import com.branik.updater.database.model.db.StandingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StandingsRepository extends JpaRepository<StandingEntity, Long> {
    StandingEntity findByTeamNameAndLeagueLeagueYearAndLeagueLeagueSeasonAndLeagueLeagueNumberAndLeagueLeagueGroup(String teamName, String leagueYear, String leagueSeason, String leagueNumber, String leagueGroup);
    List<StandingEntity> findByLeagueLeagueYearAndLeagueLeagueSeasonAndLeagueLeagueNumberAndLeagueLeagueGroup(String leagueYear, String leagueSeason, String leagueNumber, String leagueGroup);
    List<StandingEntity> findStandingEntitiesByLeagueId(Long leagueId);
}
