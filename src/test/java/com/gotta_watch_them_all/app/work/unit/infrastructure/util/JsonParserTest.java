package com.gotta_watch_them_all.app.work.unit.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.infrastructure.util.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonParserTest {

    ObjectMapper mapper;

    JsonParser jsonParser;

    @BeforeEach
    public void setUp() {
        mapper = Mockito.mock(ObjectMapper.class);
        jsonParser = new JsonParser(mapper);
    }

    @Test
    public void shouldThrowExceptionWhenParsingNullObject() {
        assertThrows(NullPointerException.class, () -> jsonParser.toRaw(null));
    }

    @Test
    public void shouldCallMapperWriteAsStringValueOnce() throws JsonProcessingException {
        jsonParser.toRaw(new Work());
        verify(mapper, times(1)).writeValueAsString(any());
    }

    @Test
    public void shouldReturnJsonRaw() throws JsonProcessingException {
        Work toParse = new Work().setId("1").setTitle("titre");
        when(mapper.writeValueAsString(toParse)).thenReturn("Parsing worked fine");
        String raw = jsonParser.toRaw(toParse);
        assertEquals("Parsing worked fine", raw);
    }

    @Test
    public void shouldReturnNullIfRawNull() {
        Work work = jsonParser.toObject(null, Work.class);
        assertNull(work);
    }

    @Test
    public void shouldReturnNullIfRawEmpty() {
        Work work = jsonParser.toObject("", Work.class);
        assertNull(work);
    }

    @Test
    public void shouldReturnNullIfRawWrongFormat() {
        Work work = jsonParser.toObject("bonjour à tous", Work.class);
        assertNull(work);
    }
}
