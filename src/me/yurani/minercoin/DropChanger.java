package me.yurani.minercoin;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class DropChanger implements Listener{

	File configPath;
	FileConfiguration Config;
	Scroll scroll;
	
	private Main main;
	public DropChanger(Main p){
		this.main = p;
		scroll = new Scroll(p);
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		
		configPath = new File(main.getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		boolean isDropOre = Config.getBoolean("MinerCoin.OreCoin.BlockOreDrops");
		
		if (!isDropOre){
			// Mendeteksi drop item
			int amount = 1;
			if (e.getBlock().getType() == Material.IRON_ORE){
				e.getBlock().getDrops().clear();
				e.getBlock().breakNaturally(new ItemStack(Material.IRON_ORE, amount));
				e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT, amount));
			}
			else if (e.getBlock().getType() == Material.GOLD_ORE){
				e.getBlock().getDrops().clear();
				e.getBlock().breakNaturally(new ItemStack(Material.GOLD_ORE, amount));
				e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT, amount));
			}
		}

		
	}
}
