package de.dercoder.football.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

final class FootballPlayerSessionTest {
  private FootballPlayerSession playerSession;

  @BeforeEach
  void initialize() {
    var player = FootballPlayer.withId(UUID.randomUUID());
    playerSession = FootballPlayerSession.of(player, 0, FootballPunishment.UNPUNISHED);
  }

  @Test
  void testPlayerPunishment() {
    playerSession.punish(FootballPunishment.YELLOW_CARD);
    assertEquals(FootballPunishment.YELLOW_CARD, playerSession.punishment());
    playerSession.punish(FootballPunishment.YELLOW_CARD);
    assertEquals(FootballPunishment.RED_CARD, playerSession.punishment());
  }

  @Test
  void testPlayerGoalShooting() {
    playerSession.shootAGoal();
    assertEquals(1, playerSession.goals());
  }
}
