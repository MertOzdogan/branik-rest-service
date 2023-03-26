package com.branik.restservice.itemreader;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class AlternativeTableItemReader<TABLE_MODEL> implements WebReader<List<TABLE_MODEL>> {
    private String apiUrl;
    private final RestTemplate restTemplate;
    private final Class<TABLE_MODEL[]> clazz;

    public AlternativeTableItemReader(String apiUrl, RestTemplate restTemplate, Class<TABLE_MODEL[]> clazz) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        this.clazz = clazz;
    }

    @Override
    public List<TABLE_MODEL> read(String url) { // TODO ANASININ AMIYMIS BU DA
        try {
            ResponseEntity<? extends TABLE_MODEL[]> exchange = restTemplate.exchange(url, HttpMethod.GET, null, this.clazz);
            return Arrays.asList(exchange.getBody());
        } catch (Exception e) {
            System.err.println(url);
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<TABLE_MODEL> read() {
        if (apiUrl == null) {
            throw new RuntimeException("Url can't be null");
        }
        return read(this.apiUrl);
    }
}
