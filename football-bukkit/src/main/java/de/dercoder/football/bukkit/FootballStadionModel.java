package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;

public final class FootballStadionModel {
    private String name;
    private FootballLocationModel stadionCenter;
    private FootballGoalModel[] goals;

    FootballStadionModel() {

    }

    FootballStadionModel(
            String name,
            FootballLocationModel stadionCenter,
            FootballGoalModel[] goals
    ) {
        this.name = name;
        this.stadionCenter = stadionCenter;
        this.goals = goals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FootballLocationModel getStadionCenter() {
        return stadionCenter;
    }

    public void setStadionCenter(
            FootballLocationModel stadionCenter
    ) {
        this.stadionCenter = stadionCenter;
    }

    public FootballGoalModel[] getGoals() {
        return goals;
    }

    public void setGoals(FootballGoalModel[] goals) {
        this.goals = goals;
    }

    public FootballStadion toFootballStadion() {
        var stadionGoals = modelsToGoals(goals);
        return FootballStadion.of(
                name,
                stadionCenter.toLocation(),
                stadionGoals
        );
    }

    private DefaultFootballGoal[] modelsToGoals(
            FootballGoalModel[] footballGoalModels
    ) {
        var footballGoals = new DefaultFootballGoal[footballGoalModels.length];
        for (var i = 0; i < footballGoalModels.length; i++) {
            var footballGoal = footballGoalModels[i];
            footballGoals[i] = modelToGoal(footballGoal);
        }
        return footballGoals;
    }

    private DefaultFootballGoal modelToGoal(
            FootballGoalModel footballGoalModel
    ) {
        return footballGoalModel.toFootballGoal();
    }

    public static FootballStadionModel ofFootballStadion(
            FootballStadion footballStadion
    ) {
        Preconditions.checkNotNull(footballStadion);
        var stadionCenterModel = FootballLocationModel
                .ofLocation(footballStadion.stadionCenter());
        var stadionGoals = modelsFromGoals(footballStadion.footballGoals());
        return new FootballStadionModel(
                footballStadion.name(),
                stadionCenterModel,
                stadionGoals
        );
    }

    private static FootballGoalModel[] modelsFromGoals(
            DefaultFootballGoal[] footballGoals
    ) {
        var goals = new FootballGoalModel[footballGoals.length];
        for (var i = 0; i < footballGoals.length; i++) {
            var footballGoal = footballGoals[i];
            goals[i] = modelFromGoal(footballGoal);
        }
        return goals;
    }

    private static FootballGoalModel modelFromGoal(
            DefaultFootballGoal footballGoal
    ) {
        return FootballGoalModel.ofFootballGoal(footballGoal);
    }
}
