package de.dercoder.football.core;

import com.google.common.base.Preconditions;

import java.util.UUID;

public final class FootballPlayerSession {
    private final FootballPlayer footballPlayer;
    private int goals;
    private FootballPunishment footballPunishment;

    private FootballPlayerSession(
            FootballPlayer footballPlayer,
            int goals,
            FootballPunishment footballPunishment
    ) {
        this.footballPlayer = footballPlayer;
        this.goals = goals;
        this.footballPunishment = footballPunishment;
    }

    public void shootAGoal() {
        goals += 1;
    }

    public boolean punish(FootballPunishment footballPunishment) {
        Preconditions.checkNotNull(footballPunishment);
        if (footballPunishment == FootballPunishment.UNPUNISHED ||
                this.footballPunishment == FootballPunishment.RED_CARD) {
            return false;
        }
        if (this.footballPunishment == FootballPunishment.UNPUNISHED) {
            this.footballPunishment = footballPunishment;
            return true;
        }
        if (this.footballPunishment == FootballPunishment.YELLOW_CARD) {
            this.footballPunishment = FootballPunishment.RED_CARD;
            return true;
        }
        return false;
    }

    public UUID id() {
        return footballPlayer.id();
    }

    public FootballPlayer player() {
        return footballPlayer;
    }

    public int goals() {
        return goals;
    }

    public FootballPunishment punishment() {
        return footballPunishment;
    }

    public static FootballPlayerSession of(
            FootballPlayer footballPlayer,
            int goals,
            FootballPunishment footballPunishment
    ) {
        Preconditions.checkNotNull(footballPlayer);
        Preconditions.checkNotNull(footballPunishment);
        return new FootballPlayerSession(footballPlayer, goals, footballPunishment);
    }
}
