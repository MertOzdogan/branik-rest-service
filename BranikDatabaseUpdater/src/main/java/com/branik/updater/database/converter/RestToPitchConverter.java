package com.branik.updater.database.converter;

import com.branik.updater.core.model.rest.PitchRestModel;
import com.branik.updater.database.model.db.PitchEntity;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RestToPitchConverter {
    private final Pattern addressPattern = Pattern.compile("^(.*?)Praha [0-9]");


    public PitchEntity convert(PitchRestModel pitchModel) {
        PitchEntity.PitchEntityBuilder pitchEntityBuilder = PitchEntity.builder();
        return pitchEntityBuilder.pitch_abbr(pitchModel.getAbbvr())
                .pitch_address(getAddress(pitchModel.getDetails()))
                .pitch_desc(pitchModel.getDetails()).build();
    }

    private String getAddress(String fullDetails) {
        Matcher matcher = addressPattern.matcher(fullDetails);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
