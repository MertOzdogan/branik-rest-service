package com.branik.updater.database.util;

import com.branik.updater.database.model.db.LeagueEntity;
import com.branik.updater.database.repository.LeagueRepository;
import com.typesafe.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.branik.updater.database.constants.ConfigKeys.*;
import static com.branik.updater.database.constants.ConfigKeys.LEAGUE_GROUP;

@Component
public class CurrentLeagueUtil {
    private final Config config;
    private final LeagueRepository leagueRepository;

    @Autowired
    public CurrentLeagueUtil(Config config, LeagueRepository leagueRepository) {
        this.config = config;
        this.leagueRepository = leagueRepository;
    }

    public LeagueEntity getCurrentLeague() {
        return leagueRepository.findByLeagueYearAndLeagueSeasonAndLeagueNumberAndLeagueGroup(
                config.getString(LEAGUE_YEAR),
                config.getString(LEAGUE_SEASON),
                config.getString(LEAGUE_NUMBER),
                config.getString(LEAGUE_GROUP));
    }
}
