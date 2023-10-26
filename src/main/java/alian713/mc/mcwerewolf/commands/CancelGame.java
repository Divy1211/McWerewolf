package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;


public class CancelGame extends CommandBase {
    public CancelGame() {
        super("cancel-game", false, true);
    }

    public static boolean onCancel(@NotNull Player player) {
        return onCancel(player, false);
    }

    public static boolean onCancel(@NotNull Player player, boolean fin) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var playerUuid = player.getUniqueId().toString();

        if (!worldPdc.has(plugin.HOSTS_KEY)) {
            Msg.send(player, "&4You are not currently hosting a werewolf game!");
            return true;
        }

        Map<String, Set<String>> hostPlayerMap;
        hostPlayerMap = worldPdc.get(plugin.HOSTS_KEY, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));

        if (!hostPlayerMap.containsKey(playerUuid)) {
            Msg.send(player, "&4You are not currently hosting a werewolf game!");
            return true;
        }

        var players = hostPlayerMap.get(playerUuid);
        if(!fin) {
            Msg.broadcast(players, "&c" + player.getName() + " has cancelled their game of werewolf!", true);
        }

        hostPlayerMap.remove(playerUuid);

        worldPdc.set(plugin.HOSTS_KEY, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)), hostPlayerMap);
        if(!fin){
            Msg.send(player, "&cYou have cancelled your game of werewolf!");
        }
        return true;
    }

    @Override
    public boolean onCommand(@NotNull Player player) {
        return onCancel(player);
    }
}