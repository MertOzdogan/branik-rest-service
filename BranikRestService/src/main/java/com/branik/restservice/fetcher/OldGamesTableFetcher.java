package com.branik.restservice.fetcher;

import com.branik.restservice.model.LeagueQueryModel;
import com.branik.restservice.service.TableService;
import com.branik.restservice.util.URLUtil;
import com.typesafe.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.branik.restservice.constants.ConfigKeys.*;

@Service
public class OldGamesTableFetcher implements TableFetcher {
    private final String tableHtmlElement;
    private final TableService tableService;
    private final String urlString;
    private final Config config;

    @Autowired
    public OldGamesTableFetcher(Config config, TableService genericTableService) {
        this.tableService = genericTableService;
        this.config = config;
        this.tableHtmlElement = config.getString(OLD_GAMES_TABLE_HTML_ELEMENT);
        this.urlString = config.getString(OLD_GAMES_TABLE_URL);
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
                .replace(config.getString(SEASON_PLACE_HOLDER_KEY), leagueQueryModel.getSeason())
                .replace(config.getString(TEAM_PLACE_HOLDER_KEY), URLUtil.getTeamNameInUrlVersion(leagueQueryModel.getTeam()));
    }
}
