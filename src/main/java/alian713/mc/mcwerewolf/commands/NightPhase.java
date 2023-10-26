package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import alian713.mc.mcwerewolf.PlayerListener;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NightPhase extends CommandBase {
    public NightPhase() {
        super("night-phase", false, true);
    }

    @Override
    public boolean onCommand(@NotNull Player player) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var playerUuid = player.getUniqueId().toString();

        Map<String, Set<String>> hostPlayerMap = new HashMap<>();
        if (worldPdc.has(plugin.HOSTS_KEY)) {
            hostPlayerMap = worldPdc.get(plugin.HOSTS_KEY, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        }

        if(!hostPlayerMap.containsKey(playerUuid)) {
            Msg.send(player, "&4You have not hosted a werewolf game!");
            return true;
        }

        var pdc = player.getPersistentDataContainer();
        if(!pdc.has(plugin.ROLE_KEY)) {
            Msg.send(player, "&4You are not in an active werewolf game!");
            return true;
        }

        Map<String, Boolean> phaseMap = new HashMap<>();
        if (worldPdc.has(plugin.DAY_KEY)) {
            phaseMap = worldPdc.get(plugin.DAY_KEY, DataType.asMap(DataType.STRING, DataType.BOOLEAN));
        }

        var day = phaseMap.get(playerUuid);
        if(!day) {
            Msg.send(player, "&4It is already night time!");
            return true;
        }

        phaseMap.put(playerUuid, false);
        worldPdc.set(plugin.DAY_KEY, DataType.asMap(DataType.STRING, DataType.BOOLEAN), phaseMap);
        Msg.broadcast(hostPlayerMap.get(playerUuid), "&aIt is now night time! Use your night phase action");
        for(var uuid : hostPlayerMap.get(playerUuid)) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if(p == null) {
                continue;
            }
            p.getPersistentDataContainer().set(plugin.USED_ACTION_KEY, DataType.BOOLEAN, false);
        }
        return true;
    }
}