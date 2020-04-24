package de.dercoder.football.bukkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Optional;

@Singleton
public final class FootballStadiumRepository {
    private final ObjectMapper objectMapper;
    private final Path repositoryPath;
    private FootballStadium footballStadium;

    @Inject
    private FootballStadiumRepository(
            ObjectMapper objectMapper,
            @Named("stadiumPath") Path repositoryPath
    ) {
        this.objectMapper = objectMapper;
        this.repositoryPath = repositoryPath;
    }

    public void loadAll() {
        if (footballStadium != null) {
            return;
        }
        try {
            var configuration = readConfiguration();
            footballStadium = configuration.getStadium().toFootballStadium();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private FootballStadiumConfiguration readConfiguration()
            throws FootballStadiumConfigurationException
    {
        try {
            return objectMapper.readValue(
                    repositoryPath.toFile(),
                    FootballStadiumConfiguration.class
            );
        } catch (Exception exception) {
            throw FootballStadiumConfigurationException
                    .create("Can't load Stadium-Configuration");
        }
    }

    public void saveAll() {
        var configuration = buildConfiguration();
        writeConfiguration(configuration);
    }

    private FootballStadiumConfiguration buildConfiguration() {
        var footballStadiumModel = FootballStadiumModel
                .ofFootballStadium(footballStadium);
        return new FootballStadiumConfiguration(footballStadiumModel);
    }

    private void writeConfiguration(
            FootballStadiumConfiguration configuration
    ) {
        try {
            objectMapper.writeValue(
                    repositoryPath.toFile(),
                    configuration
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Optional<FootballStadium> footballStadium() {
        return Optional.ofNullable(footballStadium);
    }

    public void setFootballStadium(FootballStadium footballStadium) {
        Preconditions.checkNotNull(footballStadium);
        this.footballStadium = footballStadium;
    }
}
