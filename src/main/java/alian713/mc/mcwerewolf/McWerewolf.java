package alian713.mc.mcwerewolf;

import alian713.mc.mcwerewolf.commands.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

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
        new StartGame();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling McWerewolf!");
    }
}
