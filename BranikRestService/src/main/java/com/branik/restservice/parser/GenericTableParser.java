package com.branik.restservice.parser;

import com.branik.restservice.model.RowRestModel;
import com.branik.restservice.util.TableToJsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GenericTableParser implements TableParser {
    private final TableToJsonConverter tableToJsonConverter;

    @Autowired
    public GenericTableParser(TableToJsonConverter tableToJsonConverter) {
        this.tableToJsonConverter = tableToJsonConverter;
    }

    public String parse(Document document, String htmlTableElement) {
        Elements table = document.getElementsByClass(htmlTableElement);

        Elements rowElements = table.select("tr");

        List<String> headers = new LinkedList<>();
        List<RowRestModel> rowRestModels = new LinkedList<>();
        for (int rowNumber = 0; rowNumber < rowElements.size(); rowNumber++) {
            if (rowNumber == 0) {
                headers.addAll(rowElements.get(rowNumber).select("th").eachText());
            } else {
                List<String> columns = rowElements.get(rowNumber).select("td").eachText();
                RowRestModel rowRestModel = new RowRestModel();
                for (int i = 0; i < columns.size(); i++) {
                    rowRestModel.addColumn(headers.get(i), columns.get(i));
                }
                rowRestModels.add(rowRestModel);
            }
        }
        try {
            return tableToJsonConverter.convert(rowRestModels);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
