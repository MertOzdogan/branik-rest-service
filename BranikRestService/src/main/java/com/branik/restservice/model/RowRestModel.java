package com.branik.restservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@AllArgsConstructor
public class RowRestModel {
    private final Map<String, String> columnMap = new HashMap<>();

    public void addColumn(String header, String value) {
        columnMap.put(header, value);
    }
}
