package de.dercoder.football.bukkit.stadium;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import de.dercoder.football.bukkit.configuration.FootballConfigurationException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public final class FootballStadiumRepository {
  private final Collection<FootballStadium> footballStadiums;
  private final ObjectMapper objectMapper;
  private final Path repositoryPath;

  @Inject
  private FootballStadiumRepository(
    ObjectMapper objectMapper, @Named("stadiumsPath") Path repositoryPath
  ) {
    this.footballStadiums = Sets.newHashSet();
    this.objectMapper = objectMapper;
    this.repositoryPath = repositoryPath;
  }

  public void loadAll() {
    try {
      var configuration = readConfiguration();
      configuration.getStadiums().forEach(this::register);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private FootballStadiumConfiguration readConfiguration() throws FootballConfigurationException {
    try {
      return objectMapper.readValue(
        repositoryPath.toFile(),
        FootballStadiumConfiguration.class
      );
    } catch (Exception exception) {
      exception.printStackTrace();
      throw FootballConfigurationException.create(
        "Can't load the stadium configuration");
    }
  }

  public void saveAll() {
    var configuration = buildConfiguration();
    writeConfiguration(configuration);
  }

  private FootballStadiumConfiguration buildConfiguration() {
    var footballStadiumsModels = footballStadiums.stream()
      .map(FootballStadiumModel::ofFootballStadium)
      .collect(Collectors.toUnmodifiableList());
    return new FootballStadiumConfiguration(footballStadiumsModels);
  }

  private void writeConfiguration(FootballStadiumConfiguration configuration) {
    try {
      objectMapper.writeValue(repositoryPath.toFile(), configuration);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public Optional<FootballStadium> find(String name) {
    Preconditions.checkNotNull(name);
    return footballStadiums.stream()
      .filter(stadium -> stadium.name().equals(name))
      .findFirst();
  }

  public Collection<FootballStadium> findAll() {
    return Set.copyOf(footballStadiums);
  }

  public void register(FootballStadium footballStadium) {
    Preconditions.checkNotNull(footballStadium);
    footballStadiums.add(footballStadium);
  }

  public void register(FootballStadiumModel footballStadiumModel) {
    Preconditions.checkNotNull(footballStadiumModel);
    footballStadiums.add(footballStadiumModel.toFootballStadium());
  }

  public void unregister(FootballStadium footballStadium) {
    Preconditions.checkNotNull(footballStadium);
    footballStadiums.remove(footballStadium);
  }

  public void unregister(FootballStadiumModel footballStadiumModel) {
    Preconditions.checkNotNull(footballStadiumModel);
    footballStadiums.remove(footballStadiumModel.toFootballStadium());
  }
}
