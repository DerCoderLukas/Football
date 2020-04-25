package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.bukkit.Location;

import javax.inject.Singleton;

@Singleton
public final class DefaultFootballFactory {
    private final FootballConfigurationRepository footballConfigurationRepository;

    @Inject
    private DefaultFootballFactory(
            FootballConfigurationRepository footballConfigurationRepository
    ) {
        this.footballConfigurationRepository = footballConfigurationRepository;
    }

    public DefaultFootball createDefaultFootball(
            Location spawnLocation
    ) {
        Preconditions.checkNotNull(spawnLocation);
        var configuration = footballConfigurationRepository.footballConfiguration();
        var footballTextureValue = configuration.getFootballTextureValue();
        var footballTextureSignature = configuration.getFootballTextureSignature();
        return DefaultFootball.of(
                spawnLocation,
                footballTextureValue,
                footballTextureSignature
        );
    }
}
