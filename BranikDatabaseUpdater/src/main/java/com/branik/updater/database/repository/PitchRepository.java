package com.branik.updater.database.repository;

import com.branik.updater.database.model.db.PitchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PitchRepository extends JpaRepository<PitchEntity, Integer> {
    @Query("SELECT e FROM pitch e WHERE e.pitch_abbr LIKE %?1%")
    PitchEntity getByPitchAbbrLike(String abbr);

    @Query("SELECT e FROM pitch e WHERE e.pitch_abbr = ?1")
    PitchEntity getByPitch_abbr(String abbr);
}
