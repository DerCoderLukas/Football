package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.dercoder.football.core.Football;
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

    private DefaultFootball(
            Location spawnLocation,
            String footballTextureValue,
            String footballTextureSignature
    ) {
        this.spawnLocation = spawnLocation;
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

    public void kick(Player player) {
        Preconditions.checkNotNull(player);
        var playerLocation = player.getLocation();
        var footballEntityLocation = footballEntity.getLocation();
        var velocityX = footballEntityLocation.getX() - playerLocation.getX();
        var velocityY = playerLocation.getY() - footballEntityLocation.getY();
        var velocityZ = footballEntityLocation.getZ() - playerLocation.getZ();
        var footballVelocity = new Vector(velocityX, velocityY, velocityZ);
        footballEntity.setVelocity(footballVelocity);
    }

    public Location location() {
        return footballEntity.getLocation();
    }

    public static DefaultFootball of(
            Location spawnLocation,
            String footballTextureValue,
            String footballTextureSignature
    ) {
        Preconditions.checkNotNull(spawnLocation);
        Preconditions.checkNotNull(footballTextureValue);
        Preconditions.checkNotNull(footballTextureSignature);
        return new DefaultFootball(
                spawnLocation,
                footballTextureValue,
                footballTextureSignature
        );
    }
}
