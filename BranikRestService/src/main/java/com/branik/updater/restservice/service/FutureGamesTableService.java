package com.branik.updater.restservice.service;

import com.branik.updater.restservice.parser.TableParser;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FutureGamesTableService implements TableService {
    private final TableParser futureGamesParser;
    private final WebConnector webConnector;

    @Autowired
    public FutureGamesTableService(TableParser futureGamesParser,
                                   JSoupWebConnector jSoupWebConnector) {
        this.futureGamesParser = futureGamesParser;
        this.webConnector = jSoupWebConnector;
    }

    @Override
    public String getTableData(String url, String htmlTableElement) throws IOException {
        Document document = webConnector.connect(url);
        return futureGamesParser.parse(document,htmlTableElement);
    }
}
