package id.naturalsmp.naturalVote.listeners;

import id.naturalsmp.naturalVote.NaturalVote;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final NaturalVote plugin;
    private final VoteListener voteListener;

    public JoinListener(NaturalVote plugin, VoteListener voteListener) {
        this.plugin = plugin;
        this.voteListener = voteListener;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String username = player.getName();

        int offlineVotes = plugin.getDataManager().getOfflineVotes(username);
        if (offlineVotes > 0) {
            int pointsPerVote = plugin.getConfig().getInt("points.per_vote", 1);
            int totalPoints = offlineVotes * pointsPerVote;
            
            plugin.getDataManager().addPoints(username, totalPoints);
            
            // Give rewards for each offline vote
            for (int i = 0; i < offlineVotes; i++) {
                voteListener.giveRewards(player);
            }

            // Send reminder message
            String reminderMsg = plugin.getConfig().getString("messages.vote_offline_reminder", "&a&lVOTE &8| &7Terima kasih telah vote saat offline! Anda mendapatkan hadiahnya sekarang.");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', reminderMsg));

            // Clear offline votes
            plugin.getDataManager().clearOfflineVotes(username);
        }
    }
}
