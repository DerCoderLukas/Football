package de.dercoder.football.bukkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import de.dercoder.football.core.FootballMatch;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Singleton;
import java.nio.file.Path;

public final class FootballModule extends AbstractModule {
    private final JavaPlugin plugin;

    private FootballModule(
            JavaPlugin plugin
    ) {
        this.plugin = plugin;
    }

    @Provides
    @Singleton
    PluginManager providePluginManager() {
        return plugin.getServer().getPluginManager();
    }

    @Provides
    @Singleton
    YAMLFactory provideYamlFactory() {
        return YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build();
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper(YAMLFactory yamlFactory) {
        return new ObjectMapper(yamlFactory);
    }

    private static final String STADIUM_REPOSITORY_PATH = "stadium.yml";

    @Provides
    @Singleton
    @Named("stadiumPath")
    Path provideStadiumPath() {
        return Path.of(plugin.getDataFolder().getAbsolutePath(), STADIUM_REPOSITORY_PATH);
    }

    @Provides
    @Singleton
    FootballStadium provideFootballStadium(
            FootballStadiumRepository footballStadiumRepository
    ) {
        var footballStadiumOptional = footballStadiumRepository.footballStadium();
        if (footballStadiumOptional.isEmpty()) {
            footballStadiumRepository.loadAll();
            return footballStadiumRepository.footballStadium().get();
        }
        return footballStadiumOptional.get();
    }

    @Provides
    @Singleton
    DefaultFootball provideDefaultFootball(
            FootballStadium footballStadium
    ) {
        return DefaultFootball.of(footballStadium.stadiumCenter());
    }

    @Provides
    @Singleton
    FootballMatch provideFootballMatch(
            DefaultFootball football,
            FootballStadium footballStadium
    ) {
        return FootballMatch.empty(football, footballStadium.footballGoals());
    }

    @Provides
    @Singleton
    FootballGame provideFootballGame(
            FootballMatch footballMatch
    ) {
        return FootballGame.of(
                footballMatch,
                FootballGame.FootballGameState.NOT_RUNNING
        );
    }

    public static FootballModule withPlugin(
            JavaPlugin plugin
    ) {
        Preconditions.checkNotNull(plugin);
        return new FootballModule(plugin);
    }
}
