package com.monowii.mwstop;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class stop extends JavaPlugin
{
	File bukkitConfigFile = new File("bukkit.yml");
	
	public void onEnable()
	{
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    System.out.println("Failed to submit data :(");
		}
		
		FileConfiguration bukkitConfig = YamlConfiguration.loadConfiguration(bukkitConfigFile);
		bukkitConfig.addDefault("aliases.stop", "mwstop");
		bukkitConfig.options().copyDefaults(true);
		
		try {
			bukkitConfig.save(bukkitConfigFile);
		} catch (IOException e) { }
		
		getConfig().addDefault("message.disconnect", "&2Server Offline NEWLINE &aCheck back later!");
        getConfig().options().copyDefaults(true);
        saveConfig();
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) 
	{
		if (cmd.getName().equalsIgnoreCase("mwstop") && (sender.isOp() || sender.hasPermission("mwStop.use")) )
		{
			String StopMsg = null;
			
			if (args.length == 0)
			{
				StopServer(StopMsg);
			}
			else
			{
				StringBuilder msg = new StringBuilder();
				
				for (int i = 0; i < args.length; i++) 
				{
					if (i > 0) {
						msg.append(" ");
					}
					msg.append(args[i]);
				}
				
				StopMsg = msg.toString();
				StopServer(StopMsg);
			}
		}
		return false;
	}
	
	
	public void StopServer(String StopMsg)
	{
		if (StopMsg == null)
		{
			StopMsg = getConfig().getString("message.disconnect");
		}
		StopMsg = ChatColor.translateAlternateColorCodes('&', StopMsg).replaceAll("NEWLINE", "\n");

		for (Player ply : getServer().getOnlinePlayers())
		{
			ply.kickPlayer(StopMsg);
		}
		
		getServer().shutdown();
	}
}
