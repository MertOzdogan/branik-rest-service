package com.branik.restservice.util;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigLoader {
    public static Config getConfig() {
        return ConfigFactory.load();
    }
}
