package alian713.mc.mcwerewolf;

import alian713.mc.mcwerewolf.commands.CancelGame;
import alian713.mc.mcwerewolf.commands.LeaveGame;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        var plugin = McWerewolf.getInstance();
        var inGameKey = new NamespacedKey(plugin, "in_game");
        var roleKey = new NamespacedKey(plugin, "role");
        var playerUuid = player.getUniqueId().toString();
        var pdc = player.getPersistentDataContainer();

        if (!pdc.has(inGameKey) || pdc.has(roleKey)) {
            return;
        }

        var targetUuid = pdc.get(inGameKey, DataType.STRING);

        if (playerUuid.equals(targetUuid)) {
            CancelGame.onCancel(player);
        }

        LeaveGame.onLeave(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        var plugin = McWerewolf.getInstance();
        var inGameKey = new NamespacedKey(plugin, "in_game");
        var roleKey = new NamespacedKey(plugin, "role");
        var pdc = player.getPersistentDataContainer();

        if(!pdc.has(inGameKey)) {
            return;
        }

        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var hostsKey = new NamespacedKey(plugin, "hosts");
        if(!worldPdc.has(hostsKey)) {
            pdc.remove(inGameKey);
            pdc.remove(roleKey);
            return;
        }
        var hostUuid = pdc.get(inGameKey, DataType.STRING);
        var hostPlayerMap = worldPdc.get(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        if(!hostPlayerMap.containsKey(hostUuid)) {
            pdc.remove(inGameKey);
            pdc.remove(roleKey);
        }
    }
}