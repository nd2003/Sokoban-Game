package sokoban.results;

import lombok.NonNull;
import util.repository.FileSystemRepository;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


/**
 * Repository class for the {@link GameResult} entity.
 */
public class GameResultRepository extends FileSystemRepository<GameResult> {

    /**
     * Constructor for the GameResultRepository.
     * This constructor initializes the repository with the GameResult.
     */
    public GameResultRepository() {
        super(GameResult.class);
    }

    /**
     * Adds a new GameResult element to the set, ensuring it has
     * an unique ID and setting the creation timestamp.
     *
     * @param element the GameResult element to be added; must not be null.
     * @return the updated set of GameResult elements after adding the new element.
     *
     * @throws NullPointerException if the element is null.
     */
    @Override
    public Set<GameResult> addOne(
            @NonNull final GameResult element) {

        if (element.getId() == null) {
            element.setId(getAll().stream()
                    .mapToLong(GameResult::getId)
                    .max()
                    .orElse(0L) + 1L);
        }
        element.setCreated(ZonedDateTime.now());
        return super.addOne(element);
    }

    /**
     * Returns the list of {@code n} best results with respect to the steps taken
     * for finishing the game.
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results with respect to the steps taken
     * for finishing the game.
     */
    public List<GameResult> findBest(
            final int n) {

        return getAll().stream()
                .filter(GameResult::isSolved)
                .sorted(Comparator.comparing(GameResult::getSteps)
                        .thenComparing(GameResult::getCreated, Comparator.reverseOrder()))
                .limit(n)
                .toList();
    }
}
