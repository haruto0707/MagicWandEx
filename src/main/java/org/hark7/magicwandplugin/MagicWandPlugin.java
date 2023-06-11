package org.hark7.magicwandplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.block.Block;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;

public class MagicWandPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getCommand("magicwand").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (command.getName().equalsIgnoreCase("magicwand")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        ItemStack magicWand = new ItemStack(Material.STICK, 1);

                        // Add custom name to the magic wand
                        ItemMeta meta = magicWand.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(ChatColor.RED + "爆破の杖"); // Set name to "爆破の杖" in red color
                            magicWand.setItemMeta(meta);
                        }

                        player.getInventory().addItem(magicWand); // Add the magic wand to the player's inventory
                        return true;
                    } else {
                        sender.sendMessage("This command can only be used by players.");
                        return false;
                    }
                }

                // If the command is not recognized, return false
                return false;
            }
        });

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.STICK) { // If the item in hand is a stick (our magic wand)
            @NotNull Location explosionLocation;

            // Get the block the player is currently targeting
            Block targetedBlock = player.getTargetBlock(null, 100);

            if (targetedBlock.getType() != Material.AIR) {
                // If the player is targeting a block, create the explosion there
                explosionLocation = targetedBlock.getLocation();
            } else {
                // If the player isn't targeting a block, create the explosion 30 blocks along their line of sight
                explosionLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(50));
            }

            player.getWorld().createExplosion(explosionLocation, 2.0f); // Create an explosion with a power of 2.0
        }
    }
}
