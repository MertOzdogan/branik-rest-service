package com.branik.updater.database.itemreader;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class TableItemReader<TABLE_MODEL> implements ItemReader<List<TABLE_MODEL>> {
    private  String apiUrl;
    private final RestTemplate restTemplate;
    private final Class<TABLE_MODEL[]> clazz;

    public TableItemReader(String apiUrl, RestTemplate restTemplate, Class<TABLE_MODEL[]> clazz) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        this.clazz = clazz;
    }

    @Override
    public List<TABLE_MODEL> read() { // TODO ANASININ AMIYMIS BU DA
        ResponseEntity<? extends TABLE_MODEL[]> exchange = restTemplate.exchange(apiUrl, HttpMethod.GET, null, this.clazz);
        return Arrays.asList(exchange.getBody());
    }
}
