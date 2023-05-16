package com.branik.updater.database.converter;

import com.branik.updater.core.model.rest.StatsRestModel;
import com.branik.updater.database.model.db.PlayerEntity;
import com.branik.updater.database.model.db.TeamEntity;
import org.springframework.stereotype.Service;

@Service
public class RestToPlayerConverter {

    public PlayerEntity convert(StatsRestModel statsModel, TeamEntity teamEntity) {
        PlayerEntity.PlayerEntityBuilder playerEntityBuilder = PlayerEntity.builder();
        return playerEntityBuilder.name(statsModel.getName()).teamEntity(teamEntity).build();
    }
}
