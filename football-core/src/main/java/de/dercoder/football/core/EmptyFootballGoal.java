package de.dercoder.football.core;

public final class EmptyFootballGoal implements FootballGoal {
    private EmptyFootballGoal() {

    }

    public static EmptyFootballGoal create() {
        return new EmptyFootballGoal();
    }
}
