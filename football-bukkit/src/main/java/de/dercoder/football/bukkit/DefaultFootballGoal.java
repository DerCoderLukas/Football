package de.dercoder.football.bukkit;

import com.google.common.base.Preconditions;
import de.dercoder.football.core.FootballGoal;
import org.bukkit.Location;

public final class DefaultFootballGoal implements FootballGoal {
  private final Location goalLocation;
  private final int width;
  private final int height;
  private final FootballGoalDirection direction;

  private DefaultFootballGoal(
      Location goalLocation, int width, int height, FootballGoalDirection direction) {
    this.goalLocation = goalLocation;
    this.width = width;
    this.height = height;
    this.direction = direction;
  }

  public boolean isInGoal(DefaultFootball football) {
    Preconditions.checkNotNull(football);
    var footballLocation = football.location();
    var goalLocationHeight = goalLocation.getY();
    if (footballLocation.getY() < goalLocationHeight
        || footballLocation.getY() > goalLocationHeight + height) {
      return false;
    }
    var goalWidthHalf = ((double) width) / 2.0;
    switch (direction) {
      case NORTH:
        return (footballLocation.getZ() > goalLocation.getZ()
            && footballLocation.getX() > goalLocation.getX() - goalWidthHalf
            && footballLocation.getX() < goalLocation.getX() + goalWidthHalf);
      case EAST:
        return (footballLocation.getX() < goalLocation.getX()
            && footballLocation.getZ() > goalLocation.getZ() - goalWidthHalf
            && footballLocation.getZ() < goalLocation.getZ() + goalWidthHalf);
      case SOUTH:
        return (footballLocation.getZ() < goalLocation.getZ()
            && footballLocation.getX() > goalLocation.getX() - goalWidthHalf
            && footballLocation.getX() < goalLocation.getX() + goalWidthHalf);
      case WEST:
        return (footballLocation.getX() > goalLocation.getX()
            && footballLocation.getZ() > goalLocation.getZ() - goalWidthHalf
            && footballLocation.getZ() < goalLocation.getZ() + goalWidthHalf);
      default:
        return false;
    }
  }

  public Location goalLocation() {
    return goalLocation;
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  public FootballGoalDirection direction() {
    return direction;
  }

  public static DefaultFootballGoal of(
      Location goalLocation, int width, int height, FootballGoalDirection direction) {
    Preconditions.checkNotNull(goalLocation);
    Preconditions.checkNotNull(direction);
    return new DefaultFootballGoal(goalLocation, width, height, direction);
  }

  enum FootballGoalDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;
  }
}
