package com.branik.updater.database.converter;

import com.branik.updater.core.model.rest.GoalRestModel;
import com.branik.updater.database.model.db.GoalEntity;
import com.branik.updater.database.model.db.PlayerEntity;
import com.branik.updater.database.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestToGoalConverter {
    private final PlayerRepository playerRepository;

    @Autowired
    public RestToGoalConverter(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public GoalEntity convert(GoalRestModel goalModel) {
        GoalEntity goalEntity = new GoalEntity();
        PlayerEntity player = getPlayer(goalModel.getName());
        String minute = goalModel.getMinute();
        goalEntity.setPlayerEntity(player);
        goalEntity.setMinute(minute);
        return goalEntity;
    }

    private PlayerEntity getPlayer(String name) {
        if (name != null || !name.isEmpty()) {
            ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
            String[] nameSurname = name.split(" ");
            Example<PlayerEntity> example = Example.of(PlayerEntity.builder().name(nameSurname[0]).build(), exampleMatcher);
            List<PlayerEntity> all = playerRepository.findAll(example);
            for (PlayerEntity playerEntity : all) {
                String name1 = playerEntity.getName();
                if (name1.contains(nameSurname[0]) && name1.contains(nameSurname[1])) {
                    return playerEntity;
                }
            }
        }
        return null;
    }
}
