package de.dercoder.football.core;

public enum FootballPunishment {
  UNPUNISHED {
    @Override
    FootballPunishment punish(FootballPunishment footballPunishment) {
      return FootballPunishment.UNPUNISHED;
    }
  }, YELLOW_CARD {
    @Override
    FootballPunishment punish(FootballPunishment footballPunishment) {
      if (footballPunishment == FootballPunishment.UNPUNISHED) {
        return FootballPunishment.YELLOW_CARD;
      }
      return FootballPunishment.RED_CARD;
    }
  }, RED_CARD {
    @Override
    FootballPunishment punish(FootballPunishment footballPunishment) {
      return FootballPunishment.RED_CARD;
    }
  };

  abstract FootballPunishment punish(FootballPunishment footballPunishment);
}
