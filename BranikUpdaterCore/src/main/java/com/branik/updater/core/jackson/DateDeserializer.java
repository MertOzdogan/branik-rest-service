package com.branik.updater.core.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class DateDeserializer extends StdDeserializer<LocalDate> {
    private static final long serialVersionUID = 1L;
    //    private SimpleDateFormat formatter = new SimpleDateFormat("dd. MM. yy");
    List<SimpleDateFormat> simpleDateFormatList = List.of(new SimpleDateFormat("dd. MM. yy"), new SimpleDateFormat("dd.M.yy"));

    public DateDeserializer() {
        this(null);
    }

    public DateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String date = node.textValue();
        for (SimpleDateFormat simpleDateFormat : simpleDateFormatList) {
            try {
                if (date.equals("")) {
                    return simpleDateFormat.parse("01. 01. 01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                return simpleDateFormat.parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); //.toInstant();
            } catch (ParseException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
            }
        }
        return null;
    }
}