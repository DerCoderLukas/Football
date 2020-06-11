package de.dercoder.football.bukkit;

import com.google.inject.Inject;
import de.dercoder.football.core.FootballPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class FootballTrigger implements Listener {
  private final FootballGameRegistry footballGameRegistry;

  @Inject
  private FootballTrigger(FootballGameRegistry footballGameRegistry) {
    this.footballGameRegistry = footballGameRegistry;
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent playerMove) {
    var player = playerMove.getPlayer();
    var footballPlayer = FootballPlayer.withId(player.getUniqueId());
    footballGameRegistry
        .findGameOfPlayer(footballPlayer)
        .ifPresent(footballGame -> handleFootballKick(footballGame, footballPlayer, player));
  }

  private void handleFootballKick(
      FootballGame footballGame, FootballPlayer footballPlayer, Player player) {
    var football = footballGame.football();
    var footballLocation = football.location();
    var playerLocation = player.getLocation();
    if (footballLocation.distance(playerLocation) < 1.0) {
      footballGame
          .footballMatch()
          .findPlayerSession(footballPlayer)
          .ifPresent(footballPlayerSession -> football.kick(footballPlayerSession, player));
    }
  }
}
