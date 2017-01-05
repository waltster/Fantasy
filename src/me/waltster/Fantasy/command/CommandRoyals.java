package me.waltster.Fantasy.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.StatType;

public class CommandRoyals implements CommandExecutor{
    private FantasyMain main;
    
    public CommandRoyals(FantasyMain main){
        this.main = main;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length == 1 && sender instanceof Player){
            if(args[0].toLowerCase() == "get"){
                sender.sendMessage(ChatColor.GOLD + "You have " + ChatColor.WHITE + main.getStatsManager().getStat(StatType.ROYALS, (Player)sender) + ChatColor.GOLD + " Royals");;
            }
        }else if(args.length < 3){
            sender.sendMessage(ChatColor.RED + "/royals <set/get/give/take> <player> <amount>");
            return true;
        }else if(args.length == 3){
            if(args[0].toLowerCase() == "set"){
                if(sender.hasPermission("fantasy.royals.set")){
                    Player p = Bukkit.getPlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    
                    if(p == null){
                        sender.sendMessage(ChatColor.RED + "Player not found");
                        return true;
                    }
                    
                    main.getStatsManager().setValue(StatType.ROYALS, p, amount);
                    sender.sendMessage(ChatColor.GREEN + "Set " + p.getName() + "'s Royals to " + ChatColor.WHITE + amount);

                }else{
                    sender.sendMessage(ChatColor.RED + "You don't have permission!");
                }
            }else if(args[0].toLowerCase() == "give"){
                if(sender.hasPermission("fantasy.royals.give")){
                    Player p = Bukkit.getPlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    
                    if(p == null){
                        sender.sendMessage(ChatColor.RED + "Player not found");
                        return true;
                    }
                    
                    main.getStatsManager().incrementStat(StatType.ROYALS, p, amount);
                    sender.sendMessage(ChatColor.GREEN + "Gave " + ChatColor.WHITE + amount + ChatColor.GREEN + " Royals to " + p.getName());

                }else{
                    sender.sendMessage(ChatColor.RED + "You don't have permission!");
                }
            }else if(args[0].toLowerCase() == "take"){
                if(sender.hasPermission("fantasy.royals.take")){
                    Player p = Bukkit.getPlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    
                    if(p == null){
                        sender.sendMessage(ChatColor.RED + "Player not found");
                        return true;
                    }
                    
                    main.getStatsManager().incrementStat(StatType.ROYALS, p, -amount);
                    sender.sendMessage(ChatColor.GREEN + "Took " + ChatColor.WHITE + amount + ChatColor.GREEN + " Royals from " + p.getName());
                }else{
                    sender.sendMessage(ChatColor.RED + "You don't have permission!");
                }
            }
        }
        
        return true;
    }
}
