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

import java.util.*;

public class StartGame extends CommandBase {
    public StartGame() {
        super("start-game", false, true);
    }

    private List<String> generateRoles(int numPlayers) {
        int numWolves = numPlayers/8+1;
        List<String> roles = List.of(Role.SEER, Role.MEDIC);
        return roles;

//        for(int i = 0; i < numWolves; ++i) {
//            roles.add(Role.WEREWOLF);
//        }
//        while(roles.size() < numPlayers) {
//            roles.add(Role.VILLAGER);
//        }
//
//        Collections.shuffle(roles);
//        return roles;
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

        var players = hostPlayerMap.get(playerUuid);

//        if(players.size() < 5) {
//            Msg.send(player, "&4Cannot start game with less than 5 players!");
//            return true;
//        }

        Map<String, Boolean> phaseMap = new HashMap<>();
        if (worldPdc.has(plugin.DAY_KEY)) {
            phaseMap = worldPdc.get(plugin.DAY_KEY, DataType.asMap(DataType.STRING, DataType.BOOLEAN));
        }
        phaseMap.put(playerUuid, true);
        worldPdc.set(plugin.DAY_KEY, DataType.asMap(DataType.STRING, DataType.BOOLEAN), phaseMap);


        List<String> roles = generateRoles(players.size());

        int i = 0;
        for(String uuid : players) {
            var role = roles.get(i++);
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));

            var pdc = p.getPersistentDataContainer();
            pdc.set(plugin.ROLE_KEY, DataType.STRING, role);
            pdc.set(plugin.ALIVE_KEY, DataType.BOOLEAN, true);
            pdc.set(plugin.IS_SAFE, DataType.BOOLEAN, false);

            Msg.send(p, "&aThe game of werewolf has started!");
            Msg.send(p, "&aYour role is: &b"+role);
        }
        Bukkit.dispatchCommand(player, "night-phase");
        return true;
    }
}