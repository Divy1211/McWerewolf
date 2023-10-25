package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HostGame extends CommandBase {
    public HostGame() {
        super("host-game", false, true);
    }

    @Override
    public boolean onCommand(@NotNull Player player) {
        var plugin = McWerewolf.getInstance();
        NamespacedKey playersKey = new NamespacedKey(plugin, player.getUniqueId().toString());
        var pdc = player.getPersistentDataContainer();

        if(pdc.has(playersKey)) {
            Msg.send(player, "&cYou must finish/cancel the current game to be able to host another");
            return true;
        }

        pdc.set(playersKey, DataType.PLAYER_ARRAY, new Player[]{player});
        Msg.send(player, "&aYou have hosted a new game of werewolf!");
        return true;
    }
}