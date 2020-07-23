package de.dercoder.football.core;

import java.util.UUID;

import com.google.common.base.Preconditions;

public final class FootballPlayer {
  private final UUID id;

  private FootballPlayer(UUID id) {
    this.id = id;
  }

  public UUID id() {
    return id;
  }

  public static FootballPlayer withId(UUID id) {
    Preconditions.checkNotNull(id);
    return new FootballPlayer(id);
  }
}
