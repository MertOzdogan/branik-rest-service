package com.branik.updater.database.services.table;

import com.branik.updater.core.model.rest.*;
import com.branik.updater.core.model.rest.match.DetailedMatchRestModel;
import com.branik.updater.core.model.rest.match.FutureMatchRestModel;
import com.branik.updater.core.model.rest.match.OldMatchRestModel;
import com.branik.updater.core.util.URLUtil;
import com.branik.updater.database.constants.ConfigKeys;
import com.branik.updater.database.converter.RestToGoalConverter;
import com.branik.updater.database.itemreader.WebReader;
import com.branik.updater.database.model.db.*;
import com.branik.updater.database.repository.PlayerRepository;
import com.branik.updater.database.repository.StandingsRepository;
import com.branik.updater.database.services.repository.LeagueRepositoryService;
import com.branik.updater.database.services.repository.MatchRepositoryService;
import com.branik.updater.database.services.repository.PitchRepositoryService;
import com.branik.updater.database.services.repository.TeamRepositoryService;
import com.typesafe.config.Config;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.branik.updater.database.constants.ConfigKeys.*;
import static com.branik.updater.database.constants.ConfigKeys.LEAGUE_NUMBER;

@Service
public class MatchDetailsTableService {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.M.yy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("d.M.yy");


    private final WebReader<List<DetailedMatchRestModel>> detailedMatchDetailsTableReader;
    private final WebReader<List<FutureMatchRestModel>> futureMatchReader;
    private final WebReader<List<StandingsRestModel>> standingsRestReader;
    private final WebReader<List<OldMatchRestModel>> oldMatchRestReader;

    private final TeamRepositoryService teamRepositoryService;
    private final LeagueRepositoryService leagueRepositoryService;
    private final PitchRepositoryService pitchRepositoryService;
    private final RestToGoalConverter restToGoalConverter;

    private final MatchRepositoryService matchRepositoryService;
    private final StandingsRepository standingsRepository;
    private final PlayerRepository playerRepository;

    private final Config config;
    private final String leagueYear;
    private final String leagueSeason;
    private final String leagueGroup;
    private final String leagueNumber;

    public MatchDetailsTableService(Config config,
                                    WebReader<List<DetailedMatchRestModel>> detailedMatchDetailsTableReader,
                                    WebReader<List<FutureMatchRestModel>> futureMatchReader,
                                    WebReader<List<StandingsRestModel>> standingsRestReader,
                                    WebReader<List<OldMatchRestModel>> oldMatchRestReader,
                                    StandingsRepository standingsRepository,
                                    MatchRepositoryService matchRepositoryService,
                                    TeamRepositoryService teamRepositoryService,
                                    LeagueRepositoryService leagueRepositoryService,
                                    PitchRepositoryService pitchRepositoryService,
                                    RestToGoalConverter restToGoalConverter,
                                    PlayerRepository playerRepository) {
        this.detailedMatchDetailsTableReader = detailedMatchDetailsTableReader;
        this.leagueYear = config.getString(LEAGUE_YEAR);
        this.leagueSeason = config.getString(LEAGUE_SEASON);
        this.leagueGroup = config.getString(LEAGUE_GROUP);
        this.leagueNumber = config.getString(LEAGUE_NUMBER);

        this.config = config;

        this.standingsRestReader = standingsRestReader;
        this.futureMatchReader = futureMatchReader;
        this.oldMatchRestReader = oldMatchRestReader;
        this.restToGoalConverter = restToGoalConverter;

        this.standingsRepository = standingsRepository;
        this.playerRepository = playerRepository;
        this.matchRepositoryService = matchRepositoryService;
        this.teamRepositoryService = teamRepositoryService;
        this.leagueRepositoryService = leagueRepositoryService;
        this.pitchRepositoryService = pitchRepositoryService;
    }

    @Transactional
    public void initialize() {
        List<StandingsRestModel> standingsRestModelList = standingsRestReader.read(createRestURL(config.getString("rest.standing-table.url"), null));
        LeagueEntity currentLeagueEntity = leagueRepositoryService.getLeague(leagueYear, leagueSeason, leagueNumber, leagueGroup);

        for (StandingsRestModel standingsRestModel : standingsRestModelList) {
            String teamName = standingsRestModel.getTeam();
            initializeByPastGames(currentLeagueEntity, teamName);
            initializeByFutureGames(currentLeagueEntity, teamName);
        }
    }

    private void initializeByPastGames(LeagueEntity currentLeagueEntity, String teamName) {
        List<OldMatchRestModel> oldMatchRestModelList = this.oldMatchRestReader.read(createOldGamesTableRestURL(teamName));
        for (OldMatchRestModel oldMatchRestModel : oldMatchRestModelList) {
            String homeTeamName = oldMatchRestModel.getHomeTeam();
            String awayTeamName = oldMatchRestModel.getAwayTeam();
            LocalDateTime matchTime = null;
            try {
                LocalDate localDate = dateFormat2.parse(oldMatchRestModel.getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime localTime = timeFormat.parse(oldMatchRestModel.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                matchTime = LocalDateTime.of(localDate, localTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            MatchEntity match = matchRepositoryService.getMatch(matchTime, homeTeamName, awayTeamName);

            if (match == null) {
                MatchEntity matchEntity = new MatchEntity();
                TeamEntity homeTeam = teamRepositoryService.getTeamByName(homeTeamName);
                TeamEntity awayTeam = teamRepositoryService.getTeamByName(awayTeamName);
                PitchEntity pitch = pitchRepositoryService.getPitch(oldMatchRestModel.getPitch());

                matchEntity.setHomeTeam(homeTeam);
                matchEntity.setAwayTeam(awayTeam);
                matchEntity.setPitchEntity(pitch);
                matchEntity.setLeagueEntity(currentLeagueEntity);
                matchEntity.setRound(Integer.parseInt(oldMatchRestModel.getKolo()));
                matchEntity.setTime(matchTime);
                matchRepositoryService.save(matchEntity);
            }
        }

    }

    private void initializeByFutureGames(LeagueEntity currentLeagueEntity, String teamName) {
        List<FutureMatchRestModel> futureGamesOfGivenTeam = this.futureMatchReader.read(createFutureTableRestURL(teamName));
        for (FutureMatchRestModel futureMatchTeamRestModel : futureGamesOfGivenTeam) {
            FutureMatchRestModel.Team[] teams = futureMatchTeamRestModel.getTeams();
            LocalDateTime matchtime = null;
            try {
                LocalDate localDate = dateFormat.parse(futureMatchTeamRestModel.getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime localTime = timeFormat.parse(futureMatchTeamRestModel.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                matchtime = LocalDateTime.of(localDate, localTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            MatchEntity match = matchRepositoryService.getMatch(matchtime, teams[0].getName(), teams[1].getName());
            if (match == null) {
                MatchEntity matchEntity = new MatchEntity();
                TeamEntity homeTeam = teamRepositoryService.getTeamByName(teams[0].getName());
                TeamEntity awayTeam = teamRepositoryService.getTeamByName(teams[1].getName());
                PitchEntity pitch = pitchRepositoryService.getPitch(futureMatchTeamRestModel.getPitch());

                matchEntity.setHomeTeam(homeTeam);
                matchEntity.setAwayTeam(awayTeam);
                matchEntity.setPitchEntity(pitch);
                matchEntity.setLeagueEntity(currentLeagueEntity);
                matchEntity.setRound(futureMatchTeamRestModel.getKolo());
                matchEntity.setTime(matchtime);
                matchRepositoryService.save(matchEntity);
            }
        }
    }

    @Transactional
    public void update() {
        List<StandingEntity> standingTeams = this.standingsRepository.findByLeagueLeagueYearAndLeagueLeagueSeasonAndLeagueLeagueNumberAndLeagueLeagueGroup(leagueYear, leagueSeason, leagueNumber, leagueGroup);
        List<String> teamNames = standingTeams.stream().map(o -> o.getTeam().getName()).collect(Collectors.toList());
        for (String teamName : teamNames) {
            List<DetailedMatchRestModel> detailedMatchesOfTeam = detailedMatchDetailsTableReader.read(createMatchDetailsRestURL(teamName));
            for (DetailedMatchRestModel detailedMatchOfTeam : detailedMatchesOfTeam) {
                Map<String, TeamRestModel> teams = detailedMatchOfTeam.getTeams();
                TeamRestModel homeTeam = teams.get("home");
                TeamRestModel awayTeam = teams.get("away");
                List<GoalRestModel> homeTeamGoalList = homeTeam.getGoalRestModels();
                List<GoalRestModel> awayTeamGoalList = awayTeam.getGoalRestModels();

                MatchEntity matchEntity = matchRepositoryService.getAllMatchOfTeamsByLeague(homeTeam.getName(), awayTeam.getName(), leagueYear, leagueSeason, leagueNumber, leagueGroup);
                //TODO :FIX this mess
                if (matchEntity == null) {
                    matchEntity = matchRepositoryService.getAllMatchOfTeamsByLeague(awayTeam.getName(), homeTeam.getName(), leagueYear, leagueSeason, leagueNumber, leagueGroup);
                }
                //TODO: End

                String homeTeamMvp = homeTeam.getMvp();
                if (homeTeamMvp != null) {
                    PlayerEntity homeMvpPlayer = playerRepository.findPlayerEntityByName(nameConverter(homeTeamMvp));
                    matchEntity.setHomeTeamMVP(homeMvpPlayer);
                }
                String homeTeamCaptain = homeTeam.getCaptain();
                if (homeTeamCaptain != null) {
                    PlayerEntity homeCaptainPlayer = playerRepository.findPlayerEntityByName(nameConverter(homeTeamCaptain));
                    matchEntity.setHomeTeamCaptain(homeCaptainPlayer);
                }

                String awayTeamMvp = awayTeam.getMvp();
                if (awayTeamMvp != null) {
                    PlayerEntity awayMvpPlayer = playerRepository.findPlayerEntityByName(nameConverter(awayTeamMvp));
                    matchEntity.setAwayTeamMVP(awayMvpPlayer);
                }
                String awayTeamCaptain = awayTeam.getCaptain();
                if (awayTeamCaptain != null) {
                    PlayerEntity awayCaptainPlayer = playerRepository.findPlayerEntityByName(nameConverter(awayTeamCaptain));
                    matchEntity.setAwayTeamCaptain(awayCaptainPlayer);
                }
                List<GoalEntity> goalEntityList = new ArrayList<>();

                if (homeTeamGoalList != null) {
                    goalEntityList.addAll(homeTeamGoalList.stream().map(restToGoalConverter::convert).collect(Collectors.toList()));
                }
                if (awayTeamGoalList != null) {
                    goalEntityList.addAll(awayTeamGoalList.stream().map(restToGoalConverter::convert).collect(Collectors.toList()));
                }


                if (matchEntity.getGoals() == null || matchEntity.getGoals().isEmpty()) {
                    matchEntity.setGoals(goalEntityList);
                }
                matchEntity.setHomeTeamScore(homeTeamGoalList != null ? homeTeamGoalList.size() : 0);
                matchEntity.setAwayTeamScore(awayTeamGoalList != null ? awayTeamGoalList.size() : 0);
                matchEntity.setComment(detailedMatchOfTeam.getRefComment());
                matchRepositoryService.save(matchEntity);
            }
        }
    }

    private String nameConverter(String nameSurname) {
        String[] nameSurnameArray = nameSurname.split(" ");
        int nameSurnameLength = nameSurnameArray.length;
        String[] surnameNameArray = new String[nameSurnameLength];
        surnameNameArray[0] = nameSurnameArray[nameSurnameLength - 1]; // Surname to beginning
        for (int i = 0; i <= nameSurnameArray.length - 2; i++) {
            surnameNameArray[i + 1] = nameSurnameArray[i];
        }
        return Stream.of(surnameNameArray).collect(Collectors.joining(" "));
    }

    private String createRestURL(String tableName, String teamName) {
        return tableName.replace(config.getString(ConfigKeys.YEAR_PLACE_HOLDER_KEY), leagueYear)
                .replace(config.getString(ConfigKeys.LEAGUE_PLACE_HOLDER_KEY), leagueNumber)
                .replace(config.getString(ConfigKeys.DIVISION_PLACE_HOLDER_KEY), leagueGroup)
                .replace(config.getString(ConfigKeys.SEASON_PLACE_HOLDER_KEY), leagueSeason)
                .replace(config.getString(TEAM_PLACE_HOLDER_KEY), URLUtil.getTeamNameInUrlVersion(teamName));
    }

    private String createMatchDetailsRestURL(String teamName) {
        return createRestURL(config.getString("rest.match-details-table.url"), teamName);
    }


    private String createFutureTableRestURL(String teamName) {
        return createRestURL(config.getString("rest.future-games-table.url"), teamName);
    }

    private String createOldGamesTableRestURL(String teamName) {
        return createRestURL(config.getString("rest.old-games-table.url"), teamName);
    }
}
