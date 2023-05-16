package com.branik.updater.core.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;

public class TimeDeserializer extends StdDeserializer<LocalTime> {
    private static final long serialVersionUID = 1L;

    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

    public TimeDeserializer() {
        this(null);
    }

    public TimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        final String date = node.textValue();

        try {
            if (date.equals("")) {
                return formatter.parse("00:00").toInstant().atZone(ZoneId.systemDefault()).toLocalTime(); //.toInstant();
            }
            return formatter.parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalTime(); //.toInstant();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}