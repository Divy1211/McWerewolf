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

import java.util.*;

public class DayPhase extends CommandBase {
    public DayPhase() {
        super("day-phase", false, true);
    }

    @Override
    public boolean onCommand(@NotNull Player player) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var playerUuid = player.getUniqueId().toString();

        Map<String, Set<String>> hostPlayerMap = new HashMap<>();
        if (worldPdc.has(plugin.HOSTS_KEY)) {
            hostPlayerMap = worldPdc.get(plugin.HOSTS_KEY, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        }

        if(!hostPlayerMap.containsKey(playerUuid)) {
            Msg.send(player, "&4You have not hosted a werewolf game!");
            return true;
        }

        var pdc = player.getPersistentDataContainer();

        if(!pdc.has(plugin.DAY_KEY)) {
            Msg.send(player, "&4Your game has not started yet!");
            return true;
        }

        var day = pdc.get(plugin.DAY_KEY, DataType.BOOLEAN);
        if(day) {
            Msg.send(player, "&4It is already day time!");
            return true;
        }

        pdc.set(plugin.DAY_KEY, DataType.BOOLEAN, true);
        Msg.broadcast(hostPlayerMap.get(playerUuid), "&aIt is now day time! Discuss");
        return true;
    }
}