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
package me.waltster.Fantasy.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.waltster.Fantasy.PlayerMeta;
import me.waltster.Fantasy.Race;
import me.waltster.Fantasy.Util;
import net.md_5.bungee.api.ChatColor;

public class ChatListener implements Listener {
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		PlayerMeta meta = PlayerMeta.getPlayerMeta(e.getPlayer());
		
		e.setCancelled(true);
		
		String msg = "";
		
		if(meta.getRace() != Race.NONE){
			msg = ChatColor.GOLD + "[" + Util.getChatColor(meta.getRace()) + meta.getRace().getName() + ChatColor.GOLD + "] " + ChatColor.WHITE + e.getPlayer().getDisplayName() + ": " + e.getMessage();
		}else{
			msg = ChatColor.GOLD + "[L] " + ChatColor.WHITE + e.getPlayer().getDisplayName() + ": " + e.getMessage();
		}
		
		e.setMessage(msg);
		//Bukkit.broadcastMessage(msg);
	}
}
