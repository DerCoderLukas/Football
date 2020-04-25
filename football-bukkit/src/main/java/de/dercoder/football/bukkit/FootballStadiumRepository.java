package de.dercoder.football.bukkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;

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

    public void load() {
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
            throws FootballConfigurationException
    {
        try {
            return objectMapper.readValue(
                    repositoryPath.toFile(),
                    FootballStadiumConfiguration.class
            );
        } catch (Exception exception) {
            throw FootballConfigurationException
                    .create("Can't load the stadium configuration");
        }
    }

    public void save() {
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

    public FootballStadium footballStadium() {
        if (footballStadium == null) {
            load();
        }
        return footballStadium;
    }

    public void setFootballStadium(FootballStadium footballStadium) {
        Preconditions.checkNotNull(footballStadium);
        this.footballStadium = footballStadium;
    }
}
