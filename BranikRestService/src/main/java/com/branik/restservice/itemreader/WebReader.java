package com.branik.restservice.itemreader;

public interface WebReader<T> {
    T read(String url);
    T read();
}
