package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class CommandBase implements CommandExecutor {
    private final boolean playerArg;
    private final boolean playerOnly;

    protected CommandBase(@NotNull String name, boolean playerArg, boolean playerOnly) {
        this.playerArg = playerArg;
        this.playerOnly = playerOnly;

        var plugin = McWerewolf.getInstance();
        PluginCommand command = plugin.getCommand(name);
        if (command == null) {
            plugin.getLogger().warning(
                    "Attempted to instantiate a command '" + name + "' that has not been registered in plugin.yml"
            );
            return;
        }
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        if (playerOnly) {
            if (!(sender instanceof Player p)) {
                Msg.send(sender, "&cOnly a player can run this command");
                return true;
            }
            player = p;
        }

        if (!playerArg) {
            return playerOnly ? onCommand(player) : onCommand(sender);
        }

        switch (args.length) {
            case 0: {
                Msg.send(sender, "&cPlease specify a player to target");
                return true;
            }
            case 1: {
                var target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    Msg.send(sender, "&cPlayer not found");
                    return true;
                }
                return playerOnly ? onCommand(player, target) : onCommand(sender, target);
            }
            default: {
                Msg.send(sender, "&cIncorrect command usage");
                Bukkit.dispatchCommand(sender, "help " + label);
                return true;
            }
        }
    }

    public boolean onCommand(@NotNull Player player) {
        return false;
    }

    public boolean onCommand(@NotNull CommandSender sender) {
        return false;
    }

    public boolean onCommand(@NotNull Player player, @NotNull Player target) {
        return false;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Player target) {
        return false;
    }
}