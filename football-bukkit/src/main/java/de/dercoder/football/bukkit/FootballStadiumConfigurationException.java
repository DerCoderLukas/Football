package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;

public final class FootballStadiumConfigurationException extends Exception {
    private FootballStadiumConfigurationException(
            String message
    ) {
        super(message);
    }

    public static FootballStadiumConfigurationException create(
            String message
    ) {
        Preconditions.checkNotNull(message);
        return new FootballStadiumConfigurationException(message);
    }
}
