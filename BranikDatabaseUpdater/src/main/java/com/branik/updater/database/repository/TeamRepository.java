package com.branik.updater.database.repository;

import com.branik.updater.database.model.db.LeagueEntity;
import com.branik.updater.database.model.db.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {
    TeamEntity findByName(String name);
    TeamEntity save(TeamEntity teamEntity);
    List<TeamEntity> findByLeagueEntitiesContaining(LeagueEntity league);
}
