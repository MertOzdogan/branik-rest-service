package com.branik.updater.restservice.controller;

import com.branik.updater.core.model.rest.LeagueQueryModel;
import com.branik.updater.restservice.fetcher.TableFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTableController {
    private final TableFetcher pitchTableFetcher;
    private final TableFetcher fixturesTableFetcher;
    private final TableFetcher futureTableFetcher;
    private final TableFetcher oldGamesTableFetcher;
    private final TableFetcher statisticsTableFetcher;
    private final TableFetcher matchTableFetcher;

    @Autowired
    public RestTableController(TableFetcher pitchTableFetcher,
                               TableFetcher fixturesTableFetcher,
                               TableFetcher futureTableFetcher,
                               TableFetcher oldGamesTableFetcher,
                               TableFetcher statisticsTableFetcher,
                               TableFetcher matchTableFetcher) {
        this.pitchTableFetcher = pitchTableFetcher;
        this.fixturesTableFetcher = fixturesTableFetcher;
        this.futureTableFetcher = futureTableFetcher;
        this.oldGamesTableFetcher = oldGamesTableFetcher;
        this.statisticsTableFetcher = statisticsTableFetcher;
        this.matchTableFetcher = matchTableFetcher;
    }

    @GetMapping("/fetch")
    public String fetch(
            @RequestParam(value = "id", defaultValue = "old_games_table") String name,
            @RequestParam(value = "year", required = false) String year,
            @RequestParam(value = "season", required = false) String season,
            @RequestParam(value = "league", required = false) String league,
            @RequestParam(value = "group", required = false) String group,
            @RequestParam(value = "team", required = false) String teamName
    ) {
        switch (name) {
            case "pitch-table":
                return pitchTableFetcher.getData(null);
            case "fixtures-table":
                return fixturesTableFetcher.getData(createLeagueQueryModel(year, season, league, group));
            case "future-games-table":
                return futureTableFetcher.getData(createLeagueQueryModel(year, season, league, group, teamName));
            case "old-games-table":
                return oldGamesTableFetcher.getData(createLeagueQueryModel(year, season, league, group, teamName));
            case "statistics-table":
                return statisticsTableFetcher.getData(createLeagueQueryModel(year, season, league, group, teamName));
            case "match-details-table":
                return matchTableFetcher.getData(createLeagueQueryModel(year, season, league, group, teamName));
        }
        throw new RuntimeException("Invalid table name entered.");
    }

    private LeagueQueryModel createLeagueQueryModel(String year, String season, String leagueNumber, String group) {
        return this.createLeagueQueryModel(year, season, leagueNumber, group, null);
    }

    private LeagueQueryModel createLeagueQueryModel(String year, String season, String leagueNumber, String group, String team) {
        return LeagueQueryModel.builder().year(year).season(season).league(leagueNumber).group(group).team(team).build();
    }
}
