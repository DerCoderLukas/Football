package de.dercoder.football.core;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

final class FootballMatchTest {
  private static final int FOOTBALL_TEAM_SIZE = 2;

  private FootballMatch footballMatch;

  @BeforeEach
  void initialize() {
    var football = EmptyFootball.create();
    var goals = new FootballGoal[] {EmptyFootballGoal.create(), EmptyFootballGoal.create()};

    var firstTeam = createFootballTeam(FOOTBALL_TEAM_SIZE, goals[0]);
    var secondTeam = createFootballTeam(FOOTBALL_TEAM_SIZE, goals[1]);
    var teams = new FootballTeam[] {firstTeam, secondTeam};

    Set<FootballPlayerSession> playerSessions = Sets.newHashSet();
    teamPlayersToSessionSet(firstTeam, playerSessions);
    teamPlayersToSessionSet(secondTeam, playerSessions);
    footballMatch = FootballMatch.of(football, goals, teams, playerSessions);
  }

  FootballTeam createFootballTeam(int size, FootballGoal footballGoal) {
    Set<FootballPlayer> players = Sets.newHashSet();
    for (var i = 0; i < size; i++) {
      players.add(FootballPlayer.withId(UUID.randomUUID()));
    }
    return FootballTeam.of(players, footballGoal, 0);
  }

  void teamPlayersToSessionSet(
      FootballTeam footballTeam, Set<FootballPlayerSession> playerSessions) {
    footballTeam.players().stream().map(this::sessionFromPlayer).forEach(playerSessions::add);
  }

  FootballPlayerSession sessionFromPlayer(FootballPlayer footballPlayer) {
    return FootballPlayerSession.of(footballPlayer, 0, FootballPunishment.UNPUNISHED);
  }

  @Test
  void testPlayerFinding() {
    var playerSession = footballMatch.players().stream().findFirst().get();
    var player = playerSession.player();
    var foundPlayerSession = footballMatch.findPlayerSession(player).get();
    assertEquals(playerSession, foundPlayerSession);
  }

  @Test
  void testPlayerTeamFinding() {
    var playerSession = footballMatch.players().stream().findFirst().get();
    var player = playerSession.player();
    var foundPlayerTeam = footballMatch.findTeamOfPlayer(player);
    assertTrue(foundPlayerTeam.isPresent());
  }

  @Test
  void testPlayerExchange() {
    var team = footballMatch.teams().get()[0];
    var playingPlayer = team.players().iterator().next();
    var playerToExchangeWith = FootballPlayer.withId(UUID.randomUUID());
    team.addPlayer(playerToExchangeWith);
    assertTrue(footballMatch.exchangePlayers(playingPlayer, playerToExchangeWith));
  }
}
