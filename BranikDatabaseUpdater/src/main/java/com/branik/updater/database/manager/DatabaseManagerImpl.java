package com.branik.updater.database.manager;

import com.branik.updater.database.services.repository.LeagueRepositoryService;
import com.branik.updater.database.services.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseManagerImpl implements DatabaseManager {

    private final StandingTableService standingsTableService;
    private final MatchDetailsTableService matchDetailsTableService;
    private final TeamTableService teamTableService;
    private final PlayerTableService playerTableService;
    private final LeagueRepositoryService leagueRepositoryService;
    private final PitchTableService pitchTableService;

    @Autowired
    public DatabaseManagerImpl(StandingTableService standingTableService,
                               MatchDetailsTableService matchDetailsTableService,
                               TeamTableService teamTableService,
                               PlayerTableService playerTableService,
                               LeagueRepositoryService leagueRepositoryService, PitchTableService pitchTableService) {
        this.standingsTableService = standingTableService;
        this.matchDetailsTableService = matchDetailsTableService;
        this.teamTableService = teamTableService;
        this.playerTableService = playerTableService;
        this.leagueRepositoryService = leagueRepositoryService;
        this.pitchTableService = pitchTableService;
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
        teamTableService.update();
    }

    @Override
    public void initializePlayers() {
        playerTableService.initialize();
    }

    @Override
    public void initializeTeams() {
        teamTableService.buildLeagueTeams();
    }

    @Override
    public void updateLeague() {
        leagueRepositoryService.initialize();
    }

    @Override
    public void initializePitches() {
        pitchTableService.initialize();

    }
}
