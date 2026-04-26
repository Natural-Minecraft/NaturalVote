package id.naturalsmp.naturalVote.commands;

import id.naturalsmp.naturalVote.NaturalVote;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VoteCommand implements CommandExecutor, TabCompleter {
    private final NaturalVote plugin;

    public VoteCommand(NaturalVote plugin) {
        this.plugin = plugin;
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            if (!sender.hasPermission("naturalvote.admin")) {
                sender.sendMessage(color(plugin.getConfig().getString("messages.no_permission", "&cNo permission.")));
                return true;
            }
            sendHelpMessage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("naturalvote.admin")) {
                    sender.sendMessage(color(plugin.getConfig().getString("messages.no_permission", "&cNo permission.")));
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage(color(plugin.getConfig().getString("messages.reload_success", "&aConfig reloaded!")));
                break;
            case "gui":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(color(plugin.getConfig().getString("messages.player_only", "&cPlayer only.")));
                    return true;
                }
                Player p = (Player) sender;
                new id.naturalsmp.naturalVote.gui.VoteGUI(plugin, p).open(p);
                break;
            case "shop":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(color(plugin.getConfig().getString("messages.player_only", "&cPlayer only.")));
                    return true;
                }
                Player pShop = (Player) sender;
                new id.naturalsmp.naturalVote.gui.VoteShopGUI(plugin, pShop).open();
                break;
            case "party":
                if (!sender.hasPermission("naturalvote.admin")) {
                    sender.sendMessage(color(plugin.getConfig().getString("messages.no_permission", "&cNo permission.")));
                    return true;
                }
                sender.sendMessage(color("&aMemicu Vote Party... &7(Fitur sedang dalam pengembangan)"));
                // TODO: Trigger vote party logic here
                break;
            default:
                sendHelpMessage(sender);
                break;
        }

        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(color(plugin.getConfig().getString("messages.help_header", "&8&m--------&r &a&lNaturalVote &8&m--------")));
        sender.sendMessage(color(plugin.getConfig().getString("messages.help_gui", "&a/vote gui &8- &7Buka GUI")));
        sender.sendMessage(color("&a/vote shop &8- &7Buka Vote Shop"));
        sender.sendMessage(color(plugin.getConfig().getString("messages.help_party", "&a/vote party &8- &7Trigger vote party")));
        sender.sendMessage(color(plugin.getConfig().getString("messages.help_reload", "&a/vote reload &8- &7Reload plugin")));
        sender.sendMessage(color(plugin.getConfig().getString("messages.help_footer", "&8&m------------------------")));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("naturalvote.admin")) {
                completions.add("help");
                completions.add("reload");
                completions.add("party");
            }
            if (sender instanceof Player) {
                completions.add("gui");
                completions.add("shop");
            }

            // Filter completions based on what the user has typed so far
            List<String> filteredCompletions = new ArrayList<>();
            for (String c : completions) {
                if (c.toLowerCase().startsWith(args[0].toLowerCase())) {
                    filteredCompletions.add(c);
                }
            }
            return filteredCompletions;
        }
        return new ArrayList<>();
    }
}
