package de.dercoder.football.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

public final class FootballTeam {
    private final Collection<FootballPlayer> players;
    private final FootballGoal footballGoal;
    private int goals;

    private FootballTeam(
            Collection<FootballPlayer> players,
            FootballGoal footballGoal,
            int goals
    ) {
        this.players = players;
        this.footballGoal = footballGoal;
        this.goals = goals;
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
        if(players.contains(footballPlayer)) {
            return true;
        }
        return this.players.stream()
                .anyMatch(player -> player.id().equals(footballPlayer.id()));
    }

    public void shootAGoal() {
        goals += 1;
    }

    public Collection<FootballPlayer> players() {
        return Set.copyOf(players);
    }

    public FootballGoal footballGoal() {
        return footballGoal;
    }

    public int goals() {
        return goals;
    }

    public static FootballTeam of(
            Set<FootballPlayer> players,
            FootballGoal footballGoal,
            int goals
    ) {
        Preconditions.checkNotNull(players);
        Preconditions.checkNotNull(footballGoal);
        return new FootballTeam(
                Sets.newHashSet(players),
                footballGoal,
                goals
        );
    }

    public static FootballTeam empty(
            FootballGoal footballGoal,
            int goals
    ) {
        Preconditions.checkNotNull(footballGoal);
        return new FootballTeam(
                Sets.newHashSet(),
                footballGoal,
                goals
        );
    }
}
