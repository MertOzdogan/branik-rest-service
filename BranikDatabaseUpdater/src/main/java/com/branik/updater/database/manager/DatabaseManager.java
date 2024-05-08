package com.branik.updater.database.manager;

public interface DatabaseManager {
    void updateAll();

    void updateStandings();

    void updateMatchDetails();

    void initializeMatches();

    void updateTeams();

    void initializePlayers();

    void initializeTeams();

    void updateLeague();

    void initializePitches();
}
