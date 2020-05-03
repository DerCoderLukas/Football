package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.bukkit.Location;

public final class FootballStadium {
    private final String name;
    private final Location stadiumCenter;
    private DefaultFootballGoal[] footballGoals;

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

    public void addFootballGoal(DefaultFootballGoal defaultFootballGoal) {
        Preconditions.checkNotNull(defaultFootballGoal);
        var footballGoalList = Lists.newArrayList(footballGoals);
        footballGoalList.add(defaultFootballGoal);
        footballGoals = footballGoalList.toArray(DefaultFootballGoal[]::new);
    }

    public void removeFootballGoal(DefaultFootballGoal defaultFootballGoal) {
        Preconditions.checkNotNull(defaultFootballGoal);
        var footballGoalList = Lists.newArrayList(footballGoals);
        footballGoalList.remove(defaultFootballGoal);
        footballGoals = footballGoalList.toArray(DefaultFootballGoal[]::new);
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
