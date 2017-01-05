package me.waltster.Fantasy.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.waltster.Fantasy.Util;

public class CommandRaceBuy implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player p = (Player)sender;
            p.sendMessage(ChatColor.GREEN + "Opening buy selector");
            Util.showRaceBuySelector(p);
        }else{
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
        }
        
        return false;
    }
}
