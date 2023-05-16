package com.branik.updater.restservice.parser;

import com.branik.updater.core.model.rest.GoalRestModel;
import com.branik.updater.core.model.rest.LeagueQueryModel;
import com.branik.updater.core.model.rest.TeamRestModel;
import com.branik.updater.core.util.URLUtil;
import com.branik.updater.restservice.parser.json.JsonKeys;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class DetailedTableParser implements TableParser {
    // Main Container Element of the details table.
    // Contains 11 Elements inside. (11 Games for Full season)
    public static final String COMPONENT_TABLE_HAS_WRAPPER = "component__table has-wrapper";

    // Date Table and Comment Table lies within this container.
    // Contains 2 Elements inside.
    public static final String COMPONENT_TABLE_IS_INSIDE = "component__table is-inside";

    // Playerlist table, GoalScorer Table, Karty Table lies within this container.
    // Contains 2 Elements inside.
    public static final String COMPONENT_TABLE_IS_INSIDE_HAS_SMALLER_TEXT = "component__table is-inside has-smaller-text";

    public static final String TD = "td";
    public static final String TH = "th";
    public static final String SPAN = "span";

    // CSS Class for MVP
    public static final String IS_BEST = ".is-best";
    // CSS Class for Captain
    public static final String IS_CAPTAIN = ".is-captain";
    public static final String IS_BEST_AND_A_CAPTAIN = ".is-best-and-captain";

    public static final int TEAMS_ELEMENT_INDEX = 0;
    public static final int HOME_TEAM_SCORER_INDEX = 0;
    public static final int HOME_TEAM_YELLOW_CARD_INDEX = 1;
    public static final int AWAY_TEAM_SCORER_INDEX = 3;
    public static final int AWAY_TEAM_YELLOW_CARD_INDEX = 4;

    public static final int HOME_TEAM_DATA_INDEX = 0;
    public static final int AWAY_TEAM_DATA_INDEX = 2;
    public static final int HOME_TEAM_INDEX = 0;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String parse(Document document, String htmlTableElement) {
        Elements mainElement = document.getElementsByClass(htmlTableElement);
        LeagueQueryModel leagueQueryFromURL = URLUtil.getLeagueQueryFromURL(document.baseUri());
        ArrayNode allGamesArrayNode = objectMapper.createArrayNode();
        ArrayList<Element> allGameTables = new ArrayList<>(mainElement);
        for (Element singleGameTableElement : allGameTables) { // There are 11 elements.
            ObjectNode eachGameNode = objectMapper.createObjectNode();
            Elements matchDetailsAndScoreElements = singleGameTableElement.getElementsByClass(COMPONENT_TABLE_IS_INSIDE);
            Elements lineUpAndScorerElements = singleGameTableElement.getElementsByClass(COMPONENT_TABLE_IS_INSIDE_HAS_SMALLER_TEXT);

            buildMatchDetailsAndScoreNode(eachGameNode, matchDetailsAndScoreElements);
            buildTeamLineUpDetailsAndScorersNode(eachGameNode, lineUpAndScorerElements);

            eachGameNode.set(JsonKeys.LEAGUE_DETAILS_KEY, objectMapper.valueToTree(leagueQueryFromURL));
            allGamesArrayNode.add(eachGameNode);
        }
        return allGamesArrayNode.toPrettyString();
    }

    private void buildTeamLineUpDetailsAndScorersNode(ObjectNode objectNode, Elements lineUpAndScorerElements) {
        Map<String, TeamRestModel> teamBuilderMap = new HashMap<>();
        if (lineUpAndScorerElements.isEmpty()) { // TODO fix here.
            TeamRestModel hometeam = TeamRestModel.builder().name(objectNode.get("home").asText()).yellowCards(new ArrayList<>()).players(new ArrayList<>()).build();
            TeamRestModel awayteam = TeamRestModel.builder().name(objectNode.get("away").asText()).yellowCards(new ArrayList<>()).players(new ArrayList<>()).build();

            teamBuilderMap.put(JsonKeys.HOME_TEAM_KEY, hometeam);
            teamBuilderMap.put(JsonKeys.AWAY_TEAM_KEY, awayteam);
        } else {
            teamBuilderMap = buildGameDetails(lineUpAndScorerElements);
        }

        JsonNode teamsNode = objectMapper.valueToTree(teamBuilderMap);
        objectNode.set(JsonKeys.TEAMS_KEY, teamsNode);
    }


    private Map<String, TeamRestModel> buildGameDetails(Elements lineUpAndScorerElements) {
        Map<String, TeamRestModel> teamBuilderMap = new HashMap<>();
        Element teamsElement = lineUpAndScorerElements.get(TEAMS_ELEMENT_INDEX);
        Elements teamData = teamsElement.select(TD);

        List<String> teamNamesList = teamsElement.select(TH).eachText();

        for (int i = 0; i < teamNamesList.size(); i++) {
            TeamRestModel.TeamRestModelBuilder teamBuilder = TeamRestModel.builder();
            String teamName = teamNamesList.get(i);
            teamBuilder.name(teamName);

            Element secondPart = lineUpAndScorerElements.get(1);
            List<String> scorers = secondPart.select(TD).get(i == HOME_TEAM_INDEX ? HOME_TEAM_SCORER_INDEX : AWAY_TEAM_SCORER_INDEX).textNodes().stream().map(TextNode::text).map(String::trim).filter(o -> !o.equals("")).collect(Collectors.toList());// home team scorers
            List<String> yellowCards = secondPart.select(TD).get(i == HOME_TEAM_INDEX ? HOME_TEAM_YELLOW_CARD_INDEX : AWAY_TEAM_YELLOW_CARD_INDEX).select(SPAN).eachText();// home team card
            teamBuilder.goalRestModels(fetchGoals(scorers));
            teamBuilder.yellowCards(yellowCards);
            Elements teamElement = teamData.select(TD).get(i == HOME_TEAM_INDEX ? HOME_TEAM_DATA_INDEX : AWAY_TEAM_DATA_INDEX).getAllElements();
            teamBuilderMap.put(i == HOME_TEAM_INDEX ? "home" : "away", buildTeam(teamElement, teamBuilder).build());
        }

        return teamBuilderMap;
    }

    private List<GoalRestModel> fetchGoals(List<String> goalListString) {
        List<GoalRestModel> goalRestModelList = new ArrayList<>();
        for (String goalString : goalListString) {
            String[] split = goalString.replace(",", "").split("\\.");
            for (int i = 0; i <= split.length - 2; i++) {
                goalRestModelList.add(new GoalRestModel(split[i], split[split.length - 1].trim()));
            }
        }
        return goalRestModelList;
    }

    private TeamRestModel.TeamRestModelBuilder buildTeam(Elements teamElement, TeamRestModel.TeamRestModelBuilder builder) {
        List<String> teamPlayerList = fetchPlayerList(teamElement.select(TD).text());
        String isBestAndACaptain = teamElement.select(IS_BEST_AND_A_CAPTAIN).text();
        if (isBestAndACaptain.isEmpty()) {
            builder.mvp(teamElement.select(IS_BEST).text());
            builder.captain(teamElement.select(IS_CAPTAIN).text());
        } else {
            builder.mvp(isBestAndACaptain);
            builder.captain(isBestAndACaptain);
        }


        return builder.players(teamPlayerList);
    }

    private List<String> fetchPlayerList(String team) {
        if (team.isEmpty()) {
            return new ArrayList<>();
        }
        String[] players = team.split("–");
        String firstPlayer = players[0].trim();
        String[] restOfPlayers = players[1].split(",");
        List<String> playerList = Stream.of(restOfPlayers).map(String::trim).collect(Collectors.toList());
        playerList.add(firstPlayer);
        return playerList;
    }

    private void buildMatchDetailsAndScoreNode(ObjectNode objectNode, Elements elementsByClass) {
        for (Element byClass : elementsByClass) {
            List<String> rows = byClass.select("tr th").eachText();
            Elements values = byClass.select("tr td");

            for (int i = 0; i < rows.size(); i++) {
                try {
                    Element element1 = values.get(i);
                    Elements teams = element1.select("a");
                    if (!teams.isEmpty() && teams.size() > 1) {
                        objectNode.put(JsonKeys.HOME_TEAM_KEY, teams.get(0).text());
                        objectNode.put(JsonKeys.AWAY_TEAM_KEY, teams.get(1).text());
                    } else {
                        objectNode.put(rows.get(i), values.get(i).text());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
