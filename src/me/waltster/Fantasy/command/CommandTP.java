/*
 * Copyright 2016-2017 Walter Pach, all rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
