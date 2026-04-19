package id.naturalsmp.naturalVote.gui;

import id.naturalsmp.naturalVote.NaturalVote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VoteGUI implements InventoryHolder {

    private final Inventory inventory;
    private final NaturalVote plugin;

    public VoteGUI(NaturalVote plugin) {
        this.plugin = plugin;
        String title = color(plugin.getConfig().getString("gui.title", "&8Menu Vote"));
        int size = plugin.getConfig().getInt("gui.size", 27);
        
        this.inventory = Bukkit.createInventory(this, size, title);
        initializeItems();
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private void initializeItems() {
        // Fill border with glass panes
        ItemStack filler = createItem(Material.BLACK_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, filler);
        }

        // Add Vote Item in the middle
        int centerSlot = plugin.getConfig().getInt("gui.vote_slot", 13);
        Material mat = Material.matchMaterial(plugin.getConfig().getString("gui.vote_item.material", "FIREWORK_ROCKET"));
        if (mat == null) mat = Material.FIREWORK_ROCKET;
        
        String name = plugin.getConfig().getString("gui.vote_item.name", "&a&lVote Server Kami!");
        List<String> loreParams = plugin.getConfig().getStringList("gui.vote_item.lore");
        
        ItemStack voteItem = createItem(mat, name, loreParams.toArray(new String[0]));
        inventory.setItem(centerSlot, voteItem);
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(color(name));
            if (lore != null && lore.length > 0) {
                List<String> coloredLore = new ArrayList<>();
                for (String line : lore) {
                    coloredLore.add(color(line));
                }
                meta.setLore(coloredLore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
