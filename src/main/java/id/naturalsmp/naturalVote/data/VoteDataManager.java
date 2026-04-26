package id.naturalsmp.naturalVote.data;

import id.naturalsmp.naturalVote.NaturalVote;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class VoteDataManager {

    private final NaturalVote plugin;
    private File dataFile;
    private FileConfiguration dataConfig;

    public VoteDataManager(NaturalVote plugin) {
        this.plugin = plugin;
        setupDataFile();
    }

    private void setupDataFile() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        dataFile = new File(plugin.getDataFolder(), "data.yml");

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create data.yml!");
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public FileConfiguration getConfig() {
        return dataConfig;
    }

    public void saveConfig() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save data.yml!");
        }
    }

    public void addOfflineVote(String playerName) {
        String path = "offline_votes." + playerName.toLowerCase();
        int current = dataConfig.getInt(path, 0);
        dataConfig.set(path, current + 1);
        saveConfig();
    }

    public int getOfflineVotes(String playerName) {
        return dataConfig.getInt("offline_votes." + playerName.toLowerCase(), 0);
    }

    public void clearOfflineVotes(String playerName) {
        dataConfig.set("offline_votes." + playerName.toLowerCase(), null);
        saveConfig();
    }

    public void addTotalVote(String playerName) {
        String path = "total_votes." + playerName.toLowerCase();
        int current = dataConfig.getInt(path, 0);
        dataConfig.set(path, current + 1);
        saveConfig();
    }

    public int getPoints(String playerName) {
        return dataConfig.getInt("vote_points." + playerName.toLowerCase(), 0);
    }

    public void addPoints(String playerName, int amount) {
        String path = "vote_points." + playerName.toLowerCase();
        int current = dataConfig.getInt(path, 0);
        dataConfig.set(path, current + amount);
        saveConfig();
    }

    public void removePoints(String playerName, int amount) {
        String path = "vote_points." + playerName.toLowerCase();
        int current = dataConfig.getInt(path, 0);
        dataConfig.set(path, Math.max(0, current - amount));
        saveConfig();
    }
}
