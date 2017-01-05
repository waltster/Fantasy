package me.waltster.Fantasy.listener;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SoulboundListener implements Listener{
	
	@EventHandler
	public void onPlayerDropSoulbound(PlayerDropItemEvent event){
		Player p = event.getPlayer();
		Item i = event.getItemDrop();
		
		if(isSoulbound(i.getItemStack())){
			p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 1F, 0.5F);
		}
		
		event.getItemDrop().remove();
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Iterator<ItemStack> items = event.getDrops().iterator();
		
		while(items.hasNext()){
			if(isSoulbound(items.next())){
				items.remove();
			}
		}
	}
	
	public static boolean isSoulbound(ItemStack item){
		ItemMeta meta = item.getItemMeta();
		
		if(!item.hasItemMeta()){
		    return false;
		}
		
		if(meta.getDisplayName().contains(ChatColor.GOLD + "Soulbound")){
			return true;
		}
		
		return false;
	}
	
	public static void soulbind(ItemStack item){
		ItemMeta meta = item.getItemMeta();
		
		if(item.getType() != Material.AIR){
			if(!meta.hasLore()){
				meta.setLore(Arrays.asList(ChatColor.GOLD + "Soulbound"));
			}else{
				List<String> lore = meta.getLore();
				lore.add(ChatColor.GOLD + "Soulbound");
				meta.setLore(lore);
			}
		}
	}
}
