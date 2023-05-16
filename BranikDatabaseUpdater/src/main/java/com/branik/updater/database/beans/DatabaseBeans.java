package com.branik.updater.database.beans;

import com.branik.updater.core.model.rest.*;
import com.branik.updater.core.model.rest.match.DetailedMatchRestModel;
import com.branik.updater.core.model.rest.match.FutureMatchRestModel;
import com.branik.updater.core.model.rest.match.OldMatchRestModel;
import com.branik.updater.core.util.ConfigLoader;
import com.branik.updater.database.itemreader.AlternativeTableItemReader;
import com.branik.updater.database.itemreader.WebReader;
import com.typesafe.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class DatabaseBeans {
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebReader<List<StatsRestModel>> statsTableReader() {
        String apiUrl = "http://localhost:8080/fetch?id=statistics-table";
        return new AlternativeTableItemReader<>(apiUrl, new RestTemplate(), StatsRestModel[].class);
    }
    @Bean
    public WebReader<List<StandingsRestModel>> standingsTableRestReader() {
        return new AlternativeTableItemReader<>(null, new RestTemplate(), StandingsRestModel[].class);
    }

    @Bean
    public WebReader<List<DetailedMatchRestModel>> matchDetailsReader() {
        return new AlternativeTableItemReader<>("http://localhost:8080/matches?year=2022&season=podzim&league=7&group=c&team=branik-city", new RestTemplate(), DetailedMatchRestModel[].class);
    }

    @Bean
    public WebReader<List<PitchRestModel>> pitchReader() {
        String apiUrl = "http://localhost:8080/fetch?id=pitch-table";
        return new AlternativeTableItemReader<>(apiUrl, new RestTemplate(), PitchRestModel[].class);
    }

    @Bean
    public WebReader<List<FutureMatchRestModel>> futureMatchReader() {
        String apiUrl = "http://localhost:8080/fetch?id=pitch-table";
        return new AlternativeTableItemReader<>(apiUrl, new RestTemplate(), FutureMatchRestModel[].class);
    }

    @Bean
    public WebReader<List<OldMatchRestModel>> pastMatchReader() {
        String apiUrl = "http://localhost:8080/fetch?id=pitch-table";
        return new AlternativeTableItemReader<>(apiUrl, new RestTemplate(), OldMatchRestModel[].class);
    }

    @Bean
    public Config config() {
        return ConfigLoader.getConfig();
    }
}
