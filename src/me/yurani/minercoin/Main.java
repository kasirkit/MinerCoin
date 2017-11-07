package me.yurani.minercoin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	File playerPath;
	FileConfiguration playerConfig;
	
	File configPath;
	FileConfiguration Config;
	
	File blockPath;
	FileConfiguration blockConfig;
	
	boolean isLevelUp = false;
	boolean isMaxLevel = false;
	
	private static Economy econ = null;
	
	private Shop shop;

	public String players;
	
	Scroll scroll;
	
	@Override
	public void onEnable(){
		new BlockListener(this);
		new DropChanger(this);
		new Scroll(this);
		scroll = new Scroll(this);
	
		shop = new Shop(this);
		
		getLogger().info("Miner Coin has been enabled!");
		createPlayerConfig();
		createBlockConfig();
		createConfig();
		
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public void onDisable(){
		getLogger().info("Miner Coin has been disabled!");
	}
	
	
	String Prefix;
	String moneySymbol;
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player player = (Player) sender;
		playerPath = new File(getDataFolder(), "players.yml");
		playerConfig = YamlConfiguration.loadConfiguration(playerPath);
		
		configPath = new File(getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		
		blockPath = new File(getDataFolder(), "blocks.yml");
		blockConfig = YamlConfiguration.loadConfiguration(blockPath);
		
		moneySymbol = Config.getString("MinerCoin.Currency.MoneySymbol");
		
		if (cmd.getName().equalsIgnoreCase("miner") && sender instanceof Player){
			int arg = args.length;
			if (arg > 0){
				if (args[0].equalsIgnoreCase("status")){
					player.sendMessage(ChatColor.GOLD + "--- MINER LEVEL ---");
					player.sendMessage(ChatColor.GREEN + "Name: " + ChatColor.GOLD + player.getName());
					String level = playerConfig.getString("PlayerLevel." + player.getName() + ".Level");
					player.sendMessage(ChatColor.GREEN + "Level: " + ChatColor.AQUA + level);
					String maxLevel = Config.getString("MinerCoin.Level.MaxLevel");
					player.sendMessage(ChatColor.GREEN + "Max Level: " + ChatColor.AQUA + maxLevel);
					String exp = playerConfig.getString("PlayerLevel." + player.getName() + ".EXP");
					String nextExp = playerConfig.getString("PlayerLevel." + player.getName() + ".NextExp");
					player.sendMessage(ChatColor.GREEN + "EXP: " + ChatColor.AQUA + exp + "/" + nextExp);
					
					if (maxLevel.equals(level)){
						player.sendMessage(ChatColor.GREEN + "Status: " + ChatColor.RED + "MAX LEVEL!");
					}
					else {
						player.sendMessage(ChatColor.GREEN + "Status: " + ChatColor.YELLOW + "Leveling...");
					}
					
					player.sendMessage(ChatColor.GOLD + "--- Ore Coins ---");
					double oreCoin = playerConfig.getDouble("PlayerLevel." + player.getName() + ".OreCoin");
					BigDecimal _orc = BigDecimal.valueOf(oreCoin);
					
					String orcGet = Config.getString("MinerCoin.OreCoin.Price.1ORC");
					double Baseorc = Double.parseDouble(orcGet);
					double nom = _orc.doubleValue() * Baseorc; // 1ORC = 287500
					Double _nom = new Double(nom);
					int calc = _nom.intValue();
					player.sendMessage(ChatColor.GREEN + "Saldo: " + ChatColor.YELLOW + _orc + " ORC");
					player.sendMessage(ChatColor.GREEN + "Payment: " + ChatColor.YELLOW + moneySymbol + NumberFormat.getNumberInstance(Locale.US).format(calc));
					
					player.sendMessage(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Special Effect §e««");
					
					
					return true;

				}
				
				if (args[0].equalsIgnoreCase("view")){
					if (arg > 0){
						if (arg < 2){
							Prefix = Config.getString("MinerCoin.Notification.Prefix");
							Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
							player.sendMessage(Prefix + ChatColor.RED + "Please use the correct command! ");
							player.sendMessage( ChatColor.GOLD + " §6» /miner view <player>");
							return true;
						}
				
						if (sender instanceof Player){
							if (player.hasPermission("minercoin.view.other")){
								
								Player target = Bukkit.getServer().getPlayerExact(args[1]);
								if (target != null){
									player.sendMessage(ChatColor.GOLD + "--- MINER LEVEL ---");
									player.sendMessage(ChatColor.GREEN + "Name: " + ChatColor.GOLD + args[1]);
									String level = playerConfig.getString("PlayerLevel." + args[1] + ".Level");
									player.sendMessage(ChatColor.GREEN + "Level: " + ChatColor.AQUA + level);
									String maxLevel = Config.getString("MinerCoin.Level.MaxLevel");
									player.sendMessage(ChatColor.GREEN + "Max Level: " + ChatColor.AQUA + maxLevel);
									String exp = playerConfig.getString("PlayerLevel." + args[1] + ".EXP");
									String nextExp = playerConfig.getString("PlayerLevel." + args[1] + ".NextExp");
									player.sendMessage(ChatColor.GREEN + "EXP: " + ChatColor.AQUA + exp + "/" + nextExp);
									
									if (maxLevel.equals(level)){
										player.sendMessage(ChatColor.GREEN + "Status: " + ChatColor.RED + "MAX LEVEL!");
									}
									else {
										player.sendMessage(ChatColor.GREEN + "Status: " + ChatColor.YELLOW + "Leveling...");
									}
									
									player.sendMessage(ChatColor.GOLD + "--- Ore Coins ---");
									double oreCoin = playerConfig.getDouble("PlayerLevel." + args[1] + ".OreCoin");
									BigDecimal _orc = BigDecimal.valueOf(oreCoin);
									
									String orcGet = Config.getString("MinerCoin.OreCoin.Price.1ORC");
									double Baseorc = Double.parseDouble(orcGet);
									double nom = _orc.doubleValue() * Baseorc; // 1ORC = 287500
									Double _nom = new Double(nom);
									int calc = _nom.intValue();
									player.sendMessage(ChatColor.GREEN + "Saldo: " + ChatColor.YELLOW + _orc + " ORC");
									player.sendMessage(ChatColor.GREEN + "Payment: " + ChatColor.YELLOW + moneySymbol + NumberFormat.getNumberInstance(Locale.US).format(calc));
									
									player.sendMessage(ChatColor.GREEN + "§e»» " + ChatColor.GREEN + "Special Effect §e««");
		
									
									return true;
								}
								else {
									Prefix = Config.getString("MinerCoin.Notification.Prefix");
									Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
									player.sendMessage(Prefix + ChatColor.RED + "Player's not found!");
									return true;
								}
							}
							else {
								Prefix = Config.getString("MinerCoin.Notification.Prefix");
								Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
								player.sendMessage(Prefix + ChatColor.RED + "You don't have permission to use this command!");
								return true;
							}
						}
					}
				}
				
				if (args[0].equalsIgnoreCase("reset")){
					if (arg > 0){
						if (arg < 2){
							Prefix = Config.getString("MinerCoin.Notification.Prefix");
							Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
							player.sendMessage(Prefix + ChatColor.RED + "Please use the correct command! ");
							player.sendMessage( ChatColor.GOLD + " §6» /miner reset <player>");
							return true;
						}
				
						if (sender instanceof Player){
							if (player.hasPermission("minercoin.level.reset")){

								Player target = Bukkit.getServer().getPlayerExact(args[1]);
								if (target != null){
									
									playerConfig.set("PlayerLevel." + args[1] + ".EXP", 0);
									playerConfig.set("PlayerLevel." + args[1] + ".NextExp", 25);
									playerConfig.set("PlayerLevel." + args[1] + ".Level", 1);
									
									try {
										playerConfig.save(playerPath);
										
										Prefix = Config.getString("MinerCoin.Notification.Prefix");
										Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
										player.sendMessage(Prefix + ChatColor.GREEN + "Successfully reset the player stats! ");
										target.sendMessage(Prefix + ChatColor.RED + "Your stats has been reset by administrator! ");
										
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									return true;
									
								}
								else {
									Prefix = Config.getString("MinerCoin.Notification.Prefix");
									Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
									player.sendMessage(Prefix + ChatColor.RED + "Player's not found!");
									return true;
								}
							}
							else {
								Prefix = Config.getString("MinerCoin.Notification.Prefix");
								Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
								player.sendMessage(Prefix + ChatColor.RED + "You don't have permission to use this command!");
								return true;
							}
						}
					}
				}
				
				if (args[0].equalsIgnoreCase("help")){
					player.sendMessage(ChatColor.GOLD + "--- " + ChatColor.GREEN + "MINER COIN HELP" + ChatColor.GOLD + " ---");
					player.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.GOLD + "v1.0");
					player.sendMessage(ChatColor.GREEN + "Author: " + ChatColor.GOLD + "Yusril Takeuchi");
					player.sendMessage(ChatColor.GOLD + "---------------------");
					player.sendMessage(ChatColor.GREEN + "/miner" + " §6» " + ChatColor.AQUA + "Default Miner Coin Command");
					player.sendMessage(ChatColor.GREEN + "/miner help" + " §6» " + ChatColor.AQUA + "Open help command");
					player.sendMessage(ChatColor.GREEN + "/miner status" + " §6» " + ChatColor.AQUA + "View the player stats");
					player.sendMessage(ChatColor.GREEN + "/miner xp" + " §6» " + ChatColor.AQUA + "View the experience of the block");
					if (player.hasPermission("minercoin.view.other")){
						player.sendMessage(ChatColor.GREEN + "/miner view" + " §6» " + ChatColor.AQUA + "View the other person stats");
					}
					
					if (player.hasPermission("minercoin.level.addxp")){
						player.sendMessage(ChatColor.GREEN + "/miner addxp" + " §6» " + ChatColor.AQUA + "Add Exp to the player");
					}
					
					if (player.hasPermission("minercoin.level.reset")){
						player.sendMessage(ChatColor.GREEN + "/miner reset" + " §6» " + ChatColor.AQUA + "Reset the player status");
					}
					player.sendMessage(ChatColor.GREEN + "/miner orc base" + " §6» " + ChatColor.AQUA + "View the ore coins price of the block");
					player.sendMessage(ChatColor.GREEN + "/miner orc shop" + " §6» " + ChatColor.AQUA + "Open ore coins shop");
					player.sendMessage(ChatColor.GREEN + "/miner orc transfer" + " §6» " + ChatColor.AQUA + "Transfer ore coins to another player");
					if (player.hasPermission("minercoin.reload")){
						player.sendMessage(ChatColor.GREEN + "/miner reload" + " §6» " + ChatColor.AQUA + "Reloading the plugin");
					}
				}
				
				if (args[0].equalsIgnoreCase("xp")){
					player.sendMessage(ChatColor.GOLD + "--- Experience ---");

					List<String> blockName = new ArrayList<String>();
					List<Double> blockExp = new ArrayList<Double>();
					int level = playerConfig.getInt("PlayerLevel." + player.getName() + ".Level");
					
					ConfigurationSection sec = blockConfig.getConfigurationSection("BlockExp.");
					if (sec != null) {
					    for (String key : sec.getKeys(false)) {
					       blockName.add(key);
					    }
					}
					
					for(String ble : blockConfig.getConfigurationSection("BlockExp").getKeys(false)){
						double expBlock = blockConfig.getDouble("BlockExp." + ble);
						blockExp.add(decimalDouble(expBlock * level / 3.5));
					}
					
					//Result
					for (int i = 0; i < blockName.size(); i++){
						String outputBlock = blockName.get(i).replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
						
						player.sendMessage(ChatColor.GREEN + outputBlock + " §c» " + ChatColor.YELLOW + blockExp.get(i) + " XP");
					}
					return true;

				}
				
				if (args[0].equalsIgnoreCase("orc")){
					if (arg > 1 && args[1].equalsIgnoreCase("base")){
						
						player.sendMessage(ChatColor.GOLD + "--- ORC Block Price ---");

						List<String> blockName = new ArrayList<String>();
						List<BigDecimal> blockOrc = new ArrayList<BigDecimal>();
						
						ConfigurationSection sec = blockConfig.getConfigurationSection("OreCoin.");
						if (sec != null) {
						    for (String key : sec.getKeys(false)) {
						       blockName.add(key);
						    }
						}
						
						for(String ble : blockConfig.getConfigurationSection("OreCoin").getKeys(false)){
							
					
							double orcBlock = blockConfig.getDouble("OreCoin." + ble);
							BigDecimal _orc = BigDecimal.valueOf(orcBlock);
							blockOrc.add(_orc);
						}
						
						//Result
						for (int i = 0; i < blockName.size(); i++){
							String outputBlock = blockName.get(i).replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
							
							player.sendMessage(ChatColor.GREEN + outputBlock + " §c» " + ChatColor.YELLOW + blockOrc.get(i) + " ORC");
						}
						return true;
					}
					
					else if (arg > 1 && args[1].equalsIgnoreCase("shop")){
					
						shop.show(player.getPlayer());
						return true;
					}
					
					else if (arg > 1 && args[1].equalsIgnoreCase("transfer")){
						if (arg > 2){
							Player target = Bukkit.getServer().getPlayerExact(args[2]);
							if (target != null){
								if (arg > 3){
									
									double depo = Double.parseDouble(args[3]);
									double coins = playerConfig.getDouble("PlayerLevel." + player.getName() + ".OreCoin");
									BigDecimal coinPlayer = BigDecimal.valueOf(coins);
									if (coinPlayer.doubleValue() >= depo){
										
										//Subtract sender ORC
										double coinP = coinPlayer.doubleValue();
										coinP -= depo;
										
										BigDecimal takeCoin = BigDecimal.valueOf(coinP);
										playerConfig.set("PlayerLevel." + player.getName() + ".OreCoin", oreDecimal(takeCoin.doubleValue()));
										try {
											playerConfig.save(playerPath);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										//Giving target Ore Coins
										double targetCoin = playerConfig.getDouble("PlayerLevel." + target.getName() + ".OreCoin");
										BigDecimal coinTarget = BigDecimal.valueOf(targetCoin);
										double cTarget = coinTarget.doubleValue();
										cTarget += depo;
										
										BigDecimal getCoin = BigDecimal.valueOf(cTarget);
										playerConfig.set("PlayerLevel." + target.getName() + ".OreCoin", oreDecimal(getCoin.doubleValue()));
										try {
											playerConfig.save(playerPath);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										//Notification
										Prefix = Config.getString("MinerCoin.Notification.Prefix");
										Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
										player.sendMessage(Prefix + ChatColor.GREEN + "Successfully transfer " + ChatColor.YELLOW + depo + " ORC " + ChatColor.GREEN + "to " + target.getName() + "!");
										target.sendMessage(Prefix + ChatColor.GREEN + player.getName() + " has giving you " + ChatColor.YELLOW + depo + " ORC" + ChatColor.GREEN + "!");
										return true;
									}
									else {
										Prefix = Config.getString("MinerCoin.Notification.Prefix");
										Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
										player.sendMessage(Prefix + ChatColor.RED + "You don't have enough Ore Coins!");
									}
									
								}
								else {
									Prefix = Config.getString("MinerCoin.Notification.Prefix");
									Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
									player.sendMessage(Prefix + ChatColor.RED + "Please use the correct command! ");
									player.sendMessage(ChatColor.GOLD + " §6» /miner orc transfer <player> <orcValue>");
								}
							}
							else {
								Prefix = Config.getString("MinerCoin.Notification.Prefix");
								Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
								player.sendMessage(Prefix + ChatColor.RED + "Player's not found!");
							}
						}
						else {
							Prefix = Config.getString("MinerCoin.Notification.Prefix");
							Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
							player.sendMessage(Prefix + ChatColor.RED + "Please use the correct command! ");
							player.sendMessage(ChatColor.GOLD + " §6» /miner orc transfer <player> <orcValue>");
						}
						
						return true;
					}
					
					else{
						Prefix = Config.getString("MinerCoin.Notification.Prefix");
						Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
						player.sendMessage(Prefix + ChatColor.RED + "Please use the correct command! ");
						player.sendMessage(ChatColor.GOLD + " §6» /miner orc (base, shop, transfer)");
						return true;
					}
				
				}
				
				
				if (args[0].equalsIgnoreCase("addxp")){
					if (arg > 0){
						if (arg > 1){
							if (player.hasPermission("minercoin.level.addxp")){
								
								Player target = Bukkit.getServer().getPlayerExact(args[2]);
								if (target != null){
									int exp = playerConfig.getInt("PlayerLevel." + target.getName() + ".EXP");
									int rawExp = exp + Integer.parseInt(args[1]);
									playerConfig.set("PlayerLevel." + target.getName() + ".EXP", rawExp);
									
									int maxLevel = Config.getInt("MinerCoin.Level.MaxLevel");
									int level = playerConfig.getInt("PlayerLevel." + target.getName() + ".Level");
									
									if (level < maxLevel){
										Prefix = Config.getString("MinerCoin.Notification.Prefix");
										Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
										player.sendMessage(Prefix + ChatColor.GREEN + "Successfully added exp to the player!");
										try {
											playerConfig.save(playerPath);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										try {
											checkLevelUp(target);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										
								
									}
									else {
										
										Prefix = Config.getString("MinerCoin.Notification.Prefix");
										Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
										player.sendMessage(Prefix + ChatColor.RED + "The player is already Max Level!");
										return true;
									}
								}
								else {
									
									Prefix = Config.getString("MinerCoin.Notification.Prefix");
									Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
									player.sendMessage(Prefix + ChatColor.RED + "Player's not found!");
									return true;
								}
							}
							else {
								
								Prefix = Config.getString("MinerCoin.Notification.Prefix");
								Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
								player.sendMessage(Prefix + ChatColor.RED + "You don't have permission to use this command!");
								return true;
							}
						}
						else {
							
							Prefix = Config.getString("MinerCoin.Notification.Prefix");
							Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
							player.sendMessage(Prefix + ChatColor.RED + "Please use the correct command! ");
							player.sendMessage(ChatColor.GOLD + " §6» /miner addxp <expValue> <player>");
							return true;
						}
					}
					else {
						
						Prefix = Config.getString("MinerCoin.Notification.Prefix");
						Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
						player.sendMessage(Prefix + ChatColor.RED + "Please use the correct command! ");
						player.sendMessage(ChatColor.GOLD + " §6» /miner addxp <expValue> <player>");
						return true;
					}
				}
				
				if (args[0].equalsIgnoreCase("reload")){
					if (player.hasPermission("minercoin.reload")){
						getServer().getPluginManager().disablePlugin(this);
						getServer().getPluginManager().enablePlugin(this);
						
						Prefix = Config.getString("MinerCoin.Notification.Prefix");
						Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
						player.sendMessage(Prefix + ChatColor.GREEN + "The Plugin Reloaded succesfully");
					}
					else {
						Prefix = Config.getString("MinerCoin.Notification.Prefix");
						Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
						player.sendMessage(Prefix + ChatColor.RED + "You don't have permission to use this command!");
						return true;
					}
				}
				
				
			}
			else {
				player.sendMessage(ChatColor.GOLD + "--- " + ChatColor.GREEN + "MINER COIN HELP" + ChatColor.GOLD + " ---");
				player.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.GOLD + "v1.0");
				player.sendMessage(ChatColor.GREEN + "Author: " + ChatColor.GOLD + "Yusril Takeuchi");
				player.sendMessage(ChatColor.GOLD + "---------------------");
				player.sendMessage(ChatColor.GREEN + "/miner" + " §6» " + ChatColor.AQUA + "Default Miner Coin Command");
				player.sendMessage(ChatColor.GREEN + "/miner help" + " §6» " + ChatColor.AQUA + "Open help command");
				player.sendMessage(ChatColor.GREEN + "/miner status" + " §6» " + ChatColor.AQUA + "View the player stats");
				player.sendMessage(ChatColor.GREEN + "/miner xp" + " §6» " + ChatColor.AQUA + "View the experience of the block");
				if (player.hasPermission("minercoin.view.other")){
					player.sendMessage(ChatColor.GREEN + "/miner view" + " §6» " + ChatColor.AQUA + "View the other person stats");
				}
				if (player.hasPermission("minercoin.level.addxp")){
					player.sendMessage(ChatColor.GREEN + "/miner addxp" + " §6» " + ChatColor.AQUA + "Add Exp to the player");
				}
				
				if (player.hasPermission("minercoin.level.reset")){
					player.sendMessage(ChatColor.GREEN + "/miner reset" + " §6» " + ChatColor.AQUA + "Reset the player status");
				}
				player.sendMessage(ChatColor.GREEN + "/miner orc base" + " §6» " + ChatColor.AQUA + "View the ore coins price of the block");
				player.sendMessage(ChatColor.GREEN + "/miner orc shop" + " §6» " + ChatColor.AQUA + "Open ore coins shop");
				player.sendMessage(ChatColor.GREEN + "/miner orc transfer" + " §6» " + ChatColor.AQUA + "Transfer ore coins to another player");
				if (player.hasPermission("minercoin.reload")){
					player.sendMessage(ChatColor.GREEN + "/miner reload" + " §6» " + ChatColor.AQUA + "Reloading the plugin");
				}
			}
		}
		return true;
	}
	
	private double decimalDouble(double exp){
		DecimalFormat expGenerate = new DecimalFormat("#.##");
		double rawExp = Double.valueOf(expGenerate.format(exp));
		exp = rawExp;
		return exp;
		
	}
	
	private void checkLevelUp(Player target) throws IOException{
		playerPath = new File(getDataFolder(), "players.yml");
		playerConfig = YamlConfiguration.loadConfiguration(playerPath);
		
		configPath = new File(getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		
		moneySymbol = Config.getString("MinerCoin.Currency.MoneySymbol");
		
		int exp, maxExp, maxLevel;
		exp = playerConfig.getInt("PlayerLevel." + target.getName() + ".EXP");
		maxExp = playerConfig.getInt("PlayerLevel." + target.getName() + ".NextExp");
		maxLevel = Config.getInt("MinerCoin.Level.MaxLevel");
		
		int count = 0;
		while (count < maxLevel){
			count++;
			for (int i = 0; exp >= maxExp; i++){
				exp = playerConfig.getInt("PlayerLevel." + target.getName() + ".EXP");
				maxExp = playerConfig.getInt("PlayerLevel." + target.getName() + ".NextExp");
				int level = playerConfig.getInt("PlayerLevel." + target.getName() + ".Level");
				
				if (exp >= maxExp && level < maxLevel){
					level += 1;
					int rawMaxExp = (38 * level * level) + (78 * level + 64);
					int rawExp = exp - maxExp;
					playerConfig.set("PlayerLevel." + target.getName() + ".EXP", rawExp);
					playerConfig.set("PlayerLevel." + target.getName() + ".NextExp", rawMaxExp);
					playerConfig.set("PlayerLevel." + target.getName() + ".Level", level);
					playerConfig.save(playerPath);

					isLevelUp = true;
					
					if (isLevelUp){
						if (Config.getBoolean("MinerCoin.Notification.LevelupMessage") == true){
							level = playerConfig.getInt("PlayerLevel." + target.getName() + ".Level");
							int money = Config.getInt("MinerCoin.Reward.BaseMoney") * level;
							
							@SuppressWarnings("deprecation")
							EconomyResponse r = econ.depositPlayer(target.getName(), money);
							if (r.transactionSuccess()){
								
								Prefix = Config.getString("MinerCoin.Notification.Prefix");
								Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
							
								target.sendMessage(ChatColor.GREEN + "----------- REWARD -----------");
								target.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GREEN + moneySymbol + money);
								
								//Try to new 
								List<String> list = new ArrayList<String>();
								if (Config.getStringList("MinerCoin.Reward.Kit.Level." + level) != null){
									List<String> rewardlist = Config.getStringList("MinerCoin.Reward.Kit.Level." + level);
									list = rewardlist;
									if (rewardlist.size() < 1){
										rewardlist = Config.getStringList("MinerCoin.Reward.Kit.Level.Default");
										list = rewardlist;
									}
								}
							
								String[] reward = list.toArray(new String[0]);
								
								//looping the item list in Config
								for (String entry : reward){
									String[] splited = entry.split(" ");
									
									//Giving reward to the player
									PlayerInventory inven = target.getInventory();
									
									Material items = Material.matchMaterial(splited[0]); //Get Material Type (Ex: IRON_SWORD)
									String am = splited[1]; //Get amount value (ex: 5 or 10-25)
									int num1, num2;
									int amount = 0;
									if (am.contains("-")){
										String[] sp = am.split("-");
										num1 = Integer.parseInt(sp[0]);
										num2 = Integer.parseInt(sp[1]);
										
										amount = randomStack(num1, num2);
									}
									else {
										amount = Integer.parseInt(am);
									}
									
									ItemStack item = new ItemStack(items, amount);
									inven.addItem(item);
									
									//Notification reward
									String itemNotif;
									itemNotif = splited[0];
									if (itemNotif.contains("_")){
										itemNotif = itemNotif.replace("_", " ");
									}
									
									target.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GREEN + itemNotif + " §c» §e" + amount);
			
								}
								list.clear();
								target.sendMessage(ChatColor.GREEN + "------------------------------");
								
							}
							else {
								return;
							}
						}
						
						//Notification levelup to public
						Bukkit.broadcastMessage(Prefix + ChatColor.GOLD + target.getName() + ChatColor.GREEN + " have been Levelup! " + ChatColor.GOLD + "(Lv." + playerConfig.getString("PlayerLevel." + target.getName() + ".Level") + ")");
						
						isLevelUp = false;	
					}
					
		
				}
				
				String _maxLevel = Config.getString("MinerCoin.Level.MaxLevel");
				String _level = playerConfig.getString("PlayerLevel." + target.getName() + ".Level");
				if (_maxLevel.equals(_level)){
					isMaxLevel = true;
				}
				
				if (isMaxLevel){
					Prefix = Config.getString("MinerCoin.Notification.Prefix");
					Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
					
					target.sendMessage(Prefix + ChatColor.YELLOW + "You have been reached MAX LEVEL!");
					Bukkit.broadcastMessage(Prefix + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " have been reached MAX LEVEL!");
					
					playerConfig.set("PlayerLevel." + target.getName() + ".EXP", 0);
					
					try {
						playerConfig.save(playerPath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					isMaxLevel = false;
				}
			}
		}
	}
	
	private int randomStack(int n1, int n2){
		Random rnd = new Random();
		int range = n2 - n1 + 1;
		n2 = rnd.nextInt(range) + n1;
		return n2;
	}
	
	private void createPlayerConfig(){
		File playerPath = new File(getDataFolder(), "players.yml");
		FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerPath);
		if (!playerPath.exists()){
			playerConfig.set("PlayerLevel", null);
			try {
				playerConfig.save(playerPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public double oreDecimal(double orc){
		configPath = new File(getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		String format = Config.getString("MinerCoin.OreCoin.DecimalFormat");
		
		DecimalFormat orcGenerate = new DecimalFormat(format);
		double rawOrc = Double.parseDouble(orcGenerate.format(orc));
		orc = rawOrc;
		return orc;
		
	}
	
	
	private void createBlockConfig(){
		File blockPath = new File(getDataFolder(), "blocks.yml");
		FileConfiguration blockConfig = YamlConfiguration.loadConfiguration(blockPath);
		if (!blockPath.exists()){
			if (!blockConfig.contains("BlockExp.Stone")){
				blockConfig.set("BlockExp.Stone", 2.5);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!blockConfig.contains("OreCoin.Stone")){
				BigDecimal stone = BigDecimal.valueOf(0.000011);
				blockConfig.set("OreCoin.Stone", stone);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!blockConfig.contains("BlockExp.Cobblestone")){
				blockConfig.set("BlockExp.Cobblestone", 1.3);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!blockConfig.contains("OreCoin.Cobblestone")){
				BigDecimal cobblestone = BigDecimal.valueOf(0.000032);
				blockConfig.set("OreCoin.Cobblestone", cobblestone);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!blockConfig.contains("BlockExp.IronOre")){
				blockConfig.set("BlockExp.IronOre", 3.7);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!blockConfig.contains("OreCoin.IronOre")){
				BigDecimal ironore = BigDecimal.valueOf(0.000053);
				blockConfig.set("OreCoin.IronOre", ironore);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!blockConfig.contains("BlockExp.CoalOre")){
				blockConfig.set("BlockExp.CoalOre", 3.1);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!blockConfig.contains("OreCoin.CoalOre")){
				BigDecimal coalore = BigDecimal.valueOf(0.000048);
				blockConfig.set("OreCoin.CoalOre", coalore);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!blockConfig.contains("BlockExp.GoldOre")){
				blockConfig.set("BlockExp.GoldOre", 4.5);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!blockConfig.contains("OreCoin.GoldOre")){
				BigDecimal goldore = BigDecimal.valueOf(0.000066);
				blockConfig.set("OreCoin.GoldOre", goldore);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!blockConfig.contains("BlockExp.DiamondOre")){
				blockConfig.set("BlockExp.DiamondOre", 5.3);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!blockConfig.contains("OreCoin.DiamondOre")){
				BigDecimal diamondore = BigDecimal.valueOf(0.000098);
				blockConfig.set("OreCoin.DiamondOre", diamondore);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!blockConfig.contains("BlockExp.EmeraldOre")){
				blockConfig.set("BlockExp.EmeraldOre", 5.6);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!blockConfig.contains("OreCoin.EmeraldOre")){
				BigDecimal emeraldore = BigDecimal.valueOf(0.00015);
				blockConfig.set("OreCoin.EmeraldOre", emeraldore);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!blockConfig.contains("BlockExp.LapisLazuliOre")){
				blockConfig.set("BlockExp.LapisLazuliOre", 4.3);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!blockConfig.contains("OreCoin.LapisLazuliOre")){
				BigDecimal lapislazuliore = BigDecimal.valueOf(0.000060);
				blockConfig.set("OreCoin.LapisLazuliOre", lapislazuliore);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!blockConfig.contains("BlockExp.RedstoneOre")){
				blockConfig.set("BlockExp.RedstoneOre", 3.8);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!blockConfig.contains("OreCoin.RedstoneOre")){
				BigDecimal redstoneore = BigDecimal.valueOf(0.000044);
				blockConfig.set("OreCoin.RedstoneOre", redstoneore);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!blockConfig.contains("BlockExp.NetherQuartzOre")){
				blockConfig.set("BlockExp.NetherQuartzOre", 2.8);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!blockConfig.contains("OreCoin.NetherQuartzOre")){
				BigDecimal netherore = BigDecimal.valueOf(0.000033);
				blockConfig.set("OreCoin.NetherQuartzOre", netherore);
				try {
					blockConfig.save(blockPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	private void createConfig(){
		saveDefaultConfig();
	}
	
}
