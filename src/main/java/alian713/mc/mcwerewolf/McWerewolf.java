package alian713.mc.mcwerewolf;

import alian713.mc.mcwerewolf.commands.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class McWerewolf extends JavaPlugin {
    private static McWerewolf instance;

    public final NamespacedKey HOSTS_KEY = new NamespacedKey(this, "hosts");
    public final NamespacedKey DAY_KEY = new NamespacedKey(this, "day");

    public final NamespacedKey VOTE_COUNT_KEY = new NamespacedKey(this, "vote_count_key");
    public final NamespacedKey IN_GAME_KEY = new NamespacedKey(this, "in_game");
    public final NamespacedKey ROLE_KEY = new NamespacedKey(this, "role");
    public final NamespacedKey IS_EATEN_KEY = new NamespacedKey(this, "eaten");
    public final NamespacedKey IS_ALIVE_KEY = new NamespacedKey(this, "alive");
    public final NamespacedKey USED_ACTION_KEY = new NamespacedKey(this, "used_action");
    public final NamespacedKey IS_SAFE_KEY = new NamespacedKey(this, "is_safe");
    public final NamespacedKey HAS_VOTED = new NamespacedKey(this, "has_voted");

    public static McWerewolf getInstance() {
        return instance;
    }

    private void cleanData() {
        var overworld = Bukkit.getWorld(((DedicatedServer) MinecraftServer.getServer()).getProperties().levelName);
        var worldPdc = overworld.getPersistentDataContainer();
        worldPdc.remove(HOSTS_KEY);
        worldPdc.remove(DAY_KEY);
    }

    @Override
    public void onEnable() {
        instance = this;

        cleanData();

        getLogger().info("Enabled McWerewolf!");
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        new HostGame();
        new JoinGame();
        new CancelGame();
        new LeaveGame();
        new ListGames();
        new ListPlayers();
        new StartGame();
        new DayPhase();
        new NightPhase();
        new SeePlayer();
        new SavePlayer();
        new EatPlayer();
        new Lynch();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling McWerewolf!");
    }
}
