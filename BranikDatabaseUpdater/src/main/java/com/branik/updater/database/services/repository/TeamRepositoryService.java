package com.branik.updater.database.services.repository;

import com.branik.updater.database.model.db.LeagueEntity;
import com.branik.updater.database.model.db.TeamEntity;
import com.branik.updater.database.repository.TeamRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TeamRepositoryService {
    private final TeamRepository teamRepository;

    public TeamRepositoryService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }


    public TeamEntity getTeamByName(String name) {
        TeamEntity byName = teamRepository.findByName(name);
        if (byName != null) {
            Hibernate.initialize(byName.getLeagueEntities());
        }
        return byName;

    }

    public List<TeamEntity> getTeamsByLeague(LeagueEntity leagueEntity) {
        return this.teamRepository.findByLeagueEntitiesContaining(leagueEntity);
    }

    public void save(TeamEntity teamEntity) {
        teamRepository.saveAndFlush(teamEntity);
    }
}
