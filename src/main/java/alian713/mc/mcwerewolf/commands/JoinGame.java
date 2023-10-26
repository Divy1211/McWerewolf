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

public class JoinGame extends CommandBase {
    public JoinGame() {
        super("join-game", true, true);
    }

    @Override
    public boolean onCommand(@NotNull Player player, @NotNull Player target) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var hostsKey = new NamespacedKey(plugin, "hosts");
        var inGameKey = new NamespacedKey(plugin, "in_game");
        var playerUuid = player.getUniqueId().toString();
        var targetUuid = target.getUniqueId().toString();

        var pdc = player.getPersistentDataContainer();

        if(pdc.has(inGameKey)) {
            Msg.send(player, "&4You are already in a werewolf game!");
            return true;
        }

        if (!worldPdc.has(hostsKey)) {
            Msg.send(player, "&4That player is not currently hosting a werewolf game!");
            return true;
        }

        Map<String, Set<String>> hostPlayerMap;
        hostPlayerMap = worldPdc.get(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));

        if (!hostPlayerMap.containsKey(targetUuid)) {
            Msg.send(player, "&4That player is not currently hosting a werewolf game!");
            return true;
        }

        var players = hostPlayerMap.get(targetUuid);
        if(players.contains(playerUuid)) {
            Msg.send(player, "&4You are already in " + target.getName() + "'s game of werewolf!");
            return true;
        }
        for(String uuid : players) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            Msg.send(p, "&a" + player.getName() + " has joined the game of werewolf!");
        }

        players.add(playerUuid);
        pdc.set(inGameKey, DataType.STRING, targetUuid);
        worldPdc.set(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)), hostPlayerMap);
        Msg.send(player, "&aYou have joined " + target.getName() + "'s game of werewolf!");
        return true;
    }
}