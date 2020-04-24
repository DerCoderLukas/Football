package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;

public final class FootballGoalModel {
    private FootballLocationModel goalLocation;
    private int width;
    private int height;
    private String direction;

    FootballGoalModel() {

    }

    FootballGoalModel(
            FootballLocationModel goalLocation,
            int width,
            int height,
            String direction
    ) {
        this.goalLocation = goalLocation;
        this.width = width;
        this.height = height;
        this.direction = direction;
    }

    public FootballLocationModel getGoalLocation() {
        return goalLocation;
    }

    public void setGoalLocation(
            FootballLocationModel goalLocation
    ) {
        this.goalLocation = goalLocation;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public DefaultFootballGoal toFootballGoal() {
        return DefaultFootballGoal.of(
                goalLocation.toLocation(),
                width,
                height,
                DefaultFootballGoal
                        .FootballGoalDirection
                        .valueOf(direction)
        );
    }

    public static FootballGoalModel ofFootballGoal(
            DefaultFootballGoal footballGoal
    ) {
        Preconditions.checkNotNull(footballGoal);
        return new FootballGoalModel(
                FootballLocationModel.ofLocation(
                        footballGoal.goalLocation()
                ),
                footballGoal.width(),
                footballGoal.height(),
                footballGoal.direction().toString()
        );
    }
}
