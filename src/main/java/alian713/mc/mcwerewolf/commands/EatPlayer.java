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

public class EatPlayer extends CommandBase {
    public EatPlayer() {
        super("eat-player", true, true);
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

        if(!pdc.get(plugin.IS_ALIVE_KEY, DataType.BOOLEAN)) {
            Msg.send(player, "&4Dead players cannot use night time actions!");
            return true;
        }

        var role = pdc.get(plugin.ROLE_KEY, DataType.STRING);
        if (!role.equals(Role.WEREWOLF)) {
            Msg.send(player, "&4You are not a werewolf!");
            return true;
        }

        if (playerUuid.equals(targetUuid)) {
            Msg.send(player, "&4Why in gods name are you trying to eat yourself?");
            return true;
        }

        Map<String, Boolean> phaseMap = new HashMap<>();
        if (worldPdc.has(plugin.DAY_KEY)) {
            phaseMap = worldPdc.get(plugin.DAY_KEY, DataType.asMap(DataType.STRING, DataType.BOOLEAN));
        }

        var hostUuid = pdc.get(plugin.IN_GAME_KEY, DataType.STRING);
        var day = phaseMap.get(hostUuid);
        if (day) {
            Msg.send(player, "&4You cannot use your action during the day!");
            return true;
        }

        var usedAction = pdc.get(plugin.USED_ACTION_KEY, DataType.BOOLEAN);
        if (usedAction) {
            Msg.send(player, "&4You have already used your action for this night!");
            return true;
        }

        var hostMap = worldPdc.get(plugin.HOSTS_KEY, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        if (!hostMap.get(hostUuid).contains(targetUuid)) {
            Msg.send(player, "&4That player is not in your werewolf game!");
            return true;
        }

        var targetPdc = target.getPersistentDataContainer();

        var targetRole = targetPdc.get(plugin.ROLE_KEY, DataType.STRING);

        if(Role.WEREWOLF.equals(targetRole)) {
            Msg.send(player, "&4You cannot eat other werewolves!");
            return true;
        }

        if(!targetPdc.get(plugin.IS_ALIVE_KEY, DataType.BOOLEAN)) {
            Msg.send(player, "&4That player is already dead!");
            return true;
        }

        pdc.set(plugin.USED_ACTION_KEY, DataType.BOOLEAN, true);
        Msg.send(player, "&aThat player will be eaten!");

        targetPdc.set(plugin.IS_EATEN_KEY, DataType.BOOLEAN, true);
        return true;
    }
}