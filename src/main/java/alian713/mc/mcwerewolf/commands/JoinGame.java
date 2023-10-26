package alian713.mc.mcwerewolf.commands;

import alian713.mc.mcwerewolf.McWerewolf;
import alian713.mc.mcwerewolf.Msg;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class JoinGame extends CommandBase {
    public JoinGame() {
        super("join-game", true, true);
    }

    @Override
    public boolean onCommand(@NotNull Player player, @NotNull Player target) {
        var plugin = McWerewolf.getInstance();
        NamespacedKey playersKey = new NamespacedKey(plugin, target.getUniqueId().toString());
        NamespacedKey inGameKey = new NamespacedKey(plugin, "in_game");

        var pdc = target.getPersistentDataContainer();

        if (!pdc.has(playersKey)) {
            Msg.send(player, "&4That player is not currently hosting a werewolf game!");
            return true;
        }

        List<Player> players = Arrays.asList(pdc.get(playersKey, DataType.PLAYER_ARRAY));
        if (players.contains(player)) {
            Msg.send(player, "&4You are already in " + target.getName() + "'s game of werewolf!");
            return true;
        }
        players.add(player);
        pdc.set(playersKey, DataType.PLAYER_ARRAY, players.toArray(new Player[0]));
        Msg.send(player, "&aYou have joined " + target.getName() + "'s game of werewolf!");

        pdc = player.getPersistentDataContainer();
        pdc.set(inGameKey, DataType.PLAYER, target);

        return true;
    }
}