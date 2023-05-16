package com.branik.updater.restservice;

import com.branik.updater.restservice.beans.AppConfig;
import com.branik.updater.restservice.controller.RestTableController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AppConfig.class})
@ComponentScan(basePackageClasses = RestTableController.class)
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
