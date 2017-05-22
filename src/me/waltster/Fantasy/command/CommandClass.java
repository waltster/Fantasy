/** 
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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.waltster.Fantasy.Kit;
import me.waltster.Fantasy.PlayerMeta;
import me.waltster.Fantasy.Race;
import net.md_5.bungee.api.ChatColor;

public class CommandClass implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			if(args.length > 0){
				if(Kit.valueOf(ChatColor.stripColor(args[0].toUpperCase())) != null){
					Kit k = Kit.valueOf(ChatColor.stripColor(args[0].toUpperCase()));
					
					PlayerMeta.getPlayerMeta(player).setKit(k);
					player.sendMessage(ChatColor.GOLD + "Class " + ChatColor.WHITE + k.getName() + ChatColor.GOLD + " selected.");
					player.sendMessage(ChatColor.GOLD + "You will recieve it when you respawn");
				}else{
					player.sendMessage(ChatColor.RED + "Invalid class name. Type '/class' to open selector.");
				}
			}else{
				Race.showRaceSelector(player);
			}
		}else{
			sender.sendMessage(ChatColor.RED + "Only players can use the class command!");
		}
		
		return true;
	}
}
