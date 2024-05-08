package com.branik.updater.database.services.repository;

import com.branik.updater.database.model.db.PitchEntity;
import com.branik.updater.database.repository.PitchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PitchRepositoryService {
    private final PitchRepository pitchRepository;

    @Autowired
    public PitchRepositoryService(PitchRepository pitchRepository) {
        this.pitchRepository = pitchRepository;
    }

    public PitchEntity getPitchLike(String abbr) {
        return pitchRepository.getByPitchAbbrLike(abbr);
    }

    public PitchEntity getPitch(String abbr) {
        return pitchRepository.getByPitch_abbr(abbr);
    }

    public void save(PitchEntity pitchEntity) {
        pitchRepository.save(pitchEntity);
    }
}
