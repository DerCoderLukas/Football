package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import org.bukkit.Location;

public final class FootballStadium {
    private final String name;
    private final Location stadiumCenter;
    private final DefaultFootballGoal[] footballGoals;

    private FootballStadium(
            String name,
            Location stadiumCenter,
            DefaultFootballGoal[] footballGoals
    ) {
        this.name = name;
        this.stadiumCenter = stadiumCenter;
        this.footballGoals = footballGoals;
    }

    public String name() {
        return name;
    }

    public Location stadiumCenter() {
        return stadiumCenter;
    }

    public DefaultFootballGoal[] footballGoals() {
        return footballGoals;
    }

    public static FootballStadium of(
            String name,
            Location stadiumCenter,
            DefaultFootballGoal[] footballGoals
    ) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(stadiumCenter);
        Preconditions.checkNotNull(footballGoals);
        return new FootballStadium(
                name,
                stadiumCenter,
                footballGoals
        );
    }
}
