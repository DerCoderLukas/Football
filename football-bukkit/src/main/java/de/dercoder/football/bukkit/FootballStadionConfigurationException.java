package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;

public final class FootballStadionConfigurationException extends Exception {
    private FootballStadionConfigurationException(
            String message
    ) {
        super(message);
    }

    public static FootballStadionConfigurationException create(
            String message
    ) {
        Preconditions.checkNotNull(message);
        return new FootballStadionConfigurationException(message);
    }
}
