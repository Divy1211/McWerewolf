package alian713.mc.mcwerewolf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class Msg {
    public static void send(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
    public static void broadcast(Set<String> Uuids, String msg) {
        for(var uuid : Uuids) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if(p == null) {
                continue;
            }
            Msg.send(p, msg);
        }
    }
}