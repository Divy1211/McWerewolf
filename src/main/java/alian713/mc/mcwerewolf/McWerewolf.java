package alian713.mc.mcwerewolf;

import alian713.mc.mcwerewolf.commands.CancelGame;
import alian713.mc.mcwerewolf.commands.CancelGameOther;
import alian713.mc.mcwerewolf.commands.HostGame;
import alian713.mc.mcwerewolf.commands.JoinGame;
import org.bukkit.plugin.java.JavaPlugin;

public final class McWerewolf extends JavaPlugin {
    private static McWerewolf instance;

    public static McWerewolf getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Enabled McWerewolf!");

        new HostGame();
        new JoinGame();
        new CancelGame();
        new CancelGameOther();
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
