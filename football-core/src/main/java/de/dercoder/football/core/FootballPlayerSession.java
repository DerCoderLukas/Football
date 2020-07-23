package de.dercoder.football.core;

import java.util.UUID;

import com.google.common.base.Preconditions;

public final class FootballPlayerSession {
  private final FootballPlayer footballPlayer;
  private int goals;
  private FootballPunishment footballPunishment;

  private FootballPlayerSession(
    FootballPlayer footballPlayer,
    int goals,
    FootballPunishment footballPunishment
  ) {
    this.footballPlayer = footballPlayer;
    this.goals = goals;
    this.footballPunishment = footballPunishment;
  }

  public void shootAGoal() {
    goals += 1;
  }

  public void punish(FootballPunishment footballPunishment) {
    Preconditions.checkNotNull(footballPunishment);
    this.footballPunishment = footballPunishment.punish(this.footballPunishment);
  }

  public UUID id() {
    return footballPlayer.id();
  }

  public FootballPlayer player() {
    return footballPlayer;
  }

  public int goals() {
    return goals;
  }

  public FootballPunishment punishment() {
    return footballPunishment;
  }

  public static FootballPlayerSession of(
    FootballPlayer footballPlayer,
    int goals,
    FootballPunishment footballPunishment
  ) {
    Preconditions.checkNotNull(footballPlayer);
    Preconditions.checkNotNull(footballPunishment);
    return new FootballPlayerSession(footballPlayer, goals, footballPunishment);
  }
}
