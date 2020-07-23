package de.dercoder.football.bukkit.game;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

import de.dercoder.football.bukkit.FootballPlugin;
import de.dercoder.football.bukkit.configuration.FootballConfiguration;
import de.dercoder.football.bukkit.football.DefaultFootball;
import de.dercoder.football.bukkit.goal.DefaultFootballGoal;
import de.dercoder.football.bukkit.stadium.FootballStadium;
import de.dercoder.football.core.FootballMatch;
import de.dercoder.football.core.FootballTeam;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class FootballGame {
  private final FootballMatch footballMatch;
  private final FootballStadium footballStadium;
  private final FootballGameRegistry footballGameRegistry;
  private final FootballConfiguration footballConfiguration;
  private int footballGameTask;

  private FootballGame(
    FootballMatch footballMatch,
    FootballStadium footballStadium,
    FootballGameRegistry footballGameRegistry,
    FootballConfiguration footballConfiguration
  ) {
    this.footballMatch = footballMatch;
    this.footballStadium = footballStadium;
    this.footballGameRegistry = footballGameRegistry;
    this.footballConfiguration = footballConfiguration;
  }

  public void start() {
    footballGameTask = Bukkit.getScheduler()
      .scheduleAsyncRepeatingTask(FootballPlugin.getPlugin(FootballPlugin.class),
        () -> {
          sendIntermediateTitles();
        },
        0,
        20
      );
  }

  public void handleGoalScore() {
    var football = football();
    Arrays.stream(footballMatch.goals())
      .map(DefaultFootballGoal.class::cast)
      .filter(defaultFootballGoal -> defaultFootballGoal.isInGoal(football))
      .findFirst()
      .ifPresent(this::scoreGoal);
  }

  private void scoreGoal(DefaultFootballGoal footballGoal) {
    var football = football();
    var shooter = football.shooter();
    shooter.shootAGoal();
    footballMatch.findTeamOfGoal(footballGoal)
      .ifPresent(FootballTeam::shootAGoal);
    sendGoalTitles();
    football.reset();
    resetTeams();
  }

  public void resetTeams() {
    footballMatch.teams().ifPresent(footballTeams -> {
      teleportTeam(footballTeams[0]);
      teleportTeam(footballTeams[1]);
    });
  }

  private void teleportTeam(FootballTeam footballTeam) {
    var footballGoalLocation = ((DefaultFootballGoal) footballTeam.footballGoal())
      .goalLocation();
    var players = findTeamPlayers(footballTeam).collect(Collectors.toUnmodifiableList());
    for ( var player : players ) {
      player.teleport(footballGoalLocation);
    }
  }

  private void sendIntermediateTitles() {
    var teams = footballMatch.teams().get();
    var intermediateScoreMessage = new TextComponent(footballConfiguration.parseIntermediateScoreMessage(teams[0].goals(),
      teams[1].goals()
    ));
    var players = findAllPlayers().collect(Collectors.toUnmodifiableList());
    for ( var player : players ) {
      player.spigot()
        .sendMessage(ChatMessageType.ACTION_BAR, intermediateScoreMessage);
    }
  }

  private void sendGoalTitles() {
    var teams = footballMatch.teams().get();
    var scorerName = Bukkit.getPlayer(football().shooter().id()).getName();
    var scoreMainMessage = footballConfiguration.parseScoreMainMessage(scorerName,
      teams[0].goals(),
      teams[1].goals()
    );
    var scoreSecondaryMessage = footballConfiguration.parseScoreSecondaryMessage(scorerName,
      teams[0].goals(),
      teams[1].goals()
    );
    var players = findAllPlayers().collect(Collectors.toUnmodifiableList());
    for ( var player : players ) {
      player.sendTitle(scoreMainMessage, scoreSecondaryMessage, 5, 40, 5);
    }
  }

  public Stream<Player> findTeamPlayers(FootballTeam footballTeam) {
    Preconditions.checkNotNull(footballTeam);
    return footballTeam.players()
      .stream()
      .map(footballPlayer -> Bukkit.getPlayer(footballPlayer.id()))
      .filter(Objects::nonNull);
  }

  public Stream<Player> findAllPlayers() {
    return footballMatch.players()
      .stream()
      .map(footballPlayerSession -> Bukkit.getPlayer(footballPlayerSession.player()
        .id()))
      .filter(Objects::nonNull);
  }

  public void close() {
    Bukkit.getScheduler().cancelTask(footballGameTask);
    football().stopTriggering();
    football().despawn();
    footballGameRegistry.unregister(this);
  }

  public FootballMatch footballMatch() {
    return footballMatch;
  }

  public DefaultFootball football() {
    return footballMatch.<DefaultFootball>football();
  }

  public FootballTeam[] footballTeams() {
    return footballMatch.teams().get();
  }

  public FootballStadium footballStadium() {
    return footballStadium;
  }

  public static FootballGame of(
    FootballMatch footballMatch,
    FootballStadium footballStadium,
    FootballGameRegistry footballGameRegistry,
    FootballConfiguration footballConfiguration
  ) {
    Preconditions.checkNotNull(footballMatch);
    Preconditions.checkNotNull(footballStadium);
    Preconditions.checkNotNull(footballGameRegistry);
    Preconditions.checkNotNull(footballConfiguration);
    var footballGame = new FootballGame(footballMatch,
      footballStadium,
      footballGameRegistry,
      footballConfiguration
    );
    footballGame.start();
    footballGame.resetTeams();
    return footballGame;
  }
}
