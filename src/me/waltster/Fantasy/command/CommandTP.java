package me.waltster.Fantasy.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTP implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender.hasPermission("fantasy.command.fartp") || sender.isOp()){
            if(args.length < 5|| args.length != 5){
                sender.sendMessage(ChatColor.RED + "/tp <player> <world> <x> <y> <z>");
            }else if(args.length == 5){
                Player p = Bukkit.getPlayer(args[0]);
                World w = Bukkit.createWorld(new WorldCreator(args[1]));
                double x = Double.parseDouble(args[2]);
                double y = Double.parseDouble(args[3]);
                double z = Double.parseDouble(args[4]);
                
                if(p != null){
                    p.teleport(new Location(w, x, y, z));
                }else{
                    sender.sendMessage(ChatColor.RED + "Player not found");
                }
            }
        }
        
        return true;
    }
}
