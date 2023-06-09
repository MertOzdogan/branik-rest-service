package com.branik.updater.restservice.beans;

import com.branik.updater.restservice.util.ConfigLoader;
import com.typesafe.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestBeans {

    @Bean
    public Config tableServiceConfiguration() {
        return ConfigLoader.getConfig();
    }

    @Bean
    public Config config() {
        return ConfigLoader.getConfig();
    }
}
