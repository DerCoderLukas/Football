package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import de.dercoder.football.core.FootballGoal;
import org.bukkit.Location;

public final class DefaultFootballGoal implements FootballGoal {
    private final Location goalLocation;
    private final int width;
    private final int height;
    private final FootballGoalDirection direction;

    private DefaultFootballGoal(
            Location goalLocation,
            int width,
            int height,
            FootballGoalDirection direction
    ) {
        this.goalLocation = goalLocation;
        this.width = width;
        this.height = height;
        this.direction = direction;
    }

    public Location goalLocation() {
        return goalLocation;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public FootballGoalDirection direction() {
        return direction;
    }

    public static DefaultFootballGoal of(
            Location goalLocation,
            int width,
            int height,
            FootballGoalDirection direction
    ) {
        Preconditions.checkNotNull(goalLocation);
        Preconditions.checkNotNull(direction);
        return new DefaultFootballGoal(
                goalLocation,
                width,
                height,
                direction
        );
    }

    enum FootballGoalDirection {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}
