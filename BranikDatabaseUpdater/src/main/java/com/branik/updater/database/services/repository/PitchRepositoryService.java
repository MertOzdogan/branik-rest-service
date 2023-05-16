package com.branik.updater.database.services.repository;

import com.branik.updater.database.model.db.PitchEntity;
import com.branik.updater.database.repository.PitchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PitchRepositoryService {
    private PitchRepository pitchRepository;

    @Autowired
    public PitchRepositoryService(PitchRepository pitchRepository) {
        this.pitchRepository = pitchRepository;
    }

    public PitchEntity getPitch(String abbr) {
        return pitchRepository.getByPitchAbbr(abbr);
    }
}
