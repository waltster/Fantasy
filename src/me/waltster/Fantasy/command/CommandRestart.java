package me.waltster.Fantasy.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.Kit;
import me.waltster.Fantasy.PlayerMeta;
import me.waltster.Fantasy.Race;
import me.waltster.Fantasy.Util;

public class CommandRestart implements CommandExecutor{
	private FantasyMain main;
	
	public CommandRestart(FantasyMain main){
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player p = (Player)sender;
			PlayerMeta meta = PlayerMeta.getPlayerMeta(p);

			p.sendMessage(ChatColor.GREEN + "Restarting your game");
			meta.setAlive(false);
			meta.setNeedsRevived(false);
			meta.setKit(Kit.NONE);
			meta.setRace(Race.NONE);
			Util.sendPlayerToLobby(p, main.getLobbySpawn());
		}else{
			sender.sendMessage(ChatColor.RED + "Only players can issue the restart command");
		}
		
		return true;
	}
}
