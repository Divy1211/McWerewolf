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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HostGame extends CommandBase {
    public HostGame() {
        super("host-game", false, true);
    }

    @Override
    public boolean onCommand(@NotNull Player player) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var hostsKey = new NamespacedKey(plugin, "hosts");
        Map<String, Set<String>> hostPlayerMap = new HashMap<>();
        var playerUuid = player.getUniqueId().toString();
        var inGameKey = new NamespacedKey(plugin, "in_game");

        var pdc = player.getPersistentDataContainer();

        if(pdc.has(inGameKey)) {
            Msg.send(player, "&4You are already in a werewolf game!");
            return true;
        }

        if (worldPdc.has(hostsKey)) {
            hostPlayerMap = worldPdc.get(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        }

        if(hostPlayerMap.containsKey(playerUuid)) {
            Msg.send(player, "&4You must finish/cancel the current game to be able to host another");
            return true;
        }

        Set<String> players = new HashSet<>();
        players.add(playerUuid);
        hostPlayerMap.put(playerUuid, players);

        pdc.set(inGameKey, DataType.STRING, playerUuid);
        worldPdc.set(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)), hostPlayerMap);
        Msg.send(player, "&aYou have hosted a new game of werewolf!");
        return true;
    }
}