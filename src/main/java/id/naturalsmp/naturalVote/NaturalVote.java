package id.naturalsmp.naturalVote;

import id.naturalsmp.naturalVote.commands.VoteCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class NaturalVote extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        VoteCommand voteCommand = new VoteCommand(this);
        if (getCommand("vote") != null) {
            getCommand("vote").setExecutor(voteCommand);
            getCommand("vote").setTabCompleter(voteCommand);
        } else {
            getLogger().warning("Format 'vote' tidak ditemukan di paper-plugin.yml!");
        }

        getServer().getPluginManager().registerEvents(new id.naturalsmp.naturalVote.gui.GUIListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
