package de.dercoder.football.bukkit;

public final class FootballStadionConfiguration {

    private FootballStadionModel stadion;

    FootballStadionConfiguration() {

    }

    FootballStadionConfiguration(
            FootballStadionModel stadion
    ) {
        this.stadion = stadion;
    }

    public FootballStadionModel getStadion() {
        return stadion;
    }

    public void setStadion(FootballStadionModel stadion) {
        this.stadion = stadion;
    }
}
