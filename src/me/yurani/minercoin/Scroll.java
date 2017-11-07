package me.yurani.minercoin;

import java.io.File;
import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Scroll implements Listener{

	private Main main;
	public Scroll(Main p){
		this.main = p;
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
	}
	
	String Prefix;
	File configPath;
	FileConfiguration Config;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		configPath = new File(main.getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		
		
		if (e.getPlayer().getItemInHand().getType().equals(Material.PAPER)
				&& e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Duplicator Ore Scroll")){
			// do something
			if (e.getPlayer().getItemInHand().getAmount() > 1){
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1); 
			}
			else {
				e.getPlayer().getItemInHand().setAmount(0); 
			}
			Prefix = Config.getString("MinerCoin.Notification.Prefix");
			Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
			e.getPlayer().sendMessage(Prefix + ChatColor.GREEN + "Duplicator Ore Scroll is activated!");
			
		}
		else if (e.getPlayer().getItemInHand().getType().equals(Material.PAPER)
				&& e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Exp Scroll")){
			// do something
			if (e.getPlayer().getItemInHand().getAmount() > 1){
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1); 
			}
			else {
				e.getPlayer().getItemInHand().setAmount(0); 
			}
			
			//Set Effect Scroll
			
			Prefix = Config.getString("MinerCoin.Notification.Prefix");
			Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
			e.getPlayer().sendMessage(Prefix + ChatColor.GREEN + "Double Exp Scroll is activated!");
		}
		else if (e.getPlayer().getItemInHand().getType().equals(Material.PAPER)
				&& e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Double ORC Scroll")){
			// do something
			if (e.getPlayer().getItemInHand().getAmount() > 1){
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1); 
			}
			else {
				e.getPlayer().getItemInHand().setAmount(0); 
			}
			
			Prefix = Config.getString("MinerCoin.Notification.Prefix");
			Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
			e.getPlayer().sendMessage(Prefix + ChatColor.GREEN + "Double ORC Scroll is activated!");
		}
	}
}
