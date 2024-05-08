package com.branik.updater.restservice.parser;

import com.branik.updater.core.model.rest.RowRestModel;
import com.branik.updater.restservice.util.TableToJsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class PitchTableParser implements TableParser {
    private final TableToJsonConverter tableToJsonConverter;

    @Autowired
    public PitchTableParser(TableToJsonConverter tableToJsonConverter) {
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
                int pitchNumbers = rowElements.get(rowNumber).select("td").get(1).select("a").size();

                for (int pitchNumber = 0; pitchNumber < pitchNumbers; pitchNumber++) {
                    RowRestModel rowRestModel = new RowRestModel();
                    String pitchName = rowElements.get(rowNumber).select("td").get(0).text();
                    String pitchAbbr = rowElements.get(rowNumber).select("td").get(1).select("a").get(pitchNumber).text();
                    String pitchDescription = rowElements.get(rowNumber).select("td").get(2).text();

                    rowRestModel.addColumn(headers.get(0), pitchName);
                    rowRestModel.addColumn(headers.get(1), pitchAbbr);
                    rowRestModel.addColumn(headers.get(2),pitchDescription);
                    rowRestModels.add(rowRestModel);
                }
            }
        }
        try {
            return tableToJsonConverter.convert(rowRestModels);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
