package com.branik.updater.restservice.fetcher;

import com.branik.updater.core.model.rest.LeagueQueryModel;
import com.branik.updater.restservice.service.TableService;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.branik.updater.restservice.constants.ConfigKeys.*;

@Service
@Slf4j
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
            String url = createURL(leagueQueryModel);
            log.info("Getting data for: {} ",url);
            String tableData = this.tableService.getTableData(url, tableHtmlElement);
            log.info("Data: {} ",tableData);
            return tableData;
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
