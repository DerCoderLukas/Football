package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;

import java.util.Arrays;

public final class FootballStadiumModel {
  private String name;
  private FootballLocationModel stadiumCenter;
  private FootballGoalModel[] goals;

  FootballStadiumModel() {}

  FootballStadiumModel(
      String name, FootballLocationModel stadiumCenter, FootballGoalModel[] goals) {
    this.name = name;
    this.stadiumCenter = stadiumCenter;
    this.goals = goals;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FootballLocationModel getStadiumCenter() {
    return stadiumCenter;
  }

  public void setStadiumCenter(FootballLocationModel stadiumCenter) {
    this.stadiumCenter = stadiumCenter;
  }

  public FootballGoalModel[] getGoals() {
    return goals;
  }

  public void setGoals(FootballGoalModel[] goals) {
    this.goals = goals;
  }

  public FootballStadium toFootballStadium() {
    var stadiumGoals = modelsToGoals(goals);
    return FootballStadium.of(name, stadiumCenter.toLocation(), stadiumGoals);
  }

  private DefaultFootballGoal[] modelsToGoals(FootballGoalModel[] footballGoalModels) {
    var goals =
        Arrays.stream(footballGoalModels)
            .map(this::modelToGoal)
            .toArray(DefaultFootballGoal[]::new);
    return goals;
  }

  private DefaultFootballGoal modelToGoal(FootballGoalModel footballGoalModel) {
    return footballGoalModel.toFootballGoal();
  }

  public static FootballStadiumModel ofFootballStadium(FootballStadium footballStadium) {
    Preconditions.checkNotNull(footballStadium);
    var stadiumCenterModel = FootballLocationModel.ofLocation(footballStadium.stadiumCenter());
    var stadiumGoals = modelsFromGoals(footballStadium.footballGoals());
    return new FootballStadiumModel(footballStadium.name(), stadiumCenterModel, stadiumGoals);
  }

  private static FootballGoalModel[] modelsFromGoals(DefaultFootballGoal[] footballGoals) {
    var goalModels =
        Arrays.stream(footballGoals)
            .map(FootballStadiumModel::modelFromGoal)
            .toArray(FootballGoalModel[]::new);
    return goalModels;
  }

  private static FootballGoalModel modelFromGoal(DefaultFootballGoal footballGoal) {
    return FootballGoalModel.ofFootballGoal(footballGoal);
  }
}
