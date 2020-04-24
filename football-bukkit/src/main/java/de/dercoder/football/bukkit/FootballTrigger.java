package de.dercoder.football.bukkit;

import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class FootballTrigger implements Listener {
    private final FootballGame footballGame;

    @Inject
    private FootballTrigger(
            FootballGame footballGame
    ) {
        this.footballGame = footballGame;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMove) {
        var player = playerMove.getPlayer();
    }
}
