package de.dercoder.football.bukkit.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.nio.file.Path;

@Singleton
public final class FootballConfigurationRepository {
  private final ObjectMapper objectMapper;
  private final Path repositoryPath;
  private FootballConfiguration footballConfiguration;

  @Inject
  private FootballConfigurationRepository(
    ObjectMapper objectMapper, @Named("configurationPath") Path repositoryPath
  ) {
    this.objectMapper = objectMapper;
    this.repositoryPath = repositoryPath;
  }

  public void load() {
    if (footballConfiguration != null) {
      return;
    }
    try {
      footballConfiguration = readConfiguration();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private FootballConfiguration readConfiguration() throws FootballConfigurationException {
    try {
      return objectMapper.readValue(
        repositoryPath.toFile(),
        FootballConfiguration.class
      );
    } catch (Exception exception) {
      throw FootballConfigurationException.create(
        "Can't load the football configuration");
    }
  }

  public void save() {
    writeConfiguration(footballConfiguration);
  }

  private void writeConfiguration(FootballConfiguration configuration) {
    try {
      objectMapper.writeValue(repositoryPath.toFile(), configuration);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public FootballConfiguration footballConfiguration() {
    if (footballConfiguration == null) {
      load();
    }
    return footballConfiguration;
  }

  public void setFootballConfiguration(FootballConfiguration footballConfiguration) {
    Preconditions.checkNotNull(footballConfiguration);
    this.footballConfiguration = footballConfiguration;
  }
}
