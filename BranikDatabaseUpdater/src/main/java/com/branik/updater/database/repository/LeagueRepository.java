package com.branik.updater.database.repository;


import com.branik.updater.database.model.db.LeagueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<LeagueEntity, Long> {
    LeagueEntity findByLeagueYearAndLeagueSeasonAndLeagueNumberAndLeagueGroup(String league_year, String league_season, String league_number, String league_group);
}
