package com.branik.restservice.service;

import com.branik.restservice.parser.TableParser;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MatchDetailsTableService implements TableService {
    private final TableParser detailedTableParser;
    private final WebConnector webConnector;

    @Autowired
    public MatchDetailsTableService(TableParser detailedTableParser,
                                    WebConnector webConnector) {
        this.detailedTableParser = detailedTableParser;
        this.webConnector = webConnector;
    }

    @Override
    public String getTableData(String url, String htmlTableElement) throws IOException {
        Document fullWeb = webConnector.connect(url);
        return detailedTableParser.parse(fullWeb,htmlTableElement);
    }
}
