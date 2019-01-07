package me.waltster.Fantasy.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.waltster.Fantasy.FantasyMain;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.EntityPlayer;

public class EnderFurnaceListener implements Listener{
	private FantasyMain instance;
	
	/**
	 * 
	 * @param instance
	 */
	public EnderFurnaceListener(FantasyMain instance) {
		this.instance = instance;
	}
	
	/**
	 * 
	 * @param e
	 */
	@EventHandler
	public void onFurnaceOpen(PlayerInteractEvent e) {
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		Block b = e.getClickedBlock();
		
		if(b.getType() != Material.FURNACE) {
			return;
		}
		
		Location location = b.getLocation();
		Player player = e.getPlayer();
		
		if(!instance.getEnderFurnaceManager().getFurnaceLocations().contains(location.toString())) {
			return;
		}
		
		EntityPlayer handle = ((CraftPlayer)player).getHandle();
		
		handle.openContainer(instance.getEnderFurnaceManager().getFurnace(player));
		player.sendMessage(ChatColor.AQUA + "This is an ender furnace. Anything you smelt here will be safe.");
	}
	
	/**
	 * 
	 * @param e
	 */
	@EventHandler
	public void onFurnaceBreak(BlockBreakEvent e) {
		if(instance.getEnderFurnaceManager().getFurnaceLocations().contains(e.getBlock().getLocation().toString())) {
			e.setCancelled(true);
		}
	}

}
