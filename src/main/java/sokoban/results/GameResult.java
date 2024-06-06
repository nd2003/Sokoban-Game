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

    /**
     * The name of the player.
     */
    private String player;

    /**
     * Indicates whether the player has finished the game.
     */
    private boolean solved;

    /**
     * The number of steps made by the player.
     */
    private int steps;

    /**
     * The duration of the game.
     */
    private Duration duration;

    /**
     * The date and time when the game has started.
     */
    private ZonedDateTime created;
}
