package com.branik.restservice.fetcher;

import com.branik.restservice.model.LeagueQueryModel;
import com.branik.restservice.service.TableService;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.branik.restservice.constants.ConfigKeys.PITCH_TABLE_HTML_ELEMENT;
import static com.branik.restservice.constants.ConfigKeys.PITCH_TABLE_URL;


@Service
public class PitchTableFetcher implements TableFetcher {
    private final String url;
    private final String tableHtmlElement;
    private final TableService genericTableService;

    @Autowired
    public PitchTableFetcher(Config config, TableService genericTableService) {
        this.genericTableService = genericTableService;
        this.url = config.getString(PITCH_TABLE_URL);
        this.tableHtmlElement = config.getString(PITCH_TABLE_HTML_ELEMENT);
    }

    @Override
    public String getData(LeagueQueryModel leagueQueryModel) {
        try {
            return genericTableService.getTableData(url, tableHtmlElement);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
