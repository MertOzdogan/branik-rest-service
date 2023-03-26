package com.branik.restservice.service;

import com.branik.restservice.parser.TableParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GenericTableService implements TableService {
    private final TableParser tableParser;
    private final WebConnector webConnector;

    @Autowired
    public GenericTableService(TableParser genericTableParser, WebConnector webConnector) {
        this.tableParser = genericTableParser;
        this.webConnector = webConnector;
    }


    public String getTableData(String url, String tableHtmlElement) throws IOException {
        Document fullWeb = webConnector.connect(url);
        return tableParser.parse(fullWeb,tableHtmlElement);
    }
}
