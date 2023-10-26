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


public class CancelGame extends CommandBase {
    public CancelGame() {
        super("cancel-game", false, true);
    }

    public static boolean onCancel(@NotNull Player player) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var hostsKey = new NamespacedKey(plugin, "hosts");
        var inGameKey = new NamespacedKey(plugin, "in_game");
        var roleKey = new NamespacedKey(plugin, "role");
        var playerUuid = player.getUniqueId().toString();

        if (!worldPdc.has(hostsKey)) {
            Msg.send(player, "&4You are not currently hosting a werewolf game!");
            return true;
        }

        Map<String, Set<String>> hostPlayerMap;
        hostPlayerMap = worldPdc.get(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));

        if (!hostPlayerMap.containsKey(playerUuid)) {
            Msg.send(player, "&4You are not currently hosting a werewolf game!");
            return true;
        }

        var players = hostPlayerMap.get(playerUuid);
        Msg.broadcast(players, "&c" + player.getName() + " has cancelled their game of werewolf!");

        hostPlayerMap.remove(playerUuid);

        var pdc = player.getPersistentDataContainer();
        pdc.remove(inGameKey);
        pdc.remove(roleKey);

        worldPdc.set(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)), hostPlayerMap);
        Msg.send(player, "&cYou have cancelled your game of werewolf!");
        return true;
    }

    @Override
    public boolean onCommand(@NotNull Player player) {
        return onCancel(player);
    }
}