package com.branik.restservice.service;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface WebConnector{
    Document connect(String url) throws IOException;
}
