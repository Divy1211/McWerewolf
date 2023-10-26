package alian713.mc.mcwerewolf.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class CancelGameOther extends CommandBase {
    public CancelGameOther() {
        super("cancel-game-other", true, false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Player target) {
        return CancelGame.cancelCommand(target);
    }
}