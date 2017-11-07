package me.yurani.minercoin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.RegisteredServiceProvider;

public class BlockListener implements Listener{

	private Main main;
	File playerPath;
	FileConfiguration playerConfig;
	
	File blockPath;
	FileConfiguration blockConfig;
	
	File configPath;
	FileConfiguration Config;
	
	Scroll scroll;
	
	boolean isLevelUp = false;
	boolean isMaxLevel = false;
	
	ArrayList<String> blockList = new ArrayList<String>();
	ArrayList<String> allowedTools = new ArrayList<String>();
	
	private static Economy econ = null;
	
	public BlockListener(Main plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.main = plugin;
		scroll = new Scroll(plugin);
		
        if (!setupEconomy() ) {
            main.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", main.getDescription().getName()));
            main.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
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
	
	private double decimalDouble(double exp){
		DecimalFormat expGenerate = new DecimalFormat("#.##");
		double rawExp = Double.valueOf(expGenerate.format(exp));
		exp = rawExp;
		return exp;
		
	}
	
	public double oreDecimal(double orc){
		configPath = new File(main.getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		
		String format = Config.getString("MinerCoin.OreCoin.DecimalFormat");
		
		DecimalFormat oreGenerated = new DecimalFormat(format);
		double rawOre = Double.valueOf(oreGenerated.format(orc));
		orc = rawOre;
		return orc;
	}
	
	Player player;
	String breakItem;
	String Prefix;
	String moneySymbol;
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e) throws IOException{
		Player player = e.getPlayer();
		
		addBlock();
		adddAllowedTools();

		blockPath = new File(main.getDataFolder(), "blocks.yml");
		blockConfig = YamlConfiguration.loadConfiguration(blockPath);
		
		playerPath = new File(main.getDataFolder(), "players.yml");
		playerConfig = YamlConfiguration.loadConfiguration(playerPath);
		
		configPath = new File(main.getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		
		moneySymbol = Config.getString("MinerCoin.Currency.MoneySymbol");
		int amount = 1;
		
		// Detecting the breaking block
		if (e.getBlock().getType() == Material.STONE){		
			breakItem = "Stone";
		}
		else if (e.getBlock().getType() == Material.COBBLESTONE){		
			breakItem = "Cobblestone";
		}
		else if (e.getBlock().getType() == Material.IRON_ORE){		
			breakItem = "IronOre";
		}
		else if (e.getBlock().getType() == Material.COAL_ORE){		
			breakItem = "CoalOre";
		}
		else if (e.getBlock().getType() == Material.GOLD_ORE){		
			breakItem = "GoldOre";
		}
		else if (e.getBlock().getType() == Material.DIAMOND_ORE){		
			breakItem = "DiamondOre";
		}
		else if (e.getBlock().getType() == Material.EMERALD_ORE){		
			breakItem = "EmeraldOre";
		}
		else if (e.getBlock().getType() == Material.LAPIS_ORE){		
			breakItem = "LapisLazuliOre";
		}
		else if (e.getBlock().getType() == Material.REDSTONE_ORE){		
			breakItem = "RedstoneOre";
		}
		else if (e.getBlock().getType() == Material.QUARTZ_ORE){		
			breakItem = "NetherQuartzOre";
		}
		if (blockList.contains(e.getBlock().getType().toString())){
			if (playerConfig.contains("PlayerLevel." + player.getName())){
				String exp = playerConfig.getString("PlayerLevel." + player.getName() + ".EXP");
				int level = playerConfig.getInt("PlayerLevel." + player.getName() + ".Level");
				
				if (player.getGameMode() == GameMode.CREATIVE){
					
					return;
				}

				if (allowedTools.contains(player.getInventory().getItemInHand().getType().toString())){
					if (Config.getInt("MinerCoin.Level.MaxLevel") > level){
						
						double expMining = Double.parseDouble(exp);
						double blockExp = blockConfig.getDouble("BlockExp." + breakItem);
						double expRaw = expMining + blockExp * level / 3.5;
						
						
						playerConfig.set("PlayerLevel." + player.getName() + ".EXP", decimalDouble(expRaw));
						playerConfig.save(playerPath);
						
						double expGained = decimalDouble(blockExp * level / 3.5);
						
						if (Config.getBoolean("MinerCoin.Notification.BreakMessage") == true){
							Prefix = Config.getString("MinerCoin.Notification.Prefix");
							Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
							player.sendMessage(Prefix + ChatColor.GREEN + "Gained " + ChatColor.YELLOW + expGained + ChatColor.GREEN + " EXP!");
						}
						
						//oreCoinMining(player.getName(), breakItem);
						checkLevelUp(player);
						
						
						double orc = playerConfig.getDouble("PlayerLevel." + player.getName() + ".OreCoin");
						BigDecimal _orc = BigDecimal.valueOf(orc);
						
						double blockOrc = blockConfig.getDouble("OreCoin." + breakItem);
						BigDecimal _blockOrc = BigDecimal.valueOf(blockOrc);
						
						double orcRaw = _orc.doubleValue() + _blockOrc.doubleValue();
						BigDecimal result = BigDecimal.valueOf(orcRaw);
						
						playerConfig.set("PlayerLevel." + player.getName() + ".OreCoin", oreDecimal(result.doubleValue()));
						playerConfig.save(playerPath);
						
						
					}
				}
			}
			else {
				playerConfig.set("PlayerLevel." + player.getName() + ".Level", 1);
				playerConfig.set("PlayerLevel." + player.getName() + ".EXP", 2.5);
				playerConfig.set("PlayerLevel." + player.getName() + ".NextExp", 25);
				playerConfig.save(playerPath);
			}
		}
	}
	
	
	private void checkLevelUp(Player breaker) throws IOException{
		playerPath = new File(main.getDataFolder(), "players.yml");
		playerConfig = YamlConfiguration.loadConfiguration(playerPath);
		
		configPath = new File(main.getDataFolder(), "config.yml");
		Config = YamlConfiguration.loadConfiguration(configPath);
		
		moneySymbol = Config.getString("MinerCoin.Currency.MoneySymbol");
		
		int exp, maxExp, maxLevel;
		exp = playerConfig.getInt("PlayerLevel." + breaker.getName() + ".EXP");
		maxExp = playerConfig.getInt("PlayerLevel." + breaker.getName() + ".NextExp");
		maxLevel = Config.getInt("MinerCoin.Level.MaxLevel");
		
		int count = 0;
		while (count < maxLevel){
			count++;
			for (int i = 0; exp >= maxExp; i++){
				exp = playerConfig.getInt("PlayerLevel." + breaker.getName() + ".EXP");
				maxExp = playerConfig.getInt("PlayerLevel." + breaker.getName() + ".NextExp");
				int level = playerConfig.getInt("PlayerLevel." + breaker.getName() + ".Level");
				
				if (exp >= maxExp && level < maxLevel){
					level += 1;
					int rawMaxExp = (38 * level * level) + (78 * level + 64);
					int rawExp = exp - maxExp;
					playerConfig.set("PlayerLevel." + breaker.getName() + ".EXP", rawExp);
					playerConfig.set("PlayerLevel." + breaker.getName() + ".NextExp", rawMaxExp);
					playerConfig.set("PlayerLevel." + breaker.getName() + ".Level", level);
					playerConfig.save(playerPath);

					isLevelUp = true;
					
					if (isLevelUp){
						if (Config.getBoolean("MinerCoin.Notification.LevelupMessage") == true){
							level = playerConfig.getInt("PlayerLevel." + breaker.getName() + ".Level");
							int money = Config.getInt("MinerCoin.Reward.BaseMoney") * level;
							
							@SuppressWarnings("deprecation")
							EconomyResponse r = econ.depositPlayer(breaker.getName(), money);
							if (r.transactionSuccess()){
								
								Prefix = Config.getString("MinerCoin.Notification.Prefix");
								Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
								
								breaker.sendMessage(ChatColor.GREEN + "----------- REWARD -----------");
								breaker.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GREEN + moneySymbol + money);
								
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
									PlayerInventory inven = breaker.getInventory();
									
									Material items = Material.matchMaterial(splited[0]); //Get Material Type
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
									
									breaker.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GREEN + itemNotif + " §c» §e" + amount);
									
								}
								list.clear();
								breaker.sendMessage(ChatColor.GREEN + "------------------------------");
								
							}
							else {
								
								Prefix = Config.getString("MinerCoin.Notification.Prefix");
								Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
								player.sendMessage(Prefix + ChatColor.RED + "An error occured!");
							}
						}
						
						//Notification levelup to public
						Bukkit.broadcastMessage(Prefix + ChatColor.GOLD + breaker.getName() + ChatColor.GREEN + " have been Levelup! " + ChatColor.GOLD + "(Lv." + playerConfig.getString("PlayerLevel." + breaker.getName() + ".Level") + ")");
						isLevelUp = false;	
					}
					
		
				}

				
				String _maxLevel = Config.getString("MinerCoin.Level.MaxLevel");
				String _level = playerConfig.getString("PlayerLevel." + breaker.getName() + ".Level");
				if (_maxLevel.equals(_level)){
					isMaxLevel = true;
				}
				
				if (isMaxLevel){
					Prefix = Config.getString("MinerCoin.Notification.Prefix");
					Prefix = ChatColor.translateAlternateColorCodes('&', Prefix);
					
					breaker.sendMessage(Prefix + ChatColor.YELLOW + "You have been reached MAX LEVEL!");
					Bukkit.broadcastMessage(Prefix + ChatColor.GOLD + breaker.getName() + ChatColor.YELLOW + " have been reached MAX LEVEL!");
					
					playerConfig.set("PlayerLevel." + breaker.getName() + ".EXP", 0);
					
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
	
	private void addBlock(){
		blockList.clear();
		blockList.add("STONE");
		blockList.add("COBBLESTONE");
		blockList.add("IRON_ORE");
		blockList.add("COAL_ORE");
		blockList.add("GOLD_ORE");
		blockList.add("DIAMOND_ORE");
		blockList.add("EMERALD_ORE");
		blockList.add("LAPIS_ORE");
		blockList.add("REDSTONE_ORE");
		blockList.add("NETHER_WART_BLOCK");
	}
	
	private void adddAllowedTools(){
		allowedTools.clear();
		allowedTools.add("WOOD_PICKAXE");
		allowedTools.add("STONE_PICKAXE");
		allowedTools.add("IRON_PICKAXE");
		allowedTools.add("GOLD_PICKAXE");
		allowedTools.add("DIAMOND_PICKAXE");
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws IOException{
		Player player = e.getPlayer();
		playerPath = new File(main.getDataFolder(), "players.yml");
		playerConfig = YamlConfiguration.loadConfiguration(playerPath);
		
		if (!playerConfig.contains("PlayerLevel." + player.getName())){
			playerConfig.set("PlayerLevel." + player.getName() + ".Level", 1);
			playerConfig.set("PlayerLevel." + player.getName() + ".EXP", 0);
			playerConfig.set("PlayerLevel." + player.getName() + ".NextExp", 25);
			playerConfig.set("PlayerLevel." + player.getName() + ".OreCoin", 0);
			playerConfig.save(playerPath);
		}
	}
	
}
