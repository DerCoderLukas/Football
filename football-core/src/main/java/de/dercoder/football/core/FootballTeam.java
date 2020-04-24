package de.dercoder.football.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class FootballTeam {
    private final Set<FootballPlayer> players;

    private FootballTeam(
            Set<FootballPlayer> players
    ) {
        this.players = players;
    }

    public void addPlayer(FootballPlayer footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        players.add(footballPlayer);
    }

    public void removePlayer(FootballPlayer footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        players.remove(footballPlayer);
    }

    public boolean contains(FootballPlayer footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        return players.contains(footballPlayer);
    }

    public Collection<FootballPlayer> players() {
        return Set.copyOf(players);
    }

    public static FootballTeam withPlayers(
            Set<FootballPlayer> players
    ) {
        Preconditions.checkNotNull(players);
        return new FootballTeam(players);
    }

    public static FootballTeam empty() {
        return new FootballTeam(Sets.newHashSet());
    }
}
