package com.branik.updater.restservice.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JSoupWebConnector implements WebConnector {
    @Override
    public Document connect(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        return connection.get();
    }
}
