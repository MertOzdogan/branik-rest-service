package com.branik.updater.database.repository;

import com.branik.updater.database.model.db.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Integer> {
    PlayerEntity findByNameAndTeamEntityName(String name,String teamName);
    PlayerEntity findPlayerEntityByName(String name);
}
