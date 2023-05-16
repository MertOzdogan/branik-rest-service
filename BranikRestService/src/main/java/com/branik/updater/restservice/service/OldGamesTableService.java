package com.branik.updater.restservice.service;

import com.branik.updater.restservice.parser.TableParser;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OldGamesTableService implements TableService {

    private final TableParser oldGamesTableParser;
    private final WebConnector jSoupWebConnector;


    public OldGamesTableService(TableParser oldGamesTableParser,
                                JSoupWebConnector jSoupWebConnector) {
        this.oldGamesTableParser = oldGamesTableParser;
        this.jSoupWebConnector = jSoupWebConnector;
    }

    @Override
    public String getTableData(String url, String htmlTableElement) throws IOException {
        Document document = jSoupWebConnector.connect(url);
        return oldGamesTableParser.parse(document, htmlTableElement);
    }
}
