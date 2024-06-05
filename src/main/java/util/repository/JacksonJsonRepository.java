package util.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class JacksonJsonRepository {
    protected static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());


    protected static <T> Set<T> readSet(
            @NonNull final InputStream in,
            @NonNull final Class<T> elementClass) throws IOException {

        final var type = MAPPER.getTypeFactory().constructCollectionType(Set.class, elementClass);
        return MAPPER.readValue(in, type);
    }

    protected static <T> void writeSet(
            @NonNull final OutputStream os,
            @NonNull final Set<T> value) throws IOException {

        MAPPER.writeValue(os, value);
    }
}
