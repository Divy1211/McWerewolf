package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ListGames extends CommandBase {
    public ListGames() {
        super("list-games", false, false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var hostsKey = new NamespacedKey(plugin, "hosts");
        var roleKey = new NamespacedKey(plugin, "role");
        Map<String, Set<String>> hostPlayerMap = new HashMap<>();

        if (worldPdc.has(hostsKey)) {
            hostPlayerMap = worldPdc.get(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        }

        if (hostPlayerMap.isEmpty()) {
            Msg.send(sender, "&bNo games of werewolf found, host one using /host-game!");
            return true;
        }

        Msg.send(sender, "&bThe following players currently have games:");
        for (var uuid : hostPlayerMap.keySet()) {
            Player host = Bukkit.getPlayer(UUID.fromString(uuid));
            if(host == null) {
                continue;
            }
            var started = host.getPersistentDataContainer().has(roleKey);
            Msg.send(sender, (started ? "&c" : "&a") + host.getName());
        }

        return true;
    }
}