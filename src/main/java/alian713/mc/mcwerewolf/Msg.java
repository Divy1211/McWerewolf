package alian713.mc.mcwerewolf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class Msg {
    public static void send(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[MCW] &r"+msg));
    }
    public static void broadcast(Set<String> Uuids, String msg) {
        broadcast(Uuids, msg, false);
    }

    public static void broadcast(Set<String> Uuids, String msg, boolean clearKeys) {
        for(var uuid : Uuids) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if(p == null) {
                continue;
            }
            if(clearKeys) {
                PlayerListener.clearKeys(p.getPersistentDataContainer());
            }
            Msg.send(p, msg);
        }
    }
}