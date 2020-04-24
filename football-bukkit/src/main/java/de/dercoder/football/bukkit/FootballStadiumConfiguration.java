package de.dercoder.football.bukkit;

public final class FootballStadiumConfiguration {

    private FootballStadiumModel stadium;

    FootballStadiumConfiguration() {

    }

    FootballStadiumConfiguration(
            FootballStadiumModel stadium
    ) {
        this.stadium = stadium;
    }

    public FootballStadiumModel getStadium() {
        return stadium;
    }

    public void setStadium(FootballStadiumModel stadium) {
        this.stadium = stadium;
    }
}
