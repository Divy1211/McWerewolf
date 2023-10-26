package alian713.mc.mcwerewolf;

import alian713.mc.mcwerewolf.commands.*;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class McWerewolf extends JavaPlugin {
    private static McWerewolf instance;

    public static McWerewolf getInstance() {
        return instance;
    }

    private void cleanHostList() {
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        var hostsKey = new NamespacedKey(this, "hosts");
        worldPdc.remove(hostsKey);
    }

    @Override
    public void onEnable() {
        instance = this;

        cleanHostList();

        getLogger().info("Enabled McWerewolf!");
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        new HostGame();
        new JoinGame();
        new CancelGame();
        new LeaveGame();
        new ListGames();
        new ListPlayers();
        // getCommand("list-games").setExecutor(new ListGames());
        // getCommand("list-players").setExecutor(new ListPlayers());
        // getCommand("start-game").setExecutor(new StartGame());
        // getCommand("stop-game").setExecutor(new StopGame());
        // getCommand("join-game").setExecutor(new JoinGame());
        // getCommand("leave-game").setExecutor(new LeaveGame());
        // getCommand("see-player").setExecutor(new SeePlayer());
        // getCommand("eat-player").setExecutor(new EatPlayer());
        // getCommand("save-player").setExecutor(new SavePlayer());
        // getCommand("nom-player").setExecutor(new NomPlayer());
        // getCommand("exec").setExecutor(new Exec());
        // getCommand("no-exec").setExecutor(new NoExec());
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling McWerewolf!");
    }
}
