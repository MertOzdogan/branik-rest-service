package com.branik.updater.restservice.fetcher;

import com.branik.updater.core.model.rest.LeagueQueryModel;
import com.branik.updater.restservice.service.TableService;
import com.typesafe.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.branik.updater.restservice.constants.ConfigKeys.*;

@Service
public class FixturesTableFetcher implements TableFetcher {
    private final String tableHtmlElement;
    private final TableService tableService;
    private final String urlString;
    private final Config config;

    @Autowired
    public FixturesTableFetcher(Config config,TableService genericTableService) {
        this.config = config;
        this.tableService = genericTableService;
        this.tableHtmlElement = config.getString(FIXTURE_TABLE_HTML_ELEMENT);
        this.urlString = config.getString(FIXTURES_TABLE_URL);
    }

    @Override
    public String getData(LeagueQueryModel leagueQueryModel) {
        try {
            return this.tableService.getTableData(createURL(leagueQueryModel), tableHtmlElement);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createURL(LeagueQueryModel leagueQueryModel) {
        return urlString.replace(config.getString(YEAR_PLACE_HOLDER_KEY), leagueQueryModel.getYear())
                .replace(config.getString(LEAGUE_PLACE_HOLDER_KEY), leagueQueryModel.getLeague())
                .replace(config.getString(DIVISION_PLACE_HOLDER_KEY), leagueQueryModel.getGroup())
                .replace(config.getString(SEASON_PLACE_HOLDER_KEY), leagueQueryModel.getSeason());
    }
}
