package com.branik.updater.database.repository;


import com.branik.updater.database.model.db.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<GoalEntity, Integer> {
    List<GoalEntity> findByMatchId(Long matchId);
}
