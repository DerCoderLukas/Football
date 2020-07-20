package de.dercoder.football.bukkit.goal;

import com.google.inject.Inject;

import de.dercoder.football.bukkit.football.FootballMoveEvent;
import de.dercoder.football.bukkit.game.FootballGame;
import de.dercoder.football.bukkit.game.FootballGameRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class FootballGoalTrigger implements Listener {
  private final FootballGameRegistry footballGameRegistry;

  @Inject
  private FootballGoalTrigger(FootballGameRegistry footballGameRegistry) {
    this.footballGameRegistry = footballGameRegistry;
  }

  @EventHandler
  public void onFootballMove(FootballMoveEvent footballMove) {
    var football = footballMove.football();
    footballGameRegistry.findGameByBall(football)
      .ifPresent(FootballGame::handleGoalScore);
  }
}
