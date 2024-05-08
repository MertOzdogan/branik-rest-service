package com.branik.updater.database.services.repository;

import com.branik.updater.database.model.db.PlayerEntity;
import com.branik.updater.database.model.db.TeamEntity;
import com.branik.updater.database.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerRepositoryService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerRepositoryService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public PlayerEntity getPlayerByNameSurname(String name) {
        return playerRepository.findPlayerEntityByName(name);
    }

    public PlayerEntity getPlayerByNameSurname(String name, TeamEntity team) {
        return playerRepository.findPlayerEntityByNameAndTeamEntity(name, team);
    }

    public void save(PlayerEntity playerEntity) {
        playerRepository.save(playerEntity);
    }
}
