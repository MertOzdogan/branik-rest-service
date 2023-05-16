package com.branik.updater.database;

import com.branik.updater.database.manager.DatabaseManager;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BranikDatabaseUpdater {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(BranikDatabaseUpdater.class).web(WebApplicationType.NONE).run(args);
        DatabaseManager databaseController = applicationContext.getBean(DatabaseManager.class);
//        databaseController.initializeTeams();
//        databaseController.updateTeams();
        databaseController.updateStandings();
//        databaseController.initializePlayers();
//        databaseController.initializeMatches();
//        databaseController.updateMatchDetails();
    }
}