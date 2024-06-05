package sokoban.results;

import lombok.NonNull;
import util.repository.FileSystemRepository;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GameResultRepository extends FileSystemRepository<GameResult> {

    public GameResultRepository() {
        super(GameResult.class);
    }

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

    public List<GameResult> findBest(
            final int n) {

        return getAll().stream()
                .filter(GameResult::isSolved)
                .sorted(Comparator.comparing(GameResult::getDuration)
                        .thenComparing(GameResult::getCreated, Comparator.reverseOrder()))
                .limit(n)
                .toList();
    }
}
