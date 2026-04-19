package id.naturalsmp.naturalVote.gui;

import id.naturalsmp.naturalVote.NaturalVote;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class GUIListener implements Listener {

    private final NaturalVote plugin;

    public GUIListener(NaturalVote plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        
        // Cek apakah inventory adalah VoteGUI
        if (event.getInventory().getHolder() instanceof VoteGUI) {
            event.setCancelled(true); // Prevent taking items
            
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            int voteSlot = plugin.getConfig().getInt("gui.vote_slot", 13);
            
            if (slot == voteSlot) {
                // Dimainkan sound
                String soundData = plugin.getConfig().getString("gui.vote_item.sound", "UI_BUTTON_CLICK");
                try {
                    Sound sound = Sound.valueOf(soundData);
                    player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Sound tidak valid: " + soundData);
                }

                // Kirim pesan link vote
                List<String> messages = plugin.getConfig().getStringList("gui.vote_item.click_message");
                for (String msg : messages) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
                
                player.closeInventory();
            }
        }
    }
}
