package util.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * Utility class for reading and writing sets of objects to and from JSON using Jackson ObjectMapper.
 */
public class JacksonJsonRepository {

    /**
     * ObjectMapper instance for JSON serialization and deserialization,
     * configured with JavaTimeModule.
     */
    protected static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Deserializes a set of objects from JSON.
     *
     * @param in the input stream from which JSON data will be read
     * @param elementClass represents the class of the elements
     * @return the set of objects deserialized from JSON
     * @throws IOException if any I/O error occurs
     */
    protected static <T> Set<T> readSet(
            @NonNull final InputStream in,
            @NonNull final Class<T> elementClass) throws IOException {

        final var type = MAPPER.getTypeFactory().constructCollectionType(Set.class, elementClass);
        return MAPPER.readValue(in, type);
    }


    /**
     * Serializes a set of objects to JSON.
     * @param os the output stream to which JSON data will be written
     * @param value represents the class of the elements
     * @throws IOException if any I/O error occurs
     */
    protected static <T> void writeSet(
            @NonNull final OutputStream os,
            @NonNull final Set<T> value) throws IOException {

        MAPPER.writeValue(os, value);
    }
}
