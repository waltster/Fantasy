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
		
		if(meta.getRace() != Race.NONE){
			e.setMessage(ChatColor.GOLD + "[" + Util.getChatColor(meta.getRace()) + meta.getRace().getName() + ChatColor.GOLD + "] " + ChatColor.WHITE + e.getMessage());
		}else{
			e.setMessage(ChatColor.GOLD + "[" + Util.getChatColor(meta.getRace()) + "Lobby" + ChatColor.GOLD + "] " + ChatColor.WHITE + e.getMessage());
		}
	}
}
