package de.dercoder.football.bukkit;

import com.google.common.collect.Sets;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.dercoder.football.bukkit.football.DefaultFootballFactory;
import de.dercoder.football.bukkit.game.FootballGame;
import de.dercoder.football.bukkit.game.FootballGameFactory;
import de.dercoder.football.bukkit.game.FootballGameRegistry;
import de.dercoder.football.bukkit.goal.DefaultFootballGoal;
import de.dercoder.football.bukkit.stadium.FootballStadium;
import de.dercoder.football.bukkit.stadium.FootballStadiumRepository;
import de.dercoder.football.core.FootballMatch;
import de.dercoder.football.core.FootballPlayer;
import de.dercoder.football.core.FootballTeam;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class FootballCommand implements CommandExecutor {
  private final String prefix;
  private final FootballStadiumRepository footballStadiumRepository;
  private final FootballGameRegistry footballGameRegistry;
  private final DefaultFootballFactory defaultFootballFactory;
  private final FootballGameFactory footballGameFactory;

  @Inject
  private FootballCommand(
    @Named("prefix") String prefix,
    FootballStadiumRepository footballStadiumRepository,
    FootballGameRegistry footballGameRegistry,
    DefaultFootballFactory defaultFootballFactory,
    FootballGameFactory footballGameFactory
  ) {
    this.prefix = prefix;
    this.footballStadiumRepository = footballStadiumRepository;
    this.footballGameRegistry = footballGameRegistry;
    this.defaultFootballFactory = defaultFootballFactory;
    this.footballGameFactory = footballGameFactory;
  }

  private static final String FOOTBALL_COMMAND_PERMISSION = "football.command";

  @Override
  public boolean onCommand(
    CommandSender commandSender,
    Command command,
    String label,
    String[] arguments
  ) {
    if (!commandSender.hasPermission(FOOTBALL_COMMAND_PERMISSION)) {
      commandSender.sendMessage(command.getPermissionMessage());
      return false;
    }
    if (arguments.length > 0) {
      var keyword = arguments[0];
      if (keyword.equalsIgnoreCase("stadium")) {
        return onFootballStadium(commandSender, command, arguments);
      } else if (keyword.equalsIgnoreCase("game")) {
        return onFootballGame(commandSender, command, arguments);
      } else {
        return onFootballOverview(commandSender, command);
      }
    } else {
      return onFootballOverview(commandSender, command);
    }
  }

  private boolean onFootballOverview(
    CommandSender commandSender,
    Command command
  ) {
    sendPrefixMessage(commandSender, "Football:");
    sendPrefixMessage(commandSender, "  - /football stadium");
    sendPrefixMessage(commandSender, "  - /football game");
    return true;
  }

  private boolean onFootballStadium(
    CommandSender commandSender, Command command, String[] arguments
  ) {
    if (arguments.length > 1) {
      var keyword = arguments[1];
      if (keyword.equalsIgnoreCase("list")) {
        return onFootballStadiumList(commandSender, command);
      } else if (keyword.equalsIgnoreCase("create")) {
        return onFootballStadiumCreate(commandSender, command, arguments);
      } else if (keyword.equalsIgnoreCase("goal")) {
        return onFootballStadiumGoal(commandSender, command, arguments);
      } else if (keyword.equalsIgnoreCase("info")) {
        return onFootballStadiumInfo(commandSender, command, arguments);
      } else if (keyword.equalsIgnoreCase("delete")) {
        return onFootballStadiumDelete(commandSender, command, arguments);
      } else {
        return onFootballStadiumOverview(commandSender, command);
      }
    } else {
      return onFootballStadiumOverview(commandSender, command);
    }
  }

  private boolean onFootballStadiumList(
    CommandSender commandSender,
    Command command
  ) {
    var footballStadiums = footballStadiumRepository.findAll();
    sendPrefixMessage(
      commandSender,
      "Football-Stadiums: §e" + footballStadiums.size()
    );
    sendPrefixMessage(commandSender, "Football-Stadium-List:");
    for ( var footballStadium : footballStadiums ) {
      sendPrefixMessage(commandSender, "  - " + footballStadium.name());
    }
    return true;
  }

  private boolean onFootballStadiumCreate(
    CommandSender commandSender, Command command, String[] arguments
  ) {
    if (!(commandSender instanceof Player)) {
      sendPrefixMessage(commandSender, "You need to be a player.");
      return false;
    }
    if (arguments.length == 3) {
      var footballStadiumName = arguments[2];
      footballStadiumRepository.find(footballStadiumName)
        .ifPresentOrElse(footballStadium -> {
          sendPrefixMessage(
            commandSender,
            "There is already a stadium with that name."
          );
        }, () -> {
          var player = (Player) commandSender;
          var playerLocation = player.getLocation();
          var footballGoals = new DefaultFootballGoal[0];
          var footballStadium = FootballStadium.of(
            footballStadiumName,
            playerLocation,
            footballGoals
          );
          footballStadiumRepository.register(footballStadium);
          sendPrefixMessage(
            commandSender,
            "You successfully added a new stadium."
          );
        });
    } else {
      sendSyntaxCorrection(commandSender, "/football stadium create <name>");
      return false;
    }
    return true;
  }

  private boolean onFootballStadiumGoal(
    CommandSender commandSender, Command command, String[] arguments
  ) {
    if (!(commandSender instanceof Player)) {
      sendPrefixMessage(commandSender, "You need to be a player.");
      return false;
    }
    if (arguments.length == 6) {
      var footballStadiumName = arguments[2];
      var widthString = arguments[3];
      var heightString = arguments[4];
      var directionString = arguments[5];
      var patternCompiler = Pattern.compile("-?[0-9]+");
      if (patternCompiler.matcher(widthString)
        .matches() && patternCompiler.matcher(heightString).matches()) {
        var width = Integer.parseInt(widthString);
        var height = Integer.parseInt(heightString);
        footballStadiumRepository.find(footballStadiumName)
          .ifPresentOrElse(footballStadium -> {
            try {
              var direction = DefaultFootballGoal.FootballGoalDirection.valueOf(
                directionString.toUpperCase());
              var player = (Player) commandSender;
              var playerLocation = player.getLocation();
              var footballGoal = DefaultFootballGoal.of(
                playerLocation,
                width,
                height,
                direction
              );
              footballStadium.addFootballGoal(footballGoal);
              sendPrefixMessage(
                commandSender,
                "You successfully added a new goal to the stadium."
              );
            } catch (Exception exception) {
              sendPrefixMessage(
                commandSender,
                "Can't find the direction " + directionString + "."
              );
            }
          }, () -> {
            sendPrefixMessage(
              commandSender,
              "Can't find any stadium with that name."
            );
          });
      } else {
        sendPrefixMessage(commandSender, "Please provide a real number.");
        return false;
      }
    } else {
      sendSyntaxCorrection(
        commandSender,
        "/football stadium goal <stadium name> <width> <height> <direction>"
      );
      return false;
    }
    return true;
  }

  private boolean onFootballStadiumInfo(
    CommandSender commandSender, Command command, String[] arguments
  ) {
    if (arguments.length == 3) {
      var footballStadiumName = arguments[2];
      footballStadiumRepository.find(footballStadiumName)
        .ifPresentOrElse(footballStadium -> {
          sendPrefixMessage(
            commandSender,
            "Football-Stadium-" + footballStadiumName + ":"
          );
          sendPrefixMessage(commandSender, "  - Name: " + footballStadiumName);
          var footballStadiumCenterLocation = footballStadium.stadiumCenter();
          sendPrefixMessage(commandSender, "  - Center:");
          sendPrefixMessage(
            commandSender,
            "      - X: " + footballStadiumCenterLocation.getX()
          );
          sendPrefixMessage(
            commandSender,
            "      - Y: " + footballStadiumCenterLocation.getY()
          );
          sendPrefixMessage(
            commandSender,
            "      - Z: " + footballStadiumCenterLocation.getZ()
          );
          sendPrefixMessage(
            commandSender,
            "  - Goals: " + footballStadium.footballGoals().length
          );
        }, () -> {
          sendPrefixMessage(
            commandSender,
            "Can't find any stadium with that name."
          );
        });
    } else {
      sendSyntaxCorrection(commandSender, "/football stadium info <name>");
      return false;
    }
    return false;
  }

  private boolean onFootballStadiumDelete(
    CommandSender commandSender, Command command, String[] arguments
  ) {
    if (arguments.length == 3) {
      var footballStadiumName = arguments[2];
      footballStadiumRepository.find(footballStadiumName)
        .ifPresentOrElse(footballStadium -> {
          footballStadiumRepository.unregister(footballStadium);
          sendPrefixMessage(
            commandSender,
            "You successfully removed the stadium."
          );
        }, () -> {
          sendPrefixMessage(
            commandSender,
            "Can't find any stadium with that name."
          );
        });
    } else {
      sendSyntaxCorrection(commandSender, "/football stadium delete <name>");
      return false;
    }
    return true;
  }

  private boolean onFootballStadiumOverview(
    CommandSender commandSender,
    Command command
  ) {
    sendPrefixMessage(commandSender, "Football-Stadium:");
    sendPrefixMessage(commandSender, "  - /football stadium list");
    sendPrefixMessage(commandSender, "  - /football stadium create <name>");
    sendPrefixMessage(
      commandSender,
      "  - /football stadium goal <stadium name> <width> <height> <direction>"
    );
    sendPrefixMessage(commandSender, "  - /football stadium info <name>");
    sendPrefixMessage(commandSender, "  - /football stadium delete <name>");
    return true;
  }

  private boolean onFootballGame(
    CommandSender commandSender,
    Command command,
    String[] arguments
  ) {
    if (arguments.length > 1) {
      var keyword = arguments[1];
      if (keyword.equalsIgnoreCase("list")) {
        return onFootballGameList(commandSender, command);
      } else if (keyword.equalsIgnoreCase("create")) {
        return onFootballGameCreate(commandSender, command, arguments);
      } else if (keyword.equalsIgnoreCase("info")) {
        return onFootballGameInfo(commandSender, command, arguments);
      } else if (keyword.equalsIgnoreCase("stop")) {
        return onFootballGameStop(commandSender, command, arguments);
      } else {
        return onFootballGameOverview(commandSender, command);
      }
    } else {
      return onFootballGameOverview(commandSender, command);
    }
  }

  private boolean onFootballGameList(
    CommandSender commandSender,
    Command command
  ) {
    var footballGames = footballGameRegistry.footballGames();
    sendPrefixMessage(
      commandSender,
      "Football-Games: §e" + footballGames.size()
    );
    sendPrefixMessage(commandSender, "Football-Game-List:");
    for ( var footballGame : footballGames ) {
      sendPrefixMessage(
        commandSender,
        "  - " + footballGame.footballStadium().name()
      );
    }
    return true;
  }

  private boolean onFootballGameCreate(
    CommandSender commandSender, Command command, String[] arguments
  ) {
    if (arguments.length == 5) {
      var footballStadiumName = arguments[2];
      var firstTeamPlayers = arguments[3];
      var secondTeamPlayers = arguments[4];
      footballStadiumRepository.find(footballStadiumName)
        .ifPresentOrElse(footballStadium -> {
          if (footballGameRegistry.findGameByStadium(footballStadium)
            .isEmpty()) {
            var footballGoals = footballStadium.footballGoals();
            var firstTeamOptional = parseFootballTeam(
              firstTeamPlayers,
              footballGoals[0]
            );
            var secondTeamOptional = parseFootballTeam(
              secondTeamPlayers,
              footballGoals[1]
            );
            if (firstTeamOptional.isEmpty() || secondTeamOptional.isEmpty()) {
              sendPrefixMessage(
                commandSender,
                "One of the provided players is not online."
              );
            }
            var firstTeam = firstTeamOptional.get();
            var secondTeam = secondTeamOptional.get();
            var football = defaultFootballFactory.createDefaultFootball(
              footballStadium.stadiumCenter());
            var footballMatch = FootballMatch.empty(football, footballGoals);
            var footballTeams = new FootballTeam[]{firstTeam, secondTeam};
            footballMatch.fillMatch(footballTeams);
            footballGameFactory.createFootballGame(
              footballMatch,
              footballStadium
            );
            sendPrefixMessage(commandSender,
              "You successfully created a new football game at " + "the stadium " + footballStadiumName + "."
            );
          } else {
            sendPrefixMessage(
              commandSender,
              "There is already a football game running to that stadium."
            );
          }
        }, () -> {
          sendPrefixMessage(
            commandSender,
            "Can't find any stadium with that name."
          );
        });
    } else {
      sendSyntaxCorrection(
        commandSender,
        "/football game create <stadium> <players of the first team> " + "<players of the second team> (Players-Format: DerCoder;notch;...)"
      );
      return false;
    }
    return true;
  }

  private Optional<FootballTeam> parseFootballTeam(
    String teamPlayerNames, DefaultFootballGoal footballGoal
  ) {
    var teamPlayerArray = teamPlayerNames.split(";");
    Set<FootballPlayer> players = Sets.newHashSet();
    for ( var teamPlayerName : teamPlayerArray ) {
      var player = Bukkit.getPlayer(teamPlayerName);
      if (player == null) {
        return Optional.empty();
      }
      var teamPlayer = FootballPlayer.withId(player.getUniqueId());
      players.add(teamPlayer);
    }
    var footballTeam = FootballTeam.of(players, footballGoal, 0);
    return Optional.of(footballTeam);
  }

  private boolean onFootballGameInfo(
    CommandSender commandSender, Command command, String[] arguments
  ) {
    if (arguments.length == 3) {
      var footballStadiumName = arguments[2];
      footballStadiumRepository.find(footballStadiumName)
        .ifPresentOrElse(footballStadium -> {
          footballGameRegistry.findGameByStadium(footballStadium)
            .ifPresentOrElse(footballGame -> {
              sendPrefixMessage(
                commandSender,
                "Football-Game-" + footballStadiumName + ":"
              );
              sendPrefixMessage(
                commandSender,
                "  - Stadium: " + footballStadiumName
              );
              sendPrefixMessage(commandSender, "  - Teams: ");
              var footballTeams = footballGame.footballTeams();
              displayFootballTeam(
                commandSender,
                footballGame,
                footballTeams[0],
                "First"
              );
              displayFootballTeam(
                commandSender,
                footballGame,
                footballTeams[1],
                "Second"
              );
            }, () -> {
              sendPrefixMessage(
                commandSender,
                "Can't find any football game at that stadium."
              );
            });
        }, () -> {
          sendPrefixMessage(
            commandSender,
            "Can't find any stadium with that name."
          );
        });
    } else {
      sendSyntaxCorrection(commandSender, "/football game info <stadium>");
      return false;
    }
    return true;
  }

  private void displayFootballTeam(
    CommandSender commandSender,
    FootballGame footballGame,
    FootballTeam footballTeam,
    String teamName
  ) {
    sendPrefixMessage(commandSender, "      - " + teamName + ":");
    var players = footballGame.findTeamPlayers(footballTeam)
      .collect(Collectors.toUnmodifiableList());
    for ( var player : players ) {
      sendPrefixMessage(commandSender, "          - " + player.getName());
    }
  }

  private boolean onFootballGameStop(
    CommandSender commandSender, Command command, String[] arguments
  ) {
    if (arguments.length == 3) {
      var footballStadiumName = arguments[2];
      footballStadiumRepository.find(footballStadiumName)
        .ifPresentOrElse(footballStadium -> {
          footballGameRegistry.findGameByStadium(footballStadium)
            .ifPresentOrElse(footballGame -> {
              footballGame.close();
              sendPrefixMessage(
                commandSender,
                "You successful stopped the football game."
              );
            }, () -> {
              sendPrefixMessage(
                commandSender,
                "Can't find any football game at that stadium."
              );
            });
        }, () -> {
          sendPrefixMessage(
            commandSender,
            "Can't find any stadium with that name."
          );
        });
    } else {
      sendSyntaxCorrection(commandSender, "/football game stop <stadium>");
      return false;
    }
    return true;
  }

  private boolean onFootballGameOverview(
    CommandSender commandSender,
    Command command
  ) {
    sendPrefixMessage(commandSender, "Football-Game:");
    sendPrefixMessage(commandSender, "  - /football game list");
    sendPrefixMessage(
      commandSender,
      "  - /football game create <stadium> <players of the first team> " + "<players of the second team> (Players-Format: DerCoder;notch;...)"
    );
    sendPrefixMessage(commandSender, "  - /football game info <stadium>");
    sendPrefixMessage(commandSender, "  - /football game stop <stadium>");
    return true;
  }

  private void sendPrefixMessage(CommandSender commandSender, String message) {
    commandSender.sendMessage(prefix + message);
  }

  private void sendSyntaxCorrection(
    CommandSender commandSender,
    String correction
  ) {
    commandSender.sendMessage(prefix + "Syntax: " + correction);
  }
}
