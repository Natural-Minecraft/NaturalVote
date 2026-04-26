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
                    org.bukkit.Sound sound = org.bukkit.Sound.valueOf(soundData);
                    player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                } catch (IllegalArgumentException e) {
                    // Ignore
                }

                // Kirim pesan link vote
                java.util.List<String> messages = plugin.getConfig().getStringList("gui.vote_item.click_message");
                for (String msg : messages) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
                
                player.closeInventory();
            } else if (slot == 22) {
                // Open Shop
                player.closeInventory();
                new VoteShopGUI(plugin, player).open();
            }
        } else if (event.getInventory().getHolder() instanceof VoteShopGUI) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            
            org.bukkit.configuration.ConfigurationSection itemsSection = plugin.getConfig().getConfigurationSection("shop.items");
            if (itemsSection != null) {
                for (String key : itemsSection.getKeys(false)) {
                    org.bukkit.configuration.ConfigurationSection itemSec = itemsSection.getConfigurationSection(key);
                    if (itemSec != null && itemSec.getInt("slot") == slot) {
                        int cost = itemSec.getInt("cost", 0);
                        int currentPoints = plugin.getDataManager().getPoints(player.getName());
                        
                        if (currentPoints >= cost) {
                            plugin.getDataManager().removePoints(player.getName(), cost);
                            
                            java.util.List<String> commands = itemSec.getStringList("commands");
                            for (String cmd : commands) {
                                String finalCmd = cmd.replace("%player%", player.getName());
                                org.bukkit.Bukkit.dispatchCommand(org.bukkit.Bukkit.getConsoleSender(), finalCmd);
                            }
                            
                            String successMsg = plugin.getConfig().getString("shop.buy_success", "&aBerhasil membeli item!");
                            successMsg = successMsg.replace("%item%", itemSec.getString("name", "Item")).replace("%cost%", String.valueOf(cost));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', successMsg));
                            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                            
                            player.closeInventory();
                        } else {
                            String failMsg = plugin.getConfig().getString("shop.insufficient_points", "&cPoin tidak cukup!");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', failMsg));
                            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        }
                        break;
                    }
                }
            }
        }
    }
}
