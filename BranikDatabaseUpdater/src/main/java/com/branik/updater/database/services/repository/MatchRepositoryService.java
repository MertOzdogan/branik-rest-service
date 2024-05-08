package com.branik.updater.database.services.repository;

import com.branik.updater.database.model.db.MatchEntity;
import com.branik.updater.database.repository.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MatchRepositoryService {
    private final MatchRepository matchRepository;

    @Autowired
    public MatchRepositoryService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<MatchEntity> getAllMatchesOfTeamByLeague(String teamName, String leagueYear, String leagueSeason, String leagueNumber, String leagueGroup) {
        return matchRepository.
                getMatchEntitiesByHomeTeamNameOrAwayTeamNameAndLeagueEntityLeagueYearAndLeagueEntityLeagueSeasonAndLeagueEntityLeagueNumberAndLeagueEntityLeagueGroup(teamName, teamName, leagueYear, leagueSeason, leagueNumber, leagueGroup);
    }

    public MatchEntity getAllMatchOfTeamsByLeague(String homeTeamName, String awayTeamName, String leagueYear, String leagueSeason, String leagueNumber, String leagueGroup) {
        return matchRepository.
                getMatchEntity(homeTeamName, awayTeamName, leagueYear, leagueSeason, leagueNumber, leagueGroup);
    }

    public MatchEntity getMatch(LocalDateTime time, String homeTeam, String awayTeam) {
        return matchRepository.getMatch(time, homeTeam, awayTeam);
    }

    public void save(MatchEntity matchEntity) {
        matchRepository.save(matchEntity);
    }
}
