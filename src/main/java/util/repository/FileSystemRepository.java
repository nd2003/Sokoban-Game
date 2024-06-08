package util.repository;

import lombok.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a repository to obtain a set of objects. Data is read from a
 * JSON file that contains an array of JSON objects. The implementation uses a hashcode-based set.
 *
 * @param <T> the type of the elements
 */
public abstract class FileSystemRepository<T> extends JacksonJsonRepository {

    private Set<T> elements;
    private final Class<T> elementsClass;

    /**
     * Constructs a FileSystemRepository with the specified element class.
     *
     * @param elementClass the class object representing
     *                     the type of elements to be stored in the repository
     */
    protected FileSystemRepository(
            @NonNull final Class<T> elementClass) {

        this.elementsClass = elementClass;
        this.elements = new HashSet<>();
    }

    /**
     * {@return the set of objects}
     */
    public Set<T> getAll() {
        return new HashSet<>(elements);
    }

    /**
     * Adds an element to the repository if it does net exist.
     *
     * @param element the element to be added
     * @return the view of the updated repository
     */
    public Set<T> addOne(
            @NonNull final T element) {

        elements.add(element);
        return getAll();
    }

    /**
     * Initializes the repository from a file.
     *
     * @param file the file which contains the JSON document
     * @return the view of the updated repository
     * @throws IOException if any I/O error occurs
     */
    public Set<T> loadFromFile(
            @NonNull final File file) throws IOException {

        elements = readSet(new FileInputStream(file), this.elementsClass);
        return elements;
    }

    /**
     * Saves the repository to a file.
     *
     * @param file the file which will contain the JSON document
     * @throws IOException if any I/O error occurs
     */
    public void saveToFile(
            @NonNull final File file) throws IOException {

        writeSet(new FileOutputStream(file), elements);
    }
}
