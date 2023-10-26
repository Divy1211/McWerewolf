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
        var playerUuid = player.getUniqueId().toString();
        var targetUuid = target.getUniqueId().toString();

        var pdc = player.getPersistentDataContainer();

        if(pdc.has(plugin.IN_GAME_KEY)) {
            Msg.send(player, "&4You are already in a werewolf game!");
            return true;
        }

        if(target.getPersistentDataContainer().has(plugin.ROLE_KEY)) {
            Msg.send(player, "&4This game has already started!");
            return true;
        }

        Map<String, Set<String>> hostPlayerMap = new HashMap<>();
        if (worldPdc.has(plugin.HOSTS_KEY)) {
            hostPlayerMap = worldPdc.get(plugin.HOSTS_KEY, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        }

        if (!hostPlayerMap.containsKey(targetUuid)) {
            Msg.send(player, "&4That player is not currently hosting a werewolf game!");
            return true;
        }

        var players = hostPlayerMap.get(targetUuid);
        if(players.contains(playerUuid)) {
            Msg.send(player, "&4You are already in " + target.getName() + "'s game of werewolf!");
            return true;
        }
        Msg.broadcast(players, "&a" + player.getName() + " has joined the game of werewolf!");

        players.add(playerUuid);
        pdc.set(plugin.IN_GAME_KEY, DataType.STRING, targetUuid);
        worldPdc.set(plugin.HOSTS_KEY, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)), hostPlayerMap);
        Msg.send(player, "&aYou have joined " + target.getName() + "'s game of werewolf!");
        return true;
    }
}