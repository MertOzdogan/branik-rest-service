package com.branik.updater.database.services.table;

import com.branik.updater.core.model.rest.StandingsRestModel;
import com.branik.updater.core.util.URLUtil;
import com.branik.updater.database.constants.ConfigKeys;
import com.branik.updater.database.itemreader.WebReader;
import com.branik.updater.database.model.db.LeagueEntity;
import com.branik.updater.database.model.db.StandingEntity;
import com.branik.updater.database.repository.StandingsRepository;
import com.branik.updater.database.services.repository.LeagueRepositoryService;
import com.branik.updater.database.services.repository.TeamRepositoryService;
import com.typesafe.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.branik.updater.database.constants.ConfigKeys.*;
import static com.branik.updater.database.constants.ConfigKeys.LEAGUE_NUMBER;

@Service
public class StandingTableService {
    private final WebReader<List<StandingsRestModel>> standingsTableRestReader;
    private final StandingsRepository standingsRepository;
    private final LeagueEntity currentLeague;
    private TeamRepositoryService teamRepositoryService;
    private final String restUrl;
    private final String leagueYear;
    private final String leagueSeason;
    private final String leagueGroup;
    private final String leagueNumber;

    @Autowired
    public StandingTableService(Config config,
                                WebReader<List<StandingsRestModel>> standingsTableRestReader,
                                StandingsRepository standingsRepository,
                                TeamRepositoryService teamRepositoryService,
                                LeagueRepositoryService leagueRepositoryService) {
        this.leagueYear = config.getString(LEAGUE_YEAR);
        this.leagueSeason = config.getString(LEAGUE_SEASON);
        this.leagueGroup = config.getString(LEAGUE_GROUP);
        this.leagueNumber = config.getString(LEAGUE_NUMBER);

        this.standingsTableRestReader = standingsTableRestReader;
        this.restUrl = this.createRestURL(config);
        this.standingsRepository = standingsRepository;
        this.teamRepositoryService = teamRepositoryService;
        this.currentLeague = leagueRepositoryService.getLeague(leagueYear, leagueNumber, leagueGroup, leagueSeason);
    }

    @Transactional
    public void update() {
        List<StandingsRestModel> standingTableTeamList = standingsTableRestReader.read(restUrl);
        for (StandingsRestModel standingsRestModel : standingTableTeamList) {
            StandingEntity standingEntity = standingsRepository.findByTeamNameAndLeagueLeagueYearAndLeagueLeagueSeasonAndLeagueLeagueNumberAndLeagueLeagueGroup(
                    standingsRestModel.getTeam(),
                    leagueYear,
                    leagueSeason,
                    leagueNumber,
                    leagueGroup);
            if (standingEntity == null) {
                standingEntity = StandingEntity.builder()
                        .team(teamRepositoryService.getTeamByName(standingsRestModel.getTeam()))
                        .league(currentLeague)
                        .build();
            }

            standingEntity.setWins(Integer.parseInt(standingsRestModel.getWinCount()));
            standingEntity.setLosses(Integer.parseInt(standingsRestModel.getLossCount()));
            standingEntity.setDraws(Integer.parseInt(standingsRestModel.getDrawCount()));
            String[] killDeathRatio = standingsRestModel.getKillDeathRatio().split(":");
            standingEntity.setGoalsScored(Integer.parseInt(killDeathRatio[0]));
            standingEntity.setGoalsConceded(Integer.parseInt(killDeathRatio[1]));
            standingEntity.setTeam_rank(Integer.parseInt(standingsRestModel.getRank().replace(".", "")));
            standingEntity.setPoints(Integer.parseInt(standingsRestModel.getPoints()));
            standingsRepository.save(standingEntity);
        }
    }

    private String createRestURL(Config config) {
        String urlTemplate = config.getString("rest.standing-table.url");
        return urlTemplate.replace(config.getString(ConfigKeys.YEAR_PLACE_HOLDER_KEY), config.getString(LEAGUE_YEAR))
                .replace(config.getString(ConfigKeys.LEAGUE_PLACE_HOLDER_KEY), config.getString(LEAGUE_NUMBER))
                .replace(config.getString(ConfigKeys.DIVISION_PLACE_HOLDER_KEY), config.getString(LEAGUE_GROUP))
                .replace(config.getString(ConfigKeys.SEASON_PLACE_HOLDER_KEY), config.getString(LEAGUE_SEASON))
                .replace(config.getString(TEAM_PLACE_HOLDER_KEY), URLUtil.getTeamNameInUrlVersion(config.getString("team.name")));
    }
}
