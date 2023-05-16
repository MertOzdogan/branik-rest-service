package com.branik.updater.database.services.table;

import com.branik.updater.core.model.rest.StandingsRestModel;
import com.branik.updater.core.model.rest.StatsRestModel;
import com.branik.updater.core.util.URLUtil;
import com.branik.updater.database.constants.ConfigKeys;
import com.branik.updater.database.itemreader.WebReader;
import com.branik.updater.database.model.db.LeagueEntity;
import com.branik.updater.database.model.db.PlayerEntity;
import com.branik.updater.database.model.db.TeamEntity;
import com.branik.updater.database.repository.LeagueRepository;
import com.branik.updater.database.services.repository.TeamRepositoryService;
import com.branik.updater.database.util.CurrentLeagueUtil;
import com.typesafe.config.Config;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.branik.updater.database.constants.ConfigKeys.*;
import static com.branik.updater.database.constants.ConfigKeys.LEAGUE_SEASON;

@Service
public class TeamsService {
    private final WebReader<List<StatsRestModel>> statsTableRestReader;
    private final WebReader<List<StandingsRestModel>> standingTableRestReader;
    private final LeagueEntity currentLeagueEntity;
    private final TeamRepositoryService teamRepositoryService;
    private final Config config;

    @Autowired
    public TeamsService(WebReader<List<StatsRestModel>> statsTableRestReader,
                        WebReader<List<StandingsRestModel>> standingTableRestReader,
                        TeamRepositoryService teamRepositoryService,
                        CurrentLeagueUtil currentLeagueUtil,
                        Config config) {
        this.statsTableRestReader = statsTableRestReader;
        this.standingTableRestReader = standingTableRestReader;
        this.teamRepositoryService = teamRepositoryService;
        this.config = config;

        this.currentLeagueEntity = currentLeagueUtil.getCurrentLeague();
    }

    @Transactional
    public void buildLeagueTeams() {
        List<String> standingTeams = standingTableRestReader.read(this.createStandingRestURL()).stream().map(StandingsRestModel::getTeam).collect(Collectors.toList());
        for (String standingTeam : standingTeams) {
            TeamEntity team = teamRepositoryService.getTeamByName(standingTeam);
            TeamEntity teamEntity;
            if (team == null) {
                teamEntity = new TeamEntity(standingTeam.trim());
            } else {
                teamEntity = new TeamEntity(team.getId(), team.getName(), team.getPlayers(), team.getMatch(), team.getLeagueEntities());
            }
            teamEntity.addLeague(this.currentLeagueEntity);
            teamRepositoryService.save(teamEntity);
        }
    }


    @Transactional
    public void update() {
        List<TeamEntity> teamsInCurrentLeagueList = teamRepositoryService.getTeamsByLeague(currentLeagueEntity);
        for (TeamEntity teamEntity : teamsInCurrentLeagueList) {
            List<String> playerNames = statsTableRestReader.read(createRestURL(teamEntity.getName())).stream().map(StatsRestModel::getName).collect(Collectors.toList());
            for (String playerName : playerNames) {
                PlayerEntity playerEntity = PlayerEntity.builder().teamEntity(teamEntity).name(playerName).build();
                teamEntity.setPlayers(playerEntity);
            }
            teamRepositoryService.save(teamEntity);
        }
    }

    private String createRestURL(String teamName) {
        String urlTemplate = config.getString("rest.stats-table.url");
        return urlTemplate.replace(config.getString(ConfigKeys.YEAR_PLACE_HOLDER_KEY), config.getString(LEAGUE_YEAR))
                .replace(config.getString(ConfigKeys.LEAGUE_PLACE_HOLDER_KEY), config.getString(LEAGUE_NUMBER))
                .replace(config.getString(ConfigKeys.DIVISION_PLACE_HOLDER_KEY), config.getString(LEAGUE_GROUP))
                .replace(config.getString(ConfigKeys.SEASON_PLACE_HOLDER_KEY), config.getString(LEAGUE_SEASON))
                .replace(config.getString(TEAM_PLACE_HOLDER_KEY), URLUtil.getTeamNameInUrlVersion(teamName));
    }

    private String createStandingRestURL() {
        String urlTemplate = config.getString("rest.standing-table.url");
        return urlTemplate.replace(config.getString(ConfigKeys.YEAR_PLACE_HOLDER_KEY), config.getString(LEAGUE_YEAR))
                .replace(config.getString(ConfigKeys.LEAGUE_PLACE_HOLDER_KEY), config.getString(LEAGUE_NUMBER))
                .replace(config.getString(ConfigKeys.DIVISION_PLACE_HOLDER_KEY), config.getString(LEAGUE_GROUP))
                .replace(config.getString(ConfigKeys.SEASON_PLACE_HOLDER_KEY), config.getString(LEAGUE_SEASON));
    }
}
