package id.naturalsmp.naturalVote.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import id.naturalsmp.naturalVote.NaturalVote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;

public class VoteListener implements Listener {

    private final NaturalVote plugin;

    public VoteListener(NaturalVote plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
        String username = vote.getUsername();
        String serviceName = vote.getServiceName();

        // Increment total votes
        plugin.getDataManager().addTotalVote(username);

        // Broadcast to all players
        String broadcastMsg = plugin.getConfig().getString("messages.vote_broadcast", "&a&lVOTE &8| &e%player% &7baru saja melakukan vote di &e%service%&7! &8(&a/vote&8)");
        broadcastMsg = broadcastMsg.replace("%player%", username).replace("%service%", serviceName);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMsg));

        Player player = Bukkit.getPlayerExact(username);
        
        int points = plugin.getConfig().getInt("points.per_vote", 1);
        
        if (player != null && player.isOnline()) {
            // Player is online, give rewards & points
            plugin.getDataManager().addPoints(username, points);
            giveRewards(player);
        } else {
            // Player is offline, save to queue
            plugin.getDataManager().addOfflineVote(username);
        }
    }

    public void giveRewards(Player player) {
        List<String> commands = plugin.getConfig().getStringList("rewards.commands");
        for (String cmd : commands) {
            String finalCmd = cmd.replace("%player%", player.getName());
            // Execute command synchronously as console
            Bukkit.getScheduler().runTask(plugin, () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
            });
        }
    }
}
