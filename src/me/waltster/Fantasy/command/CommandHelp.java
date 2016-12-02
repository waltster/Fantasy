package me.waltster.Fantasy.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.waltster.Fantasy.Util;
import net.md_5.bungee.api.ChatColor;

public class CommandHelp implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player p = (Player)sender;
			
			if(args.length == 0){
				Util.clearChat(p);
				p.sendMessage(ChatColor.GOLD + "========= " + ChatColor.WHITE + "Help" + ChatColor.GOLD + " =========");
				p.sendMessage(ChatColor.GOLD + "How to Play");
				p.sendMessage(ChatColor.GOLD + "Rules");
				p.sendMessage(ChatColor.GOLD + "Restart/Death");
				p.sendMessage(ChatColor.GOLD + "Commands");
				
				/*if(p.isOp() || p.hasPermission("fantasy.advanced_help")){
					p.sendMessage(ChatColor.GOLD + "City");
				}*/
			}else if(args.length > 0){
				p.sendMessage("" + args.length);
				if(args[0].toLowerCase().contains("how")){
					Util.clearChat(p);
					p.sendMessage(ChatColor.GOLD + "====== " + ChatColor.WHITE + "How to Play" + ChatColor.GOLD + " ======");
					p.sendMessage(ChatColor.GOLD + "The goal of " + ChatColor.WHITE + "Fantasy" + ChatColor.GOLD + " is to advance your race and yourself from bare-minimum to advanced civilization.");
					p.sendMessage(ChatColor.GOLD + "Go on quests, fight bosses, and build cities.");
					p.sendMessage(ChatColor.GOLD + "You can learn to use magic and unlock hidden secrets deep in dunegons or across huge oceans.");
					p.sendMessage("");
					p.sendMessage(ChatColor.GOLD + "Start off with nothing in a city your race owns then begin by working as your class and earning gold. ");
					p.sendMessage(ChatColor.GOLD + "Then purchase the supplies for your quest and get out of dodge!");
					p.sendMessage("");
					p.sendMessage(ChatColor.GOLD + "Choose a race and class, then join a city by clicking on of its signs and you're off!");
				}else if(args[0].toLowerCase().contains("rules")){
					Util.clearChat(p);
					p.sendMessage(ChatColor.GOLD + "========= " + ChatColor.WHITE + "Rules" + ChatColor.GOLD + " =========");
					p.sendMessage(ChatColor.GOLD + "1. You cannot destroy your own city or your own race");
					p.sendMessage(ChatColor.GOLD + "2. Trade may not be influenced by outside promises. Only");
					p.sendMessage(ChatColor.GOLD + "   trade for gold for your own protection.");
					p.sendMessage(ChatColor.GOLD + "3. Once you die you lose your progress. Show courtsey to");
					p.sendMessage(ChatColor.GOLD + "   to other races just as you'd want them to.");
					p.sendMessage(ChatColor.GOLD + "4. Please do not ask staff to revive you, only another player");
					p.sendMessage(ChatColor.GOLD + "   can do that for you.");
				}else if(args[0].toLowerCase().contains("restart")){
					Util.clearChat(p);
					p.sendMessage(ChatColor.GOLD + "======== " + ChatColor.WHITE + "Restart" + ChatColor.GOLD + " ========");
					p.sendMessage(ChatColor.GOLD + "You restart in Fantasy by dying. Once you die in the game you lose your progress and restart.");
					p.sendMessage(ChatColor.GOLD + "This gives you the oppertunity to choose a new race/class.");
					p.sendMessage(ChatColor.GOLD + "");
					p.sendMessage(ChatColor.GOLD + "If you'd like to restart without adding a death to your stats, just type: /die");
				}else if(args[0].toLowerCase().contains("commands")){
					Util.clearChat(p);
					p.sendMessage(ChatColor.GOLD + "======= " + ChatColor.WHITE + "Commands" + ChatColor.GOLD + " =======");
					p.sendMessage(ChatColor.GOLD + "Commands: ");
					p.sendMessage(ChatColor.GOLD + "  /help  - Display help messages");
					p.sendMessage(ChatColor.GOLD + "  /race  - Open race selector");
					p.sendMessage(ChatColor.GOLD + "  /class - Open class selector");
					p.sendMessage(ChatColor.GOLD + "  /die   - Restart in lobby");
				}
			}
		}			
		
		return true;
	}
}
