package com.branik.updater.database.services.repository;

import com.branik.updater.database.model.db.LeagueEntity;
import com.branik.updater.database.repository.LeagueRepository;
import com.typesafe.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.branik.updater.database.constants.ConfigKeys.*;
import static com.branik.updater.database.constants.ConfigKeys.LEAGUE_NUMBER;

@Service
public class LeagueRepositoryService {
    private final LeagueRepository leagueRepository;
    private Config config;

    @Autowired
    public LeagueRepositoryService(LeagueRepository leagueRepository, Config config) {
        this.leagueRepository = leagueRepository;
        this.config = config;
    }

    public LeagueEntity getLeague(String year, String number, String group, String season) {
        return leagueRepository.findByLeagueYearAndLeagueSeasonAndLeagueNumberAndLeagueGroup(year, season, number, group);
    }

    public void initialize() {
        String leagueYear = config.getString(LEAGUE_YEAR);
        String leagueSeason = config.getString(LEAGUE_SEASON);
        String leagueGroup = config.getString(LEAGUE_GROUP);
        String leagueNumber = config.getString(LEAGUE_NUMBER);

        LeagueEntity leagueEntity = leagueRepository.findByLeagueYearAndLeagueSeasonAndLeagueNumberAndLeagueGroup(leagueYear, leagueSeason, leagueNumber, leagueGroup);
        if (leagueEntity == null) {
            LeagueEntity newLeague = LeagueEntity.builder().leagueYear(leagueYear).leagueGroup(leagueGroup).leagueSeason(leagueSeason).leagueNumber(leagueNumber).isActive(true).build();
            leagueRepository.save(newLeague);
        }
    }
}
