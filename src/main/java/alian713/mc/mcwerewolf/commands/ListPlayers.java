package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ListPlayers extends CommandBase {
    public ListPlayers() {
        super("list-players", true, false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Player target) {
        var plugin = McWerewolf.getInstance();
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var targetUuid = target.getUniqueId().toString();

        Map<String, Set<String>> hostPlayerMap = new HashMap<>();
        if (worldPdc.has(plugin.HOSTS_KEY)) {
            hostPlayerMap = worldPdc.get(plugin.HOSTS_KEY, DataType.asMap(DataType.STRING, DataType.asSet(DataType.STRING)));
        }

        if (!hostPlayerMap.containsKey(targetUuid)) {
            Msg.send(sender, "&4That player is not currently hosting a werewolf game!");
            return true;
        }

        Msg.send(sender, "&bThe following players are in " + target.getName() + "'s game of werewolf:");
        for (var uuid : hostPlayerMap.get(targetUuid)) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if (p == null) {
                continue;
            }
            Msg.send(sender, p.getName());
        }
        return true;
    }
}