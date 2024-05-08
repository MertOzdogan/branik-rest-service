package com.branik.updater.database.services.table;

import com.branik.updater.core.model.rest.StatsRestModel;
import com.branik.updater.core.util.URLUtil;
import com.branik.updater.database.constants.ConfigKeys;
import com.branik.updater.database.itemreader.WebReader;
import com.branik.updater.database.model.db.LeagueEntity;
import com.branik.updater.database.model.db.PlayerEntity;
import com.branik.updater.database.model.db.StandingEntity;
import com.branik.updater.database.model.db.TeamEntity;
import com.branik.updater.database.services.repository.PlayerRepositoryService;
import com.branik.updater.database.services.repository.StandingsRepositoryService;
import com.branik.updater.database.services.repository.TeamRepositoryService;
import com.branik.updater.database.util.CurrentLeagueUtil;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.branik.updater.database.constants.ConfigKeys.*;

@Service
@Slf4j
public class PlayerTableService {

    private final StandingsRepositoryService standingsRepositoryService;
    private final LeagueEntity currentLeague;
    private Config config;
    private final WebReader<List<StatsRestModel>> statsTableRestReader;
    private final PlayerRepositoryService playerRepositoryService;

    @Autowired
    public PlayerTableService(WebReader<List<StatsRestModel>> statsTableRestReader,
                              PlayerRepositoryService playerRepositoryService,
                              StandingsRepositoryService standingsRepositoryService,
                              CurrentLeagueUtil currentLeagueUtil,
                              Config config) {
        this.statsTableRestReader = statsTableRestReader;
        this.playerRepositoryService = playerRepositoryService;
        this.standingsRepositoryService = standingsRepositoryService;
        this.currentLeague = currentLeagueUtil.getCurrentLeague();
        this.config = config;
    }

    public void initialize() {
        List<StandingEntity> standingEntityList = standingsRepositoryService.getByLeagueEntity(currentLeague);

        for (StandingEntity standingEntity : standingEntityList) {
            TeamEntity team = standingEntity.getTeam();
            List<StatsRestModel> statsRestModelList = statsTableRestReader.read(createRestURL(team.getName()));
            for (StatsRestModel statsRestModel : statsRestModelList) {
                String fullName = statsRestModel.getName();
                PlayerEntity playerByNameSurname = playerRepositoryService.getPlayerByNameSurname(fullName, team);
                if (playerByNameSurname == null) {
                    PlayerEntity newPlayerToSave = PlayerEntity.builder().name(fullName).teamEntity(team).build();
                    playerRepositoryService.save(newPlayerToSave);
                    log.info("Player saved: ID: {} Name: {} Team: {}", newPlayerToSave.getId(), newPlayerToSave.getName(), team.getName());
                }
            }
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
}
