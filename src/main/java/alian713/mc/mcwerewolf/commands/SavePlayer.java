package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import alian713.mc.mcwerewolf.Role;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SavePlayer extends CommandBase {
    public SavePlayer() {
        super("save-player", true, true);
    }

    @Override
    public boolean onCommand(@NotNull Player player, @NotNull Player target) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var playerUuid = player.getUniqueId().toString();
        var targetUuid = target.getUniqueId().toString();

        var pdc = player.getPersistentDataContainer();

        if(!pdc.has(plugin.ROLE_KEY)) {
            Msg.send(player, "&4You are not in an active werewolf game!");
            return true;
        }

        var role = pdc.get(plugin.ROLE_KEY, DataType.STRING);
        if(!role.equals(Role.MEDIC)) {
            Msg.send(player, "&4You are not the Medic!");
            return true;
        }

        if(playerUuid.equals(targetUuid)) {
            Msg.send(player, "&4You cannot save yourself!");
            return true;
        }

        Map<String, Boolean> phaseMap = new HashMap<>();
        if (worldPdc.has(plugin.DAY_KEY)) {
            phaseMap = worldPdc.get(plugin.DAY_KEY, DataType.asMap(DataType.STRING, DataType.BOOLEAN));
        }

        var hostUuid = pdc.get(plugin.IN_GAME_KEY, DataType.STRING);
        var day = phaseMap.get(hostUuid);
        if(day) {
            Msg.send(player, "&4You cannot use your action during the day!");
            return true;
        }

        var usedAction = pdc.get(plugin.USED_ACTION_KEY, DataType.BOOLEAN);
        if(usedAction) {
            Msg.send(player, "&4You have already used your action for this night!");
            return true;
        }

        var hostMap = worldPdc.get(plugin.HOSTS_KEY, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        if(!hostMap.get(hostUuid).contains(targetUuid)) {
            Msg.send(player, "&4That player is not in your werewolf game!");
            return true;
        }

        target.getPersistentDataContainer().set(plugin.IS_SAFE, DataType.BOOLEAN, true);
        pdc.set(plugin.USED_ACTION_KEY, DataType.BOOLEAN, true);

        Msg.send(player, "&aThat player is now safe from the werewolves for this night!");
        return true;
    }
}