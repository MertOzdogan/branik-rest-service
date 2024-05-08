package com.branik.updater.database.manager;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class BranikUpdateManagerImpl implements BranikUpdateManager {
    private final ScheduledExecutorService websiteWatchdog = Executors.newSingleThreadScheduledExecutor();
    private String websiteDocument;
    private final AtomicBoolean firstRun = new AtomicBoolean(true);
    private DatabaseManager databaseManager;
    private Config config;

    @Autowired
    public BranikUpdateManagerImpl(DatabaseManager databaseManager, Config config) {
        this.databaseManager = databaseManager;
        this.config = config;
    }

    @Override
    public void start() {
        triggerUpdate();
//        websiteWatchdog.scheduleAtFixedRate(() -> {
//            if (firstRun.get()) {
//                // Trigger the update
//                log.info("Starting with the first update...");
//                websiteDocument = getWebsiteDocument();
//                triggerUpdate();
//                log.info("First update completed.");
//                firstRun.set(false);
//            } else {
//                String websiteWatchdogDocument = getWebsiteDocument();
//                if (!websiteWatchdogDocument.equals(websiteDocument)) {
//                    //Trigger the update
//                    triggerUpdate();
//                    websiteDocument = websiteWatchdogDocument;
//                    log.info("Changes detected. Update completed.");
//                } else {
//                    log.info("No changes detected... skipping...");
//                }
//
//            }
//        }, 0, 1, TimeUnit.MINUTES);
    }

    private void triggerUpdate() {
//        databaseManager.initializePitches();
//        databaseManager.updateLeague();
//        databaseManager.initializePlayers();
//        databaseManager.initializeTeams();
//        databaseManager.updateTeams();
//        databaseManager.updateStandings();
//        databaseManager.initializeMatches();
        databaseManager.updateMatchDetails();
    }

    private String getWebsiteDocument() {
        try {
            return Jsoup.connect("https://www.psmf.cz/souteze/2023-hanspaulska-liga-jaro/7-b").get().html();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
