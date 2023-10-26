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

public class HostGame extends CommandBase {
    public HostGame() {
        super("host-game", false, true);
    }

    @Override
    public boolean onCommand(@NotNull Player player) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        NamespacedKey playersKey = new NamespacedKey(plugin, player.getUniqueId().toString());
        NamespacedKey hostsKey = new NamespacedKey(plugin, "hosts");

        var pdc = player.getPersistentDataContainer();

        if (pdc.has(playersKey)) {
            Msg.send(player, "&4You must finish/cancel the current game to be able to host another");
            return true;
        }

        Player[] hosts = new Player[]{player};

        var worldPdc = overworld.getPersistentDataContainer();
        worldPdc.remove(hostsKey);
        if (worldPdc.has(hostsKey)) {
            hosts = worldPdc.get(hostsKey, DataType.PLAYER_ARRAY);
            var ls = new ArrayList<>(Arrays.asList(hosts));
            ls.add(player);
            hosts = ls.toArray(new Player[0]);
        }

        worldPdc.set(hostsKey, DataType.PLAYER_ARRAY, hosts);

        pdc.set(playersKey, DataType.PLAYER_ARRAY, new Player[]{player});
        Msg.send(player, "&aYou have hosted a new game of werewolf!");
        return true;
    }
}