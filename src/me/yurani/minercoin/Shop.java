package me.yurani.minercoin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Shop implements Listener{

	private Inventory inv;
	private ItemStack withdraw, withdraw2, withdraw3, scrollOre, scrollExp, scrollORC;
	
	File configPath;
	FileConfiguration Config;
	
	File playerPath;
	FileConfiguration playerConfig;
	
	private static Economy econ = null;
	
	private Main main;
	public Shop(Main p){
		this.main = p;
		invenInit();
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		
        if (!setupEconomy() ) {
            main.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", main.getDescription().getName()));
            main.getServer().getPluginManager().disablePlugin(p);
            return;
        }
        
    	main.saveDefaultConfig();
	}
	
    private boolean setupEconomy() {
        if (main.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = main.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    private double countingMoney(double coinP){
    	//Counting Money
		double Baseorc = Config.getDouble("MinerCoin.OreCoin.Price.1ORC");
		double nom = coinP * Baseorc; 
		Double _nom = new Double(nom);
		int calc = _nom.intValue();
		return calc;
    }
	
	private void invenInit(){
		
		configPath = new File(main.getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		
		inv = Bukkit.getServer().createInventory(null, 18, ChatColor.GREEN + "§lOre Coins Shop");
		ArrayList<String> listLore = new ArrayList<String>();
		
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Description §e««");
		listLore.add(ChatColor.AQUA + "Ore Coins is a virtual money.");
		listLore.add(ChatColor.AQUA + "Player can get the coin by");
		listLore.add(ChatColor.AQUA + "mining ore or stone, and you");
		listLore.add(ChatColor.AQUA + "can buy any item using Ore Coin");
		listLore.add(ChatColor.AQUA + "in Ore Coins Shop.");
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Base Price §e««");
		listLore.add(ChatColor.YELLOW + "§e» 1 ORC §6» " + ChatColor.GREEN + "$" + NumberFormat.getNumberInstance(Locale.US).format(countingMoney(1)));
		listLore.add(ChatColor.YELLOW + "§e» 0.5 ORC §6» " + ChatColor.GREEN + "$" + NumberFormat.getNumberInstance(Locale.US).format(countingMoney(0.5)));
		
		listLore.add(ChatColor.YELLOW + "§e» 0.1 ORC §6» " + ChatColor.GREEN + "$" + NumberFormat.getNumberInstance(Locale.US).format(countingMoney(0.1)));

		listLore.add(" ");
		listLore.add("§6»" + ChatColor.GREEN + " Click to buy");

		withdraw = createItem(Material.GOLD_NUGGET, ChatColor.YELLOW + "Withdraw Ore Coins §e» 1 ORC", listLore);
		inv.setItem(1, withdraw);
		//////////////////////////////////////////////////////////
		listLore.clear();
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Description §e««");
		listLore.add(ChatColor.AQUA + "Ore Coins is a virtual money.");
		listLore.add(ChatColor.AQUA + "Player can get the coin by");
		listLore.add(ChatColor.AQUA + "mining ore or stone, and you");
		listLore.add(ChatColor.AQUA + "can buy any item using Ore Coin");
		listLore.add(ChatColor.AQUA + "in Ore Coins Shop.");
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Base Price §e««");
		listLore.add(ChatColor.YELLOW + "§e» 1 ORC §6» " + ChatColor.GREEN + "$" + NumberFormat.getNumberInstance(Locale.US).format(countingMoney(1)));
		listLore.add(ChatColor.YELLOW + "§e» 0.5 ORC §6» " + ChatColor.GREEN + "$" + NumberFormat.getNumberInstance(Locale.US).format(countingMoney(0.5)));
		
		listLore.add(ChatColor.YELLOW + "§e» 0.1 ORC §6» " + ChatColor.GREEN + "$" + NumberFormat.getNumberInstance(Locale.US).format(countingMoney(0.1)));
		
		listLore.add(" ");
		listLore.add("§6»" + ChatColor.GREEN + " Click to buy");

		withdraw2 = createItem(Material.GOLD_NUGGET, ChatColor.YELLOW + "Withdraw Ore Coins §e» 0.5 ORC", listLore);
		inv.setItem(2, withdraw2);
		//////////////////////////////////////////////////////////
		listLore.clear();
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Description §e««");
		listLore.add(ChatColor.AQUA + "Ore Coins is a virtual money.");
		listLore.add(ChatColor.AQUA + "Player can get the coin by");
		listLore.add(ChatColor.AQUA + "mining ore or stone, and you");
		listLore.add(ChatColor.AQUA + "can buy any item using Ore Coin");
		listLore.add(ChatColor.AQUA + "in Ore Coins Shop.");
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Base Price §e««");
		listLore.add(ChatColor.YELLOW + "§e» 1 ORC §6» " + ChatColor.GREEN + "$" + NumberFormat.getNumberInstance(Locale.US).format(countingMoney(1)));
		listLore.add(ChatColor.YELLOW + "§e» 0.5 ORC §6» " + ChatColor.GREEN + "$" + NumberFormat.getNumberInstance(Locale.US).format(countingMoney(0.5)));
		
		listLore.add(ChatColor.YELLOW + "§e» 0.1 ORC §6» " + ChatColor.GREEN + "$" + NumberFormat.getNumberInstance(Locale.US).format(countingMoney(0.1)));
		listLore.add(" ");
		listLore.add("§6»" + ChatColor.GREEN + " Click to buy");

		withdraw3 = createItem(Material.GOLD_NUGGET, ChatColor.YELLOW + "Withdraw Ore Coins §e» 0.1 ORC", listLore);
		inv.setItem(3, withdraw3);
		//////////////////////////////////////////////////////////
		
		listLore.clear();
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Description §e««");
		listLore.add(ChatColor.AQUA + "A scroll that can duplicate blocks");
		listLore.add(ChatColor.AQUA + "when destroying block ore.");
		listLore.add(ChatColor.AQUA + "This item only works on some");
		listLore.add(ChatColor.AQUA + "type of ore only");
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Base Price §e««");
		listLore.add(ChatColor.YELLOW + "§e» 0.02 §6» " + ChatColor.GREEN + "§7[§a+§7]§a 1 Scroll Item");
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Special Effect §e««");
		listLore.add(ChatColor.YELLOW + "Double Ore §6» " + ChatColor.GREEN + "2x");
		listLore.add(ChatColor.YELLOW + "Duration §6» " + ChatColor.GREEN + "8 minutes");
		listLore.add(" ");
		listLore.add("§6»" + ChatColor.GREEN + " Click to buy");

		scrollOre = createItem(Material.PAPER, ChatColor.GREEN + "Duplicator Ore Scroll", listLore);
		inv.setItem(5, scrollOre);
		//////////////////////////////////////////////////////////
		
		listLore.clear();
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Description §e««");
		listLore.add(ChatColor.AQUA + "A scroll that can give ");
		listLore.add(ChatColor.AQUA + "double exp to player.");
		listLore.add(ChatColor.AQUA + "Exp given only to certain ");
		listLore.add(ChatColor.AQUA + "ore block with limited ");
		listLore.add(ChatColor.AQUA + "active period.");
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Base Price §e««");
		listLore.add(ChatColor.YELLOW + "§e» 0.03 §6» " + ChatColor.GREEN + "§7[§a+§7]§a 1 Scroll Item");
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Special Effect §e««");
		listLore.add(ChatColor.YELLOW + "Double Exp §6» " + ChatColor.GREEN + "2x");
		listLore.add(ChatColor.YELLOW + "Duration §6» " + ChatColor.GREEN + "10 minutes");
		listLore.add(" ");
		listLore.add("§6»" + ChatColor.GREEN + " Click to buy");

		scrollExp = createItem(Material.PAPER, ChatColor.GREEN + "Double Exp Scroll", listLore);
		inv.setItem(6, scrollExp);
		//////////////////////////////////////////////////////////
		
		listLore.clear();
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Description §e««");
		listLore.add(ChatColor.AQUA + "A scroll that can give double");
		listLore.add(ChatColor.AQUA + "ore coins to the player.");
		listLore.add(ChatColor.AQUA + "Double ore coins only works ");
		listLore.add(ChatColor.AQUA + "on certain block ore with ");
		listLore.add(ChatColor.AQUA + "limited active period.");
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Base Price §e««");
		listLore.add(ChatColor.YELLOW + "§e» 0.04 §6» " + ChatColor.GREEN + "§7[§a+§7]§a 1 Scroll Item");
		listLore.add(" ");
		listLore.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Special Effect §e««");
		listLore.add(ChatColor.YELLOW + "Double ORC §6» " + ChatColor.GREEN + "2x");
		listLore.add(ChatColor.YELLOW + "Duration §6» " + ChatColor.GREEN + "10 minutes");
		listLore.add(" ");
		listLore.add("§6»" + ChatColor.GREEN + " Click to buy");

		scrollORC = createItem(Material.PAPER, ChatColor.GREEN + "Double ORC Scroll", listLore);
		inv.setItem(7, scrollORC);
		
		//Fill the empty slot by Stained Glass Item
		for (int i=0; i < 18; i++){
			if (inv.getItem(i) == null){
				listLore.clear();
				ItemStack nullSlot = createItem(Material.STAINED_GLASS_PANE, ChatColor.GREEN + " ", listLore);
				inv.setItem(i, nullSlot);
			}
		}

		//////////////////////////////////////////////////////////
	}
	
	private ItemStack createItem(Material mt, String name, ArrayList<String> lore){
		ItemStack i = new ItemStack(mt, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		i.setItemMeta(im);
		return i;
	}
	
	public void show(Player p){
		p.openInventory(inv);
	}

	
	String Prefix;
	boolean isEmpty;
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		 if (!e.getInventory().getName().equalsIgnoreCase(inv.getName())) return;
		 if (e.getCurrentItem().getItemMeta() == null) return;
		 if (e.getInventory().getType().equals(InventoryType.PLAYER) || e.getInventory().getType().equals(InventoryType.CRAFTING)){
			 e.setCancelled(true);
		 }
		 
		 
		 if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Withdraw Ore Coins §e» 1 ORC")){
			 e.setCancelled(true);
			 withdrawCoin(e.getWhoClicked().getName(), 1);
			 if (isShopSuccess){
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.GREEN + "Successfully withdraw 1 Ore Coins!");
				isShopSuccess = false;
			 }
			 else {
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.RED + "You don't have enough Ore Coins!");
			 }
			 
			 //Close inventory
			 e.getWhoClicked().closeInventory();
		 }
		 
		 else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Withdraw Ore Coins §e» 0.5 ORC")){
			 e.setCancelled(true);
			 withdrawCoin(e.getWhoClicked().getName(), 0.5);
			 if (isShopSuccess){
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.GREEN + "Successfully withdraw 0.5 Ore Coins!");
				isShopSuccess = false;
			 }
			 else {
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.RED + "You don't have enough Ore Coins!");
			 }
			 
			 //Close inventory
			 e.getWhoClicked().closeInventory();
		 }
		 
		 else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Withdraw Ore Coins §e» 0.1 ORC")){
			 e.setCancelled(true);
			 withdrawCoin(e.getWhoClicked().getName(), 0.1);
			 if (isShopSuccess){
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.GREEN + "Successfully withdraw 0.1 Ore Coins!");
				isShopSuccess = false;
			 }
			 else {
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.RED + "You don't have enough Ore Coins!");
			 }
			 
			 //Close inventory
			 e.getWhoClicked().closeInventory();
		 }
		 
		 else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Duplicator Ore Scroll")){
			 e.setCancelled(true);
			 // Check player inventory slot
			 withdrawCoin(e.getWhoClicked().getName(), 0.02);
			 if (isShopSuccess){
				 ArrayList<String> lores = new ArrayList<String>();
				 lores.clear();
				 lores.add(" ");
				 lores.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Description §e««");
				 lores.add(ChatColor.AQUA + "A scroll that can duplicate blocks");
				 lores.add(ChatColor.AQUA + "when destroying block ore.");
				 lores.add(ChatColor.AQUA + "This item only works on some");
				 lores.add(ChatColor.AQUA + "type of ore only");
				 lores.add(" ");
				 lores.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Special Effect §e««");
				 lores.add(ChatColor.YELLOW + "Double Ore §6» " + ChatColor.GREEN + "2x");
				 lores.add(ChatColor.YELLOW + "Duration §6» " + ChatColor.GREEN + "8 minutes");
				 lores.add(" ");
				 lores.add("§6»" + ChatColor.GREEN + " Rightclick to use");
				 
				 e.getWhoClicked().getInventory().addItem(addScroll(Material.PAPER, ChatColor.GREEN + "Duplicator Ore Scroll", lores));
				 
				 
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.GREEN + "Successfully buy 1 Duplicator Ore Scroll by 0.02 Ore Coins!");
				isShopSuccess = false;
			 }
			 else {
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.RED + "You don't have enough Ore Coins!");
			 }
			 
			 //Close inventory
			 e.getWhoClicked().closeInventory();
		 }
		 
		 else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Exp Scroll")){
			 e.setCancelled(true);
			 
			 withdrawCoin(e.getWhoClicked().getName(), 0.03);
			 if (isShopSuccess){
				 ArrayList<String> lores = new ArrayList<String>();
				 lores.clear();
				 lores.add(" ");
				 lores.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Description §e««");
				 lores.add(ChatColor.AQUA + "A scroll that can give ");
				 lores.add(ChatColor.AQUA + "double exp to player.");
				 lores.add(ChatColor.AQUA + "Exp given only to certain ");
				 lores.add(ChatColor.AQUA + "ore block with limited ");
				 lores.add(ChatColor.AQUA + "active period.");
				 lores.add(" ");
					lores.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Special Effect §e««");
					lores.add(ChatColor.YELLOW + "Double Exp §6» " + ChatColor.GREEN + "2x");
					lores.add(ChatColor.YELLOW + "Duration §6» " + ChatColor.GREEN + "10 minutes");
					lores.add(" ");
					lores.add("§6»" + ChatColor.GREEN + " Rightclick to use");
				 
				 e.getWhoClicked().getInventory().addItem(addScroll(Material.PAPER, ChatColor.GREEN + "Double Exp Scroll", lores));
				 
				 
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.GREEN + "Successfully buy 1 Double Exp Scroll by 0.03 Ore Coins!");
				isShopSuccess = false;
			 }
			 else {
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.RED + "You don't have enough Ore Coins!");
			 }
			 
			 //Close inventory
			 e.getWhoClicked().closeInventory();
		 }
		 
		 else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Double ORC Scroll")){
			 e.setCancelled(true);
			 withdrawCoin(e.getWhoClicked().getName(), 0.04);
			 if (isShopSuccess){
				 ArrayList<String> lores = new ArrayList<String>();
				 lores.clear();
				 lores.add(" ");
				 lores.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Description §e««");
				 lores.add(ChatColor.AQUA + "A scroll that can give double");
					lores.add(ChatColor.AQUA + "ore coins to the player.");
					lores.add(ChatColor.AQUA + "Double ore coins only works");
					lores.add(ChatColor.AQUA + "on certain block ore with ");
					lores.add(ChatColor.AQUA + "limited active period.");
					lores.add(" ");
					lores.add(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Special Effect §e««");
					lores.add(ChatColor.YELLOW + "Double ORC §6» " + ChatColor.GREEN + "2x");
					lores.add(ChatColor.YELLOW + "Duration §6» " + ChatColor.GREEN + "10 minutes");
					lores.add(" ");
					lores.add("§6»" + ChatColor.GREEN + " Rightclick to use");
				 
				 e.getWhoClicked().getInventory().addItem(addScroll(Material.PAPER, ChatColor.GREEN + "Double ORC Scroll", lores));
				
				 
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.GREEN + "Successfully buy 1 Double ORC Scroll by 0.04 Ore Coins!");
				isShopSuccess = false;
			 }
			 else {
				Prefix = Config.getString("MinerCoin.Notification.Prefix");
				Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
				e.getWhoClicked().sendMessage(Prefix + ChatColor.RED + "You don't have enough Ore Coins!");
			 }
			 
			 //Close inventory
			 e.getWhoClicked().closeInventory();
		 }
		 
		 else {
			 e.setCancelled(true); 
		 }
		 e.setCancelled(true);
		 

	}
	
	boolean isShopSuccess = false;
	public double oreDecimal(double orc){
		configPath = new File(main.getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		
		String format = Config.getString("MinerCoin.OreCoin.DecimalFormat");
		
		DecimalFormat oreGenerated = new DecimalFormat(format);
		double rawOre = Double.valueOf(oreGenerated.format(orc));
		orc = rawOre;
		return orc;
	}
	
	private ItemStack addScroll(Material material, String itemName, ArrayList<String> lore){
		ItemStack item = new ItemStack(material, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(itemName);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private void withdrawCoin(String pl, double coin){
		
		playerPath = new File(main.getDataFolder(), "players.yml");
		playerConfig = YamlConfiguration.loadConfiguration(playerPath);
		
		configPath = new File(main.getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		
		double coins = playerConfig.getDouble("PlayerLevel." + pl + ".OreCoin");
		BigDecimal coinPlayer = BigDecimal.valueOf(coins);
		
		if (coinPlayer.doubleValue() >= coin){
			try {
				double coinP = coinPlayer.doubleValue();
				coinP -= coin;
				
				BigDecimal takeCoin = BigDecimal.valueOf(coinP);
				playerConfig.set("PlayerLevel." + pl + ".OreCoin", oreDecimal(takeCoin.doubleValue()));
				
				playerConfig.save(playerPath);
				
				//Do something to upgrade Bag Slot
				@SuppressWarnings("deprecation")
				EconomyResponse r = econ.depositPlayer(pl, countingMoney(coin));
				if (r.transactionSuccess()){
					isShopSuccess = true;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else {
			//Return;
			isShopSuccess = false;
		}
		
	}
}
