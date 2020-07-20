package de.dercoder.football.bukkit.configuration;

import com.google.common.base.Preconditions;

public final class FootballConfigurationException extends Exception {
  private FootballConfigurationException(String message) {
    super(message);
  }

  public static FootballConfigurationException create(String message) {
    Preconditions.checkNotNull(message);
    return new FootballConfigurationException(message);
  }
}
