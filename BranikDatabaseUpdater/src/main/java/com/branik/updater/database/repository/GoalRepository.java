package com.branik.updater.database.repository;


import com.branik.updater.database.model.db.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<GoalEntity, Integer> {
}
