package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Lynch extends CommandBase {
    public Lynch() {
        super("lynch", true, true);
    }

    @Override
    public boolean onCommand(@NotNull Player player, @NotNull Player target) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var playerUuid = player.getUniqueId().toString();
        var targetUuid = target.getUniqueId().toString();

        var pdc = player.getPersistentDataContainer();

        if (!pdc.has(plugin.ROLE_KEY)) {
            Msg.send(player, "&4You are not in an active werewolf game!");
            return true;
        }

        if (!pdc.get(plugin.IS_ALIVE_KEY, DataType.BOOLEAN)) {
            Msg.send(player, "&4Dead players cannot vote to lynch!");
            return true;
        }

        if (playerUuid.equals(targetUuid)) {
            Msg.send(player, "&4Why in gods name are you voting to lynch yourself?");
            return true;
        }

        Map<String, Boolean> phaseMap = new HashMap<>();
        if (worldPdc.has(plugin.DAY_KEY)) {
            phaseMap = worldPdc.get(plugin.DAY_KEY, DataType.asMap(DataType.STRING, DataType.BOOLEAN));
        }

        var hostUuid = pdc.get(plugin.IN_GAME_KEY, DataType.STRING);
        var day = phaseMap.get(hostUuid);
        if (!day) {
            Msg.send(player, "&4You cannot lynch people during the night!");
            return true;
        }

        if (pdc.get(plugin.HAS_VOTED, DataType.BOOLEAN)) {
            Msg.send(player, "&4You can only vote to lynch one person per day!");
            return true;
        }

        var targetPdc = target.getPersistentDataContainer();

        if(!targetPdc.get(plugin.IS_ALIVE_KEY, DataType.BOOLEAN)) {
            Msg.send(player, "&4That player is already dead!");
            return true;
        }

        var votes = targetPdc.get(plugin.VOTE_COUNT_KEY, DataType.INTEGER);
        targetPdc.set(plugin.VOTE_COUNT_KEY, DataType.INTEGER, votes + 1);

        pdc.set(plugin.HAS_VOTED, DataType.BOOLEAN, true);
        Msg.send(player, "&cYou've voted to lynch " + target.getName() + "!");
        return true;
    }
}