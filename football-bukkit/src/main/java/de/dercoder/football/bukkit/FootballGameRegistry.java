package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import de.dercoder.football.core.FootballPlayer;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public final class FootballGameRegistry {
    private Collection<FootballGame> footballGames;

    private FootballGameRegistry(
            Collection<FootballGame> footballGames
    ) {
        this.footballGames = footballGames;
    }

    public void register(FootballGame footballGame) {
        Preconditions.checkNotNull(footballGame);
        footballGames.add(footballGame);
    }

    public void unregister(FootballGame footballGame) {
        Preconditions.checkNotNull(footballGame);
        footballGames.remove(footballGame);
    }

    public boolean isPlayerActive(FootballPlayer footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        return footballGames.stream()
                .anyMatch(footballGame -> footballGame.footballMatch().contains(footballPlayer));
    }

    public Optional<FootballGame> findGameOfPlayer(FootballPlayer footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        return footballGames.stream()
                .filter(footballGame -> footballGame.footballMatch().contains(footballPlayer))
                .findFirst();
    }

    public Optional<FootballGame> findGameByStadium(FootballStadium footballStadium) {
        Preconditions.checkNotNull(footballStadium);
        return footballGames.stream()
                .filter(footballGame ->
                        footballGame.footballStadium().equals(footballStadium)
                )
                .findFirst();
    }

    public Optional<FootballGame> findGameByBall(DefaultFootball football) {
        Preconditions.checkNotNull(football);
        return footballGames.stream()
                .filter(footballGame ->
                        footballGame.football().equals(football)
                )
                .findFirst();
    }

    public Collection<FootballGame> footballGames() {
        return Set.copyOf(footballGames);
    }

    public static FootballGameRegistry empty() {
        return new FootballGameRegistry(Sets.newHashSet());
    }
}
