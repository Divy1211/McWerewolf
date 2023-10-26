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

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class LeaveGame extends CommandBase {
    public LeaveGame() {
        super("leave-game", false, true);
    }

    public static boolean onLeave(@NotNull Player player) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var hostsKey = new NamespacedKey(plugin, "hosts");
        var inGameKey = new NamespacedKey(plugin, "in_game");
        var roleKey = new NamespacedKey(plugin, "role");
        var playerUuid = player.getUniqueId().toString();

        var pdc = player.getPersistentDataContainer();

        if (!pdc.has(inGameKey)) {
            Msg.send(player, "&4You are currently not in a werewolf game!");
            return true;
        }

        var targetUuid = pdc.get(inGameKey, DataType.STRING);

        if (playerUuid.equals(targetUuid)) {
            Msg.send(player, "&4You cannot leave your own game. Use /cancel-game if you wish to cancel the game!");
            return true;
        }

        Map<String, Set<String>> hostPlayerMap;
        hostPlayerMap = worldPdc.get(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));

        var players = hostPlayerMap.get(targetUuid);
        players.remove(playerUuid);
        for (String uuid : players) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            Msg.send(p, "&c" + player.getName() + " has left the game of werewolf!");
        }
        pdc.remove(inGameKey);
        pdc.remove(roleKey);
        worldPdc.set(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)), hostPlayerMap);
        Msg.send(player, "&cYou have left the game of werewolf!");
        return true;
    }

    @Override
    public boolean onCommand(@NotNull Player player) {
        return onLeave(player);
    }
}