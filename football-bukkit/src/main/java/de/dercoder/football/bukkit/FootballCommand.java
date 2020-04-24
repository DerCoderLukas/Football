package de.dercoder.football.bukkit;

import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class FootballCommand implements CommandExecutor {
    private final FootballGame footballGame;

    @Inject
    private FootballCommand(
            FootballGame footballGame
    ) {
        this.footballGame = footballGame;
    }

    private static final String FOOTBALL_COMMAND_PERMISSION = "football.command";

    @Override
    public boolean onCommand(
            CommandSender commandSender,
            Command command,
            String label,
            String[] arugments
    ) {
        if (!commandSender.hasPermission(FOOTBALL_COMMAND_PERMISSION)) {
            commandSender.sendMessage(command.getPermissionMessage());
            return false;
        }
        if (arugments.length > 0) {
            var keyword = arugments[0];
            if (keyword.equalsIgnoreCase("stadium")) {
                return onStadiumSetting(commandSender, command, arugments);
            }
        } else {

        }
        return false;
    }

    public boolean onStadiumSetting(
            CommandSender commandSender,
            Command command,
            String[] arguments
    ) {
        return true;
    }
}
