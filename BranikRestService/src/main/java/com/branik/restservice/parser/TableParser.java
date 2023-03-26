package com.branik.restservice.parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public interface TableParser {

    String parse(Document document,String htmlTableElement);
}
