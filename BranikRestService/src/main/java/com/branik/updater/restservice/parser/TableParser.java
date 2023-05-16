package com.branik.updater.restservice.parser;

import org.jsoup.nodes.Document;

public interface TableParser {

    String parse(Document document,String htmlTableElement);
}
