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

public class CommandDie implements CommandExecutor{
	private FantasyMain main;
	
	public CommandDie(FantasyMain main){
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
			sender.sendMessage(ChatColor.RED + "Only players can issue the 'die' command");
		}
		
		return true;
	}
}
