package me.waltster.Fantasy.listener;

import org.bukkit.Bukkit;
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
			msg = ChatColor.GOLD + "[" + Util.getChatColor(meta.getRace()) + meta.getRace().getName() + ChatColor.GOLD + "] " + ChatColor.WHITE + e.getMessage();
		}else{
			msg = ChatColor.GOLD + "[" + Util.getChatColor(meta.getRace()) + "Lobby" + ChatColor.GOLD + "] " + ChatColor.WHITE + e.getMessage();
		}
		
		Bukkit.broadcastMessage(msg);
	}
}
