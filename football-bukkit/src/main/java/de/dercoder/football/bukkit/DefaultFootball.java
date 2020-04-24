package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import de.dercoder.football.core.Football;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class DefaultFootball implements Football {
    private final Location spawnLocation;
    private Entity footballEntity;

    private DefaultFootball(
            Location spawnLocation
    ) {
        this.spawnLocation = spawnLocation;
    }

    public void spawn() {

    }

    public void kick(Player player) {
        Preconditions.checkNotNull(player);
    }

    public static DefaultFootball of(
            Location spawnLocation
    ) {
        Preconditions.checkNotNull(spawnLocation);
        return new DefaultFootball(spawnLocation);
    }
}
