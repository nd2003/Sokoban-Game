package util.repository;

import lombok.NonNull;

import lombok.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;


public abstract class ResourcesRepository<T> extends JacksonJsonRepository {

    private final Set<T> elements;

    protected ResourcesRepository(
            @NonNull final Class<T> elementClass,
            @NonNull final String resourceName) {
        try {
            elements = Collections.unmodifiableSet(
                    readSet(elementClass.getResourceAsStream(resourceName), elementClass)
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Failed to load resource " + resourceName, e); // Can't happen
        }
    }

    public Set<T> getAll() {
        return elements;
    }
}
