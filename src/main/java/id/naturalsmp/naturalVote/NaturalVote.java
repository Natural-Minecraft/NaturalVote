package id.naturalsmp.naturalVote;

import id.naturalsmp.naturalVote.commands.VoteCommand;
import id.naturalsmp.naturalVote.data.VoteDataManager;
import id.naturalsmp.naturalVote.listeners.JoinListener;
import id.naturalsmp.naturalVote.listeners.VoteListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ArrayList;

public final class NaturalVote extends JavaPlugin {

    private VoteDataManager dataManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        
        // Initialize DataManager
        this.dataManager = new VoteDataManager(this);

        VoteCommand voteCommand = new VoteCommand(this);
        getServer().getCommandMap().register("naturalvote", new Command("vote") {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                return voteCommand.onCommand(sender, this, commandLabel, args);
            }

            @NotNull
            @Override
            public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
                List<String> completions = voteCommand.onTabComplete(sender, this, alias, args);
                return completions == null ? new ArrayList<>() : completions;
            }
        });

        // Register GUI Listener
        getServer().getPluginManager().registerEvents(new id.naturalsmp.naturalVote.gui.GUIListener(this), this);
        
        // Register Vote Listeners
        VoteListener voteListener = new VoteListener(this);
        getServer().getPluginManager().registerEvents(voteListener, this);
        getServer().getPluginManager().registerEvents(new JoinListener(this, voteListener), this);
        org.bukkit.Bukkit.getConsoleSender().sendMessage(
                org.bukkit.ChatColor.translateAlternateColorCodes('&',
                    "\n&a===============\n" +
                    "&a _   _       _                  _     &e __     __    _\\n" +
                    "&a| \\ | | __ _| |_ _   _ _ __ __ _| |   &e \\ \\   / /__ | |_ ___\\n" +
                    "&a|  \\| |/ _` | __| | | | '__/ _` | |   &e  \\ \\ / / _ \\| __/ _ \\\\n" +
                    "&a| |\\  | (_| | |_| |_| | | | (_| | |   &e   \\ V / (_) | ||  __/\\n" +
                    "&a|_| \\_|\\__,_|\\__|\\__,_|_|       \\_/ \\_&e__/ \\__\\___|\\n" +
                    "       >> &eNaturalVote v" + getDescription().getVersion() + " Enabled! <<\n" +
                    "&a===============\n"
                )
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (dataManager != null) {
            dataManager.saveConfig();
        }
    }
    
    public VoteDataManager getDataManager() {
        return dataManager;
    }
}
