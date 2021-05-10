package com.gotta_watch_them_all.app.work.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonParser {

    private final ObjectMapper mapper;

    public <T> List<T> toObjectList(final String raw, final Class<T> type) {
        try {
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return this.mapper.readValue(raw, this.mapper.getTypeFactory().constructCollectionType(ArrayList.class, type));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }

    public <T> T toObject(final String raw, final Class<T> type) {
        try {
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return this.mapper.readValue(raw, this.mapper.getTypeFactory().constructType(type));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> String toRaw(@NonNull final T objects) {
        try {
            return this.mapper.writeValueAsString(objects);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
