package de.dercoder.football.bukkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Optional;

@Singleton
public final class FootballStadionRepository {
    private final ObjectMapper objectMapper;
    private final Path repositoryPath;
    private FootballStadion footballStadion;

    @Inject
    private FootballStadionRepository(
            ObjectMapper objectMapper,
            @Named("stadionPath") Path repositoryPath
    ) {
        this.objectMapper = objectMapper;
        this.repositoryPath = repositoryPath;
    }

    public void loadAll() {
        if (footballStadion != null) {
            return;
        }
        try {
            var configuration = readConfiguration();
            footballStadion = configuration.getStadion().toFootballStadion();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private FootballStadionConfiguration readConfiguration()
            throws FootballStadionConfigurationException
    {
        try {
            return objectMapper.readValue(
                    repositoryPath.toFile(),
                    FootballStadionConfiguration.class
            );
        } catch (Exception exception) {
            throw FootballStadionConfigurationException
                    .create("Can't load Stadion-Configuration");
        }
    }

    public void saveAll() {
        var configuration = buildConfiguration();
        writeConfiguration(configuration);
    }

    private FootballStadionConfiguration buildConfiguration() {
        var footballStadionModel = FootballStadionModel
                .ofFootballStadion(footballStadion);
        return new FootballStadionConfiguration(footballStadionModel);
    }

    private void writeConfiguration(
            FootballStadionConfiguration configuration
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

    public Optional<FootballStadion> footballStadion() {
        return Optional.ofNullable(footballStadion);
    }

    public void setFootballStadion(FootballStadion footballStadion) {
        Preconditions.checkNotNull(footballStadion);
        this.footballStadion = footballStadion;
    }
}
