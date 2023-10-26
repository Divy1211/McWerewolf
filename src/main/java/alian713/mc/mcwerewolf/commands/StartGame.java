package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StartGame extends CommandBase {
    public StartGame() {
        super("start-game", false, true);
    }

    private List<String> generateRoles(int numPlayers) {
        int numWolves = numPlayers/8+1;
        List<String> roles = List.of("seer", "medic");
        return roles;

//        for(int i = 0; i < numWolves; ++i) {
//            roles.add("werewolf");
//        }
//        while(roles.size() < numPlayers) {
//            roles.add("villager");
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
        var hostsKey = new NamespacedKey(plugin, "hosts");
        var roleKey = new NamespacedKey(plugin, "role");
        var playerUuid = player.getUniqueId().toString();

        Map<String, Set<String>> hostPlayerMap = new HashMap<>();
        if (worldPdc.has(hostsKey)) {
            hostPlayerMap = worldPdc.get(hostsKey, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
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

        List<String> roles = generateRoles(players.size());

        int i = 0;
        for(String uuid : players) {
            var role = roles.get(i++);
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));

            Msg.send(p, "&aThe game of werewolf has started!");
            p.getPersistentDataContainer().set(roleKey, DataType.STRING, role);
            Msg.send(p, "&aYour role is: &b"+role);
        }
        return true;
    }
}