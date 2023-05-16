package com.branik.updater.database.services.repository;

import com.branik.updater.database.model.db.LeagueEntity;
import com.branik.updater.database.model.db.StandingEntity;
import com.branik.updater.database.repository.StandingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandingsRepositoryService {
    private final StandingsRepository standingsRepository;

    @Autowired
    public StandingsRepositoryService(StandingsRepository standingsRepository) {
        this.standingsRepository = standingsRepository;
    }

    public List<StandingEntity> getByLeagueEntity(LeagueEntity leagueEntity) {
        return standingsRepository.findStandingEntitiesByLeagueId(leagueEntity.getId());
    }
}
