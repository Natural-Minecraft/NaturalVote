package id.naturalsmp.naturalVote.gui;

import id.naturalsmp.naturalVote.NaturalVote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VoteShopGUI implements InventoryHolder {

    private final Inventory inventory;
    private final NaturalVote plugin;
    private final Player player;

    public VoteShopGUI(NaturalVote plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        String title = color(plugin.getConfig().getString("shop.title", "&8▶ &aVote Shop"));
        int size = plugin.getConfig().getInt("shop.size", 27);

        this.inventory = Bukkit.createInventory(this, size, title);
        initializeItems();
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private void initializeItems() {
        // Points indicator
        int points = plugin.getDataManager().getPoints(player.getName());
        ItemStack pointsItem = createItem(Material.SUNFLOWER, "&e&lPoin Kamu: &a" + points, "&7Gunakan poin ini untuk", "&7membeli hadiah di bawah.");
        inventory.setItem(4, pointsItem);

        // Load items from config
        ConfigurationSection itemsSection = plugin.getConfig().getConfigurationSection("shop.items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSec = itemsSection.getConfigurationSection(key);
                if (itemSec == null) continue;

                int slot = itemSec.getInt("slot");
                String matStr = itemSec.getString("material", "STONE");
                Material mat = Material.matchMaterial(matStr);
                if (mat == null) mat = Material.STONE;

                String name = itemSec.getString("name", "&cUnnamed Item");
                List<String> lore = itemSec.getStringList("lore");

                ItemStack item = createItem(mat, name, lore.toArray(new String[0]));
                inventory.setItem(slot, item);
            }
        }
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

    public void open() {
        player.openInventory(inventory);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
