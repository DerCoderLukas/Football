package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.dercoder.football.core.Football;
import de.dercoder.football.core.FootballPlayerSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.UUID;

public final class DefaultFootball implements Football {
    private final Location spawnLocation;
    private final String footballTextureValue;
    private final String footballTextureSignature;

    private ArmorStand footballEntity;
    private int triggeringTask;
    private Location lastLocation;
    private FootballPlayerSession shooter;

    private DefaultFootball(
            Location spawnLocation,
            String footballTextureValue,
            String footballTextureSignature
    ) {
        this.spawnLocation = spawnLocation;
        this.lastLocation = spawnLocation;
        this.footballTextureValue = footballTextureValue;
        this.footballTextureSignature = footballTextureSignature;
    }

    public void spawn() {
        footballEntity = (ArmorStand) spawnLocation.getWorld()
                .spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
        var footballItemStack = createFootballItemStack();
        footballEntity.setHelmet(footballItemStack);
        footballEntity.setInvulnerable(true);
        footballEntity.setCustomNameVisible(false);
        footballEntity.setVisible(false);
        footballEntity.setSmall(true);
    }

    private ItemStack createFootballItemStack() {
        var itemStack = new ItemStack(Material.PLAYER_HEAD);
        var itemMeta = itemStack.getItemMeta();
        var gameProfile = profileFromTextures(
                footballTextureValue,
                footballTextureSignature
        );
        profileToMeta(itemMeta, gameProfile);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private GameProfile profileFromTextures(
            String textureValue,
            String textureSignature
    ) {
        var gameProfile = new GameProfile(UUID.randomUUID(), "");
        gameProfile.getProperties().put("textures",
                new Property("textures",
                        textureValue,
                        textureSignature
                )
        );
        return gameProfile;
    }

    private void profileToMeta(ItemMeta itemMeta, GameProfile gameProfile) {
        try {
            var profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, gameProfile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void reset() {
        footballEntity.teleport(spawnLocation);
        footballEntity.setVelocity(new Vector(0, 0, 0));
    }

    public void despawn() {
        footballEntity.remove();
    }

    public void kick(
            FootballPlayerSession footballPlayerSession,
            Player player
    ) {
        Preconditions.checkNotNull(footballPlayerSession);
        Preconditions.checkNotNull(player);
        var playerLocation = player.getLocation();
        var footballEntityLocation = footballEntity.getLocation();
        var velocityX = footballEntityLocation.getX() - playerLocation.getX();
        var velocityY = playerLocation.getY() - footballEntityLocation.getY();
        var velocityZ = footballEntityLocation.getZ() - playerLocation.getZ();
        var footballVelocity = new Vector(velocityX, velocityY, velocityZ);
        footballEntity.setVelocity(footballVelocity);
        shooter = footballPlayerSession;
    }

    private static final int FOOTBALL_TRIGGERING_INTERVAL_TICKS = 2;

    public void startTriggering() {
        triggeringTask = Bukkit.getScheduler().scheduleAsyncRepeatingTask(
                FootballPlugin.getPlugin(FootballPlugin.class),
                () -> {
                    var location = footballEntity.getLocation();
                    if (isLocationEquals(location, lastLocation)) {
                        return;
                    }
                    var footballMoveEvent = FootballMoveEvent.of(
                            this,
                            location.clone(),
                            lastLocation.clone()
                    );
                    lastLocation = location;
                    Bukkit.getScheduler()
                            .runTask(
                                    FootballPlugin.getPlugin(FootballPlugin.class),
                                    () -> { Bukkit.getPluginManager().callEvent(footballMoveEvent); }
                            );
                },
                0,
                FOOTBALL_TRIGGERING_INTERVAL_TICKS
        );
    }

    private boolean isLocationEquals(
            Location fistLocation,
            Location secondLocation
    ) {
        if (
                fistLocation.getX() == secondLocation.getX() &&
                fistLocation.getY() == secondLocation.getY() &&
                fistLocation.getZ() == secondLocation.getZ() &&
                fistLocation.getYaw() == secondLocation.getYaw() &&
                fistLocation.getPitch() == secondLocation.getPitch()
        ) {
            return true;
        }
        return false;
    }

    public void stopTriggering() {
        Bukkit.getScheduler().cancelTask(triggeringTask);
    }

    public Location location() {
        return footballEntity.getLocation().clone();
    }

    public FootballPlayerSession shooter() {
        return shooter;
    }

    public static DefaultFootball of(
            Location spawnLocation,
            String footballTextureValue,
            String footballTextureSignature
    ) {
        Preconditions.checkNotNull(spawnLocation);
        Preconditions.checkNotNull(footballTextureValue);
        Preconditions.checkNotNull(footballTextureSignature);
        var football = new DefaultFootball(
                spawnLocation,
                footballTextureValue,
                footballTextureSignature
        );
        football.spawn();
        football.startTriggering();
        return football;
    }
}
