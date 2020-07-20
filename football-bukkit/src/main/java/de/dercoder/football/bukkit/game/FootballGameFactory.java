package de.dercoder.football.bukkit.game;

import com.google.common.base.Preconditions;

import com.google.inject.Inject;

import de.dercoder.football.bukkit.stadium.FootballStadium;
import de.dercoder.football.bukkit.configuration.FootballConfiguration;
import de.dercoder.football.bukkit.configuration.FootballConfigurationRepository;
import de.dercoder.football.core.FootballMatch;

import javax.inject.Singleton;

@Singleton
public final class FootballGameFactory {
  private final FootballGameRegistry footballGameRegistry;
  private final FootballConfiguration footballConfiguration;

  @Inject
  private FootballGameFactory(
    FootballGameRegistry footballGameRegistry,
    FootballConfigurationRepository footballConfigurationRepository
  ) {
    this.footballGameRegistry = footballGameRegistry;
    this.footballConfiguration = footballConfigurationRepository.footballConfiguration();
  }

  public FootballGame createFootballGame(
    FootballMatch footballMatch, FootballStadium footballStadium
  ) {
    Preconditions.checkNotNull(footballMatch);
    Preconditions.checkNotNull(footballStadium);
    var footballGame = FootballGame.of(
      footballMatch,
      footballStadium,
      footballGameRegistry,
      footballConfiguration
    );
    footballGameRegistry.register(footballGame);
    return footballGame;
  }
}
