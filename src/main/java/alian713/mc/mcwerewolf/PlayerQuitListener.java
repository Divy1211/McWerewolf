package alian713.mc.mcwerewolf;

import alian713.mc.mcwerewolf.commands.CancelGame;
import alian713.mc.mcwerewolf.commands.LeaveGame;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        var plugin = McWerewolf.getInstance();

        plugin.getLogger().info("Player has QUIT");

        var inGameKey = new NamespacedKey(plugin, "in_game");
        var playerUuid = player.getUniqueId().toString();
        var pdc = player.getPersistentDataContainer();

        if (!pdc.has(inGameKey)) {
            return;
        }

        var targetUuid = pdc.get(inGameKey, DataType.STRING);

        if (playerUuid.equals(targetUuid)) {
            CancelGame.onCancel(player);
        }

        LeaveGame.onLeave(player);
    }
}