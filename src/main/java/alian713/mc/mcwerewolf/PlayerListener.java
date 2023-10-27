package alian713.mc.mcwerewolf;

import alian713.mc.mcwerewolf.commands.CancelGame;
import alian713.mc.mcwerewolf.commands.LeaveGame;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        var plugin = McWerewolf.getInstance();
        var playerUuid = player.getUniqueId().toString();
        var pdc = player.getPersistentDataContainer();

        if (!pdc.has(plugin.IN_GAME_KEY) || pdc.has(plugin.ROLE_KEY)) {
            return;
        }

        var targetUuid = pdc.get(plugin.IN_GAME_KEY, DataType.STRING);

        if (playerUuid.equals(targetUuid)) {
            CancelGame.onCancel(player);
        }

        LeaveGame.onLeave(player);
    }

    public static void clearKeys(PersistentDataContainer pdc) {
        var plugin = McWerewolf.getInstance();
        pdc.remove(plugin.IN_GAME_KEY);
        pdc.remove(plugin.ROLE_KEY);
        pdc.remove(plugin.IS_ALIVE_KEY);
        pdc.remove(plugin.USED_ACTION_KEY);
        pdc.remove(plugin.IS_SAFE_KEY);
        pdc.remove(plugin.IS_EATEN_KEY);
        pdc.remove(plugin.IS_NOM_KEY);
        pdc.remove(plugin.HAS_VOTED);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        var plugin = McWerewolf.getInstance();
        var pdc = player.getPersistentDataContainer();

        if (!pdc.has(plugin.IN_GAME_KEY)) {
            clearKeys(pdc);
            return;
        }

        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        if (!worldPdc.has(plugin.HOSTS_KEY)) {
            clearKeys(pdc);
            return;
        }
        var hostUuid = pdc.get(plugin.IN_GAME_KEY, DataType.STRING);
        var hostPlayerMap = worldPdc.get(plugin.HOSTS_KEY, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        if (!hostPlayerMap.containsKey(hostUuid)) {
            clearKeys(pdc);
            return;
        }
        if (!hostPlayerMap.get(hostUuid).contains(player.getUniqueId().toString())) {
            clearKeys(pdc);
        }
    }
}