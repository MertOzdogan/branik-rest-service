package com.branik.updater.database.services.repository;

import com.branik.updater.database.model.db.LeagueEntity;
import com.branik.updater.database.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeagueRepositoryService {
    private final LeagueRepository leagueRepository;

    @Autowired
    public LeagueRepositoryService(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }


    public LeagueEntity getLeague(String year, String number, String group, String season) {
        return leagueRepository.findByLeagueYearAndLeagueSeasonAndLeagueNumberAndLeagueGroup(year, number, group, season);
    }
}
