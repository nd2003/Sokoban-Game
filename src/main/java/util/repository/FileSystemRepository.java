package util.repository;

import lombok.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class FileSystemRepository<T> extends JacksonJsonRepository {

    private Set<T> elements;
    private final Class<T> elementsClass;

    protected FileSystemRepository(
            @NonNull final Class<T> elementClass) {

        this.elementsClass = elementClass;
        this.elements = new HashSet<>();
    }

    public Set<T> getAll() {
        return new HashSet<>(elements);
    }

    public Set<T> addOne(
            @NonNull final T element) {

        elements.add(element);
        return getAll();
    }

    public Set<T> addMany(
            @NonNull final Collection<T> elements) {

        elements.forEach(this::addOne);
        return getAll();
    }

    public Set<T> replaceOne(
            @NonNull final T element) {

        elements.remove(element);
        elements.add(element);
        return getAll();
    }

    public Set<T> replaceMany(
            @NonNull final Collection<T> elements) {

        this.elements.removeAll(elements);
        this.elements.addAll(elements);
        return getAll();
    }

    public Set<T> clear() {
        this.elements.clear();
        return getAll();
    }

    public Set<T> replaceAll(
            @NonNull final Collection<T> elements) {

        clear();
        this.elements.addAll(elements);
        return getAll();
    }

    public Set<T> loadFromFile(
            @NonNull final File file) throws IOException {

        elements = readSet(new FileInputStream(file), this.elementsClass);
        return elements;
    }

    public void saveToFile(
            @NonNull final File file) throws IOException {

        writeSet(new FileOutputStream(file), elements);
    }
}
