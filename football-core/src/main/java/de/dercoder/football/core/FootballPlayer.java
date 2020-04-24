package de.dercoder.football.core;

import com.google.common.base.Preconditions;

import java.util.UUID;

public final class FootballPlayer {
    private final UUID id;

    private FootballPlayer(
            UUID id
    ) {
        this.id = id;
    }

    public UUID id() {
        return id;
    }

    public static FootballPlayer withId(
            UUID id
    ) {
        Preconditions.checkNotNull(id);
        return new FootballPlayer(id);
    }

}
