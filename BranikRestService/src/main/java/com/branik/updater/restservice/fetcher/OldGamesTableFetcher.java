package com.branik.updater.restservice.fetcher;

import com.branik.updater.core.model.rest.LeagueQueryModel;
import com.branik.updater.restservice.service.TableService;
import com.branik.updater.core.util.URLUtil;
import com.branik.updater.restservice.constants.ConfigKeys;
import com.typesafe.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OldGamesTableFetcher implements TableFetcher {
    private final String tableHtmlElement;
    private final TableService tableService;
    private final String urlString;
    private final Config config;

    @Autowired
    public OldGamesTableFetcher(Config config, TableService oldGamesTableService) {
        this.tableService = oldGamesTableService;
        this.config = config;
        this.tableHtmlElement = config.getString(ConfigKeys.OLD_GAMES_TABLE_HTML_ELEMENT);
        this.urlString = config.getString(ConfigKeys.OLD_GAMES_TABLE_URL);
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
        return urlString.replace(config.getString(ConfigKeys.YEAR_PLACE_HOLDER_KEY), leagueQueryModel.getYear())
                .replace(config.getString(ConfigKeys.LEAGUE_PLACE_HOLDER_KEY), leagueQueryModel.getLeague())
                .replace(config.getString(ConfigKeys.DIVISION_PLACE_HOLDER_KEY), leagueQueryModel.getGroup())
                .replace(config.getString(ConfigKeys.SEASON_PLACE_HOLDER_KEY), leagueQueryModel.getSeason())
                .replace(config.getString(ConfigKeys.TEAM_PLACE_HOLDER_KEY), URLUtil.getTeamNameInUrlVersion(leagueQueryModel.getTeam()));
    }
}
