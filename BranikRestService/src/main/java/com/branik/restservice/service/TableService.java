package com.branik.restservice.service;


import java.io.IOException;

public interface TableService {
    String getTableData(String url, String htmlTableElement) throws IOException;
}
