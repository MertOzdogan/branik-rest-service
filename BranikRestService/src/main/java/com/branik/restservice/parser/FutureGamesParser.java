package com.branik.restservice.parser;

import com.branik.restservice.model.FutureGameTeamRestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FutureGamesParser implements TableParser {
    public static final int DATE_INDEX = 0;
    public static final int TIME_INDEX = 1;
    public static final int PITCH_INDEX = 2;
    public static final int TEAMS_INDEX = 3;
    public static final int KOLO_INDEX = 4;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String parse(Document document, String htmlTableElement) {
        Elements table = document.getElementsByClass(htmlTableElement);

        ArrayNode nextGamesArrayNode = objectMapper.createArrayNode();
        Elements rowElements = table.select("tr");
        for (Element rowElement : rowElements) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            if (rowElements.indexOf(rowElement) != 0) {
                Elements columns = rowElement.select("td");
                String date = columns.get(DATE_INDEX).text();
                objectNode.put("date", date.split(" ")[1]);
                String time = columns.get(TIME_INDEX).text();
                objectNode.put("time", time);
                String pitch = columns.get(PITCH_INDEX).text();
                objectNode.put("pitch", pitch);
                Elements elements = columns.get(TEAMS_INDEX).select("a");
                Map<String, FutureGameTeamRestModel> teamsMap = new HashMap<>();
                for (Element element : elements) {
                    if (element.hasAttr("data-dccolor") && element.hasAttr("data-dctitle")) {
                        teamsMap.get(element.attr("data-dctitle")).setColor(element.attr("data-dccolor"));
                    } else {
                        String teamName = element.attr("title");
                        teamsMap.put(teamName, FutureGameTeamRestModel.builder().name(teamName).build());
                    }
                }
                objectNode.set("teams", objectMapper.valueToTree(teamsMap.values()));
                String kolo = columns.get(KOLO_INDEX).text().replace(".", "");
                objectNode.put("kolo", kolo);
                nextGamesArrayNode.add(objectNode);
            }
        }
        return nextGamesArrayNode.toPrettyString();
    }
}
