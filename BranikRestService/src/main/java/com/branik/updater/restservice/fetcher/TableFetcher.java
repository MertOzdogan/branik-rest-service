package com.branik.updater.restservice.fetcher;


import com.branik.updater.core.model.rest.LeagueQueryModel;

public interface TableFetcher {
    String getData(LeagueQueryModel leagueQueryModel);
}
