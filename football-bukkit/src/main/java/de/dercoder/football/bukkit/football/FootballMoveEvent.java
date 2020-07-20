package de.dercoder.football.bukkit.football;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class FootballMoveEvent extends Event {
  private static final HandlerList HANDLERS = new HandlerList();

  private final DefaultFootball football;
  private final Location locationTo;
  private final Location locationFrom;

  private FootballMoveEvent(
    DefaultFootball football,
    Location locationTo,
    Location locationFrom
  ) {
    this.football = football;
    this.locationTo = locationTo;
    this.locationFrom = locationFrom;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public DefaultFootball football() {
    return football;
  }

  public Location locationTo() {
    return locationTo;
  }

  public Location locationFrom() {
    return locationFrom;
  }

  public static FootballMoveEvent of(
    DefaultFootball football, Location locationTo, Location locationFrom
  ) {
    return new FootballMoveEvent(football, locationTo, locationFrom);
  }
}
