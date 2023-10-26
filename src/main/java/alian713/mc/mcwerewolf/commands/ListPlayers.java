package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ListPlayers extends CommandBase {
    public ListPlayers() {
        super("list-players", true, true);
    }

    @Override
    public boolean onCommand(@NotNull Player player, @NotNull Player target) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var hostsKey = new NamespacedKey(plugin, "hosts");
        var targetUuid = target.getUniqueId().toString();

        Map<String, Set<String>> hostPlayerMap = new HashMap<>();
        if (worldPdc.has(hostsKey)) {
            hostPlayerMap = worldPdc.get(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        }

        if (!hostPlayerMap.containsKey(targetUuid)) {
            Msg.send(player, "&4That player is not currently hosting a werewolf game!");
            return true;
        }

        Msg.send(player, "&bThe following players are in "+target.getName()+"'s game of werewolf:");
        for (var p : hostPlayerMap.get(targetUuid)) {
            Msg.send(player, "&3" + Bukkit.getPlayer(UUID.fromString(p)).getName());
        }
        return true;
    }
}