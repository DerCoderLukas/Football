package de.dercoder.football.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class FootballMatch {
    private final Football football;
    private final FootballGoal[] goals;
    private final Set<FootballPlayerSession> players;
    private FootballTeam[] teams;

    private FootballMatch(
            Football football,
            FootballGoal[] goals,
            FootballTeam[] teams,
            Set<FootballPlayerSession> players
    ) {
        this.football = football;
        this.goals = goals;
        this.teams = teams;
        this.players = players;
    }

    private FootballMatch(
            Football football,
            FootballGoal[] goals,
            Set<FootballPlayerSession> players
    ) {
        this.football = football;
        this.goals = goals;
        this.players = players;
    }

    public void addPlayer(FootballPlayerSession footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        players.add(footballPlayer);
    }

    public void addPlayer(FootballPlayer footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        findPlayerSession(footballPlayer).ifPresentOrElse(this::addPlayer, () -> createAndAddPlayer(footballPlayer));
    }

    private void createAndAddPlayer(FootballPlayer footballPlayer) {
        var footballPlayerSession = FootballPlayerSession.of(
                footballPlayer,
                0,
                FootballPunishment.UNPUNISHED
        );
        addPlayer(footballPlayerSession);
    }

    public void removePlayer(FootballPlayerSession footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        players.remove(footballPlayer);
    }

    public void removePlayer(FootballPlayer footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        findPlayerSession(footballPlayer).ifPresent(this::removePlayer);
    }

    public boolean exchangePlayers(
            FootballPlayer playingPlayer,
            FootballPlayer playerToExchangeWith
     ) {
        Preconditions.checkNotNull(playingPlayer);
        Preconditions.checkNotNull(playerToExchangeWith);
        var exchanged = new AtomicBoolean(false);
        findTeamOfPlayer(playingPlayer).ifPresent(playingPlayerTeam -> {
            findTeamOfPlayer(playerToExchangeWith).ifPresent(playerToExchangeWithTeam -> {
                 if (!playingPlayerTeam.equals(playerToExchangeWithTeam)) {
                     return;
                 }
                exchanged.set(true);
                exchangePlayersDirectly(
                        playingPlayer,
                        playerToExchangeWith
                );
            });
        });
        return exchanged.get();
    }

    private void exchangePlayersDirectly(
            FootballPlayer playingPlayer,
            FootballPlayer playerToExchangeWith
    ) {
        addPlayer(playerToExchangeWith);
        removePlayer(playingPlayer);
    }

    public Optional<FootballPlayerSession> findPlayerSession(FootballPlayer footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        return players.stream()
                .filter(player -> player.id().equals(footballPlayer.id()))
                .findFirst();
    }

    public Optional<FootballTeam> findTeamOfPlayer(FootballPlayer footballPlayer) {
        Preconditions.checkNotNull(footballPlayer);
        for (var footballTeam : teams) {
            if (footballTeam.contains(footballPlayer)) {
                return Optional.of(footballTeam);
            }
        }
        return Optional.empty();
    }

    public void fillMatch(FootballTeam[] teams) {
        this.teams = teams;
        for (var footballTeam : teams) {
            footballTeam.players().forEach(this::addPlayer);
        }
    }

    public Football football() {
        return football;
    }

    public FootballGoal[] goals() {
        return goals;
    }

    public Optional<FootballTeam[]> teams() {
        return Optional.ofNullable(teams);
    }

    public Collection<FootballPlayerSession> players() {
        return List.copyOf(players);
    }

    public static FootballMatch of(
            Football football,
            FootballGoal[] goals,
            FootballTeam[] teams,
            Set<FootballPlayerSession> players
    ) {
        Preconditions.checkNotNull(football);
        Preconditions.checkNotNull(goals);
        Preconditions.checkNotNull(teams);
        Preconditions.checkNotNull(players);
        return new FootballMatch(football, goals, teams, players);
    }

    public static FootballMatch empty(
            Football football,
            FootballGoal[] goals
    ) {
        Preconditions.checkNotNull(football);
        Preconditions.checkNotNull(goals);
        return new FootballMatch(football, goals, Sets.newHashSet());
    }
}
