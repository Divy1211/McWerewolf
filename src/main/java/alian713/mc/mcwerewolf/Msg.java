package alian713.mc.mcwerewolf;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Msg {
    public static void send(CommandSender sender, String str) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
    }
}