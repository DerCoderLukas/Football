package de.dercoder.football.bukkit.stadium;

import java.util.List;

public final class FootballStadiumConfiguration {
  private List<FootballStadiumModel> stadiums;

  FootballStadiumConfiguration() {}

  FootballStadiumConfiguration(List<FootballStadiumModel> stadiums) {
    this.stadiums = stadiums;
  }

  public List<FootballStadiumModel> getStadiums() {
    return stadiums;
  }

  public void setStadiums(List<FootballStadiumModel> stadiums) {
    this.stadiums = stadiums;
  }
}
