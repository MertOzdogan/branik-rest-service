package com.branik.updater.database.itemreader;

public interface WebReader<T> {
    T read(String url);
    T read();
}
