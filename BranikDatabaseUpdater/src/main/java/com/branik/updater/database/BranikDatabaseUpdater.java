package com.branik.updater.database;

import com.branik.updater.database.manager.BranikUpdateManager;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BranikDatabaseUpdater {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(BranikDatabaseUpdater.class).web(WebApplicationType.NONE).run(args);
        BranikUpdateManager branikUpdateManager = applicationContext.getBean(BranikUpdateManager.class);
        branikUpdateManager.start();
    }
}