package com.branik.updater.restservice.parser;

import com.branik.updater.core.model.rest.RowRestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


@Service
public class OldGamesTableParser implements TableParser {
    public static final int DATE_INDEX = 0;
    public static final int TIME_INDEX = 1;
    public static final int PITCH_INDEX = 2;
    public static final int TEAMS_INDEX = 3;
    public static final int KOLO_INDEX = 4;
    public static final int SCORES_INDEX = 5;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String parse(Document document, String htmlTableElement) {
        Elements table = document.getElementsByClass(htmlTableElement);

        Elements rowElements = table.select("tr");

        List<String> headers = new LinkedList<>();
        List<RowRestModel> rowRestModels = new LinkedList<>();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (int rowNumber = 0; rowNumber < rowElements.size(); rowNumber++) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            if (rowNumber == 0) {
                continue; // Skip headers.
            } else {
                Elements row = rowElements.get(rowNumber).select("td");
                String date = row.get(DATE_INDEX).text().split(" ")[1];
                String time = row.get(TIME_INDEX).text();
                String pitch = row.get(PITCH_INDEX).text();
                Element teamsElement = row.get(TEAMS_INDEX);
                Elements teams = teamsElement.select("a");
                String homeTeam = teams.get(0).text();
                String awayTeam = teams.get(1).text();
                String kolo = row.get(KOLO_INDEX).text().replace(".", "");
                String[] scores = row.get(SCORES_INDEX).text().split(":");
                String homeTeamScore = scores[0];
                String awayTeamScore = scores[1];

                objectNode.put("date", date);
                objectNode.put("time", time);
                objectNode.put("pitch", pitch);
                objectNode.put("homeTeam", homeTeam);
                objectNode.put("awayTeam", awayTeam);
                objectNode.put("kolo", kolo);
                objectNode.put("homeTeamScore", homeTeamScore);
                objectNode.put("awayTeamScore", awayTeamScore);
                arrayNode.add(objectNode);
            }
        }
        return arrayNode.toPrettyString();
    }
}
