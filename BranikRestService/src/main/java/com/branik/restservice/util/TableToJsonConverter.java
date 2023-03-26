package com.branik.restservice.util;

import com.branik.restservice.model.RowRestModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TableToJsonConverter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String convert(List<RowRestModel> parse) throws JsonProcessingException {
        List<Map<String, String>> collect = parse.stream().map(RowRestModel::getColumnMap).collect(Collectors.toList());
        return objectMapper.writeValueAsString(collect);
    }
}
