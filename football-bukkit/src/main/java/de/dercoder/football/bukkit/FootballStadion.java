package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import org.bukkit.Location;

public final class FootballStadion {
    private final String name;
    private final Location stadionCenter;
    private final DefaultFootballGoal[] footballGoals;

    private FootballStadion(
            String name,
            Location stadionCenter,
            DefaultFootballGoal[] footballGoals
    ) {
        this.name = name;
        this.stadionCenter = stadionCenter;
        this.footballGoals = footballGoals;
    }

    public String name() {
        return name;
    }

    public Location stadionCenter() {
        return stadionCenter;
    }

    public DefaultFootballGoal[] footballGoals() {
        return footballGoals;
    }

    public static FootballStadion of(
            String name,
            Location stadionCenter,
            DefaultFootballGoal[] footballGoals
    ) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(stadionCenter);
        Preconditions.checkNotNull(footballGoals);
        return new FootballStadion(
                name,
                stadionCenter,
                footballGoals
        );
    }
}
