package com.branik.updater.restservice.service;

import com.branik.updater.restservice.parser.TableParser;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PitchTableService implements TableService {
    private final TableParser tableParser;
    private final WebConnector webConnector;

    @Autowired
    public PitchTableService(TableParser pitchTableParser, WebConnector webConnector) {
        this.tableParser = pitchTableParser;
        this.webConnector = webConnector;
    }

    public String getTableData(String url, String tableHtmlElement) throws IOException {
        Document fullWeb = webConnector.connect(url);
        return tableParser.parse(fullWeb, tableHtmlElement);
    }
}
