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

import java.util.ArrayList;
import java.util.Arrays;


public class CancelGame extends CommandBase {
    public CancelGame() {
        super("cancel-game", false, true);
    }

    public static boolean cancelCommand(@NotNull Player player) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);

        NamespacedKey playersKey = new NamespacedKey(plugin, player.getUniqueId().toString());
        NamespacedKey hostsKey = new NamespacedKey(plugin, "hosts");
        NamespacedKey inGameKey = new NamespacedKey(plugin, "in_game");

        var pdc = player.getPersistentDataContainer();

        if (!pdc.has(playersKey)) {
            Msg.send(player, "&4You are not currently hosting a werewolf game!");
            return true;
        }

        var worldPdc = overworld.getPersistentDataContainer();
        var hosts = new ArrayList<>(Arrays.asList(worldPdc.get(hostsKey, DataType.PLAYER_ARRAY)));
        hosts.remove(player);
        if(hosts.isEmpty()) {
            worldPdc.remove(hostsKey);
        } else {
            worldPdc.set(hostsKey, DataType.PLAYER_ARRAY, hosts.toArray(new Player[0]));
        }

        for (var p : pdc.get(playersKey, DataType.PLAYER_ARRAY)) {
            p.getPersistentDataContainer().remove(inGameKey);
            Msg.send(p, "&c" + player.getName() + " has cancelled their game of werewolf!");
        }

        pdc.remove(playersKey);
        Msg.send(player, "&cYou have cancelled your game of werewolf!");

        return true;
    }

    @Override
    public boolean onCommand(@NotNull Player player) {
        return cancelCommand(player);
    }
}