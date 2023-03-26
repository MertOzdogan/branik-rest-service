package com.branik.restservice.beans;

import com.branik.restservice.util.ConfigLoader;
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
