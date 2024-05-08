package com.branik.updater.database.repository;

import com.branik.updater.database.model.db.PlayerEntity;
import com.branik.updater.database.model.db.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Integer> {
    PlayerEntity findByNameAndTeamEntityName(String name,String teamName);
    PlayerEntity findPlayerEntityByName(String name);
    PlayerEntity findPlayerEntityByNameAndTeamEntity(String name, TeamEntity team);
    PlayerEntity findPlayerEntityByNameAndTeamEntityName(String name, String teamName);
}
