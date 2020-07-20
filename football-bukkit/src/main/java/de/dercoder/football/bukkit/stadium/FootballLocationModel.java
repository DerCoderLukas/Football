package de.dercoder.football.bukkit.stadium;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class FootballLocationModel {
  private String worldName;
  private double x;
  private double y;
  private double z;
  private float yaw;
  private float pitch;

  FootballLocationModel() {}

  FootballLocationModel(
    String worldName,
    double x,
    double y,
    double z,
    float yaw,
    float pitch
  ) {
    this.worldName = worldName;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public String getWorldName() {
    return worldName;
  }

  public void setWorldName(String worldName) {
    this.worldName = worldName;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  public float getYaw() {
    return yaw;
  }

  public void setYaw(float yaw) {
    this.yaw = yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  public Location toLocation() {
    return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
  }

  public static FootballLocationModel ofLocation(Location location) {
    Preconditions.checkNotNull(location);
    return new FootballLocationModel(location.getWorld().getName(),
      location.getX(),
      location.getY(),
      location.getZ(),
      location.getYaw(),
      location.getPitch()
    );
  }
}
