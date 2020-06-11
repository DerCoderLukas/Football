package de.dercoder.football.core;

public final class EmptyFootball implements Football {
  private EmptyFootball() {}

  public static EmptyFootball create() {
    return new EmptyFootball();
  }
}
