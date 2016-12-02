package me.waltster.Fantasy.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.waltster.Fantasy.Kit;
import me.waltster.Fantasy.PlayerMeta;
import me.waltster.Fantasy.Race;
import net.md_5.bungee.api.ChatColor;

public class CommandRace implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			if(args.length > 0){
				if(Race.valueOf(ChatColor.stripColor(args[0].toUpperCase())) != null){
					Race r = Race.valueOf(ChatColor.stripColor(args[0].toUpperCase()));
					
					PlayerMeta.getPlayerMeta(player).setRace(r);
					player.sendMessage(ChatColor.GOLD + "Race " + ChatColor.WHITE + r.getName() + ChatColor.GOLD + " selected.");
					player.sendMessage(ChatColor.GOLD + "You will recieve it when you respawn");
				}else{
					player.sendMessage(ChatColor.RED + "Invalid race name. Type '/race' to open selector.");
				}
			}else{
				Race.showRaceSelector(player);
			}
		}else{
			sender.sendMessage(ChatColor.RED + "Only players can use the race command!");
		}
		
		return true;
	}
}
