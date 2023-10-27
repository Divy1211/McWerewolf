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
import java.util.Set;
import java.util.UUID;

public class DayPhase extends CommandBase {
    public DayPhase() {
        super("day-phase", false, true);
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

        if (!hostPlayerMap.containsKey(playerUuid)) {
            Msg.send(player, "&4You have not hosted a werewolf game!");
            return true;
        }

        var pdc = player.getPersistentDataContainer();
        if (!pdc.has(plugin.ROLE_KEY)) {
            Msg.send(player, "&4You are not in an active werewolf game!");
            return true;
        }

        Map<String, Boolean> phaseMap = new HashMap<>();
        if (worldPdc.has(plugin.DAY_KEY)) {
            phaseMap = worldPdc.get(plugin.DAY_KEY, DataType.asMap(DataType.STRING, DataType.BOOLEAN));
        }

        var day = phaseMap.get(playerUuid);
        if (day) {
            Msg.send(player, "&4It is already day time!");
            return true;
        }

        phaseMap.put(playerUuid, true);
        worldPdc.set(plugin.DAY_KEY, DataType.asMap(DataType.STRING, DataType.BOOLEAN), phaseMap);

        var players = hostPlayerMap.get(playerUuid);

        int numWolves = 0;
        int numAlive = 0;
        for (var uuid : players) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if (p == null) {
                continue;
            }
            var playerPdc = p.getPersistentDataContainer();
            var isEaten = playerPdc.get(plugin.IS_EATEN_KEY, DataType.BOOLEAN);
            var isSafe = playerPdc.get(plugin.IS_SAFE_KEY, DataType.BOOLEAN);

            if (!isSafe && isEaten) {
                var role = playerPdc.get(plugin.ROLE_KEY, DataType.STRING);
                Msg.broadcast(players, "&c" + p.getName() + " has been eaten! They were &b" + role);

                playerPdc.set(plugin.IS_ALIVE_KEY, DataType.BOOLEAN, false);
                playerPdc.set(plugin.IS_EATEN_KEY, DataType.BOOLEAN, false);
            }
            if (playerPdc.get(plugin.IS_ALIVE_KEY, DataType.BOOLEAN)) {
                if (playerPdc.get(plugin.ROLE_KEY, DataType.STRING).equals(Role.WEREWOLF)) {
                    ++numWolves;
                } else {
                    ++numAlive;
                }
            }
            playerPdc.set(plugin.IS_SAFE_KEY, DataType.BOOLEAN, false);
            playerPdc.set(plugin.HAS_VOTED, DataType.BOOLEAN, false);
            pdc.set(plugin.NOM_COUNT_KEY, DataType.INTEGER, 0);
        }

        if (numWolves == numAlive) {
            Msg.broadcast(players, "&cThe werewolves have won the game!");
            Msg.broadcast(players, "&cRoles:");
            NightPhase.showRoles(players);
            CancelGame.onCancel(player, true);
        } else if (numWolves == 0) {
            Msg.broadcast(players, "&aThe villagers have won the game!");
            Msg.broadcast(players, "&aRoles:");
            NightPhase.showRoles(players);
            CancelGame.onCancel(player, true);
        } else {
            Msg.broadcast(players, "&aIt is now day time! Discuss");
        }
        return true;
    }
}