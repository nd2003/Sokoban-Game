package sokoban.results;

import lombok.*;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Class representing the result of a game played by a specific player.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GameResult {
    
    @EqualsAndHashCode.Include
    private Long id;

    private String player;

    private boolean solved;

    private int steps;

    private Duration duration;

    private ZonedDateTime created;
}
