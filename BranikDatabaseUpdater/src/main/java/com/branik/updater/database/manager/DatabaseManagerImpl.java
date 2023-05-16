package com.branik.updater.database.manager;

import com.branik.updater.database.services.table.MatchDetailsTableService;
import com.branik.updater.database.services.table.PlayerTableService;
import com.branik.updater.database.services.table.StandingTableService;
import com.branik.updater.database.services.table.TeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseManagerImpl implements DatabaseManager {

    private final StandingTableService standingsTableService;
    private final MatchDetailsTableService matchDetailsTableService;
    private final TeamsService teamsService;
    private final PlayerTableService playerTableService;

    @Autowired
    public DatabaseManagerImpl(StandingTableService standingTableService,
                               MatchDetailsTableService matchDetailsTableService,
                               TeamsService teamsService,
                               PlayerTableService playerTableService) {
        this.standingsTableService = standingTableService;
        this.matchDetailsTableService = matchDetailsTableService;
        this.teamsService = teamsService;
        this.playerTableService = playerTableService;
    }

    @Override
    public void updateAll() {

    }

    @Override
    public void updateStandings() {
        standingsTableService.update();
    }

    @Override
    public void updateMatchDetails() {
        matchDetailsTableService.update();
    }

    @Override
    public void initializeMatches() {
        matchDetailsTableService.initialize();
    }

    @Override
    public void updateTeams() {
        teamsService.update();
    }

    @Override
    public void initializePlayers() {
        playerTableService.initialize();
    }

    @Override
    public void initializeTeams() {
        teamsService.buildLeagueTeams();
    }
}
