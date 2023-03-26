package com.branik.restservice.fetcher;

import com.branik.restservice.model.LeagueQueryModel;

public interface TableFetcher {
    String getData(LeagueQueryModel leagueQueryModel);
}
