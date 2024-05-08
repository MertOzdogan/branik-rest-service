package com.branik.updater.database.services.table;

import com.branik.updater.core.model.rest.PitchRestModel;
import com.branik.updater.database.converter.RestToPitchConverter;
import com.branik.updater.database.itemreader.WebReader;
import com.branik.updater.database.model.db.PitchEntity;
import com.branik.updater.database.services.repository.PitchRepositoryService;
import com.branik.updater.database.services.repository.PlayerRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PitchTableService {
    private PitchRepositoryService pitchRepositoryService;
    private WebReader<List<PitchRestModel>> pitchReader;
    private RestToPitchConverter restToPitchConverter;

    @Autowired
    public PitchTableService(PitchRepositoryService pitchRepositoryService, WebReader<List<PitchRestModel>> pitchReader, PlayerRepositoryService playerRepositoryService, RestToPitchConverter restToPitchConverter) {
        this.pitchRepositoryService = pitchRepositoryService;
        this.pitchReader = pitchReader;
        this.restToPitchConverter = restToPitchConverter;
    }

    public void initialize() {
        List<PitchRestModel> pitchRestModelList = pitchReader.read();
        List<PitchEntity> pitchEntities = pitchRestModelList.stream().map(restToPitchConverter::convert).collect(Collectors.toList());

        for (PitchEntity pitchEntity : pitchEntities) {
            PitchEntity pitch = pitchRepositoryService.getPitch(pitchEntity.getPitch_abbr());
            if (pitch == null) {
                pitchRepositoryService.save(pitchEntity);
                log.info("Pitch added: {}", pitchEntity.getPitch_abbr());
            } else {
                log.info("Pitch is already there: {}", pitchEntity.getPitch_abbr());
            }
        }
    }
}
