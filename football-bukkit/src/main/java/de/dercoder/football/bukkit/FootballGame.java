package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import de.dercoder.football.core.FootballMatch;

public final class FootballGame {
    private final FootballMatch footballMatch;
    private FootballGameState footballGameState;

    private FootballGame(
            FootballMatch footballMatch,
            FootballGameState footballGameState
    ) {
        this.footballMatch = footballMatch;
        this.footballGameState = footballGameState;
    }

    public boolean isRunning() {
        return this.footballGameState == FootballGameState.RUNNING;
    }

    public void setFootballGameState(FootballGameState footballGameState) {
        Preconditions.checkNotNull(footballGameState);
        this.footballGameState = footballGameState;
    }

    public static FootballGame of(
            FootballMatch footballMatch,
            FootballGameState footballGameState
    ) {
        Preconditions.checkNotNull(footballMatch);
        Preconditions.checkNotNull(footballGameState);
        return new FootballGame(footballMatch, footballGameState);
    }

    enum FootballGameState {
        NOT_RUNNING,
        RUNNING
    }
}
