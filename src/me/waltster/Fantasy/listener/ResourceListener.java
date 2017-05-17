/*******************************************************************************
 * Copyright 2014 stuntguy3000 (Luke Anderson) and coasterman10.
 *  
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 ******************************************************************************/
package me.waltster.Fantasy.listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.Kit;
import me.waltster.Fantasy.PlayerMeta;

public class ResourceListener implements Listener {
    private class Resource {
        public final Material drop;
        public final Integer xp;
        public final Integer delay;

        public Resource(Material drop, Integer xp, Integer delay) {
            this.drop = drop;
            this.xp = xp;
            this.delay = delay;
        }
    }

    private final FantasyMain plugin;
    private final HashMap<Material, Resource> resources = new HashMap<Material, Resource>();
    private final HashSet<Location> queue = new HashSet<Location>();
    private final Set<Location> diamonds = new HashSet<Location>();
    private Random rand = new Random();

    public ResourceListener(FantasyMain plugin) {
        this.plugin = plugin;
        
        addResource(Material.COAL_ORE, 8, 30);
        addResource(Material.IRON_ORE, 10, 50);
        addResource(Material.GOLD_ORE, 15, 60);
        addResource(Material.LAPIS_ORE, 10, 50);
        addResource(Material.DIAMOND_ORE, 12, 70);
        addResource(Material.EMERALD_ORE, 18, 80);
        addResource(Material.REDSTONE_ORE, 10, 40);
        addResource(Material.GLOWING_REDSTONE_ORE, 10, 40);
        addResource(Material.LOG, 2, 30);
        addResource(Material.GRAVEL, 2, 20);
        addResource(Material.MELON_BLOCK, 0, 20);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = false)
    public void onResourceBreak(BlockBreakEvent e) {
        if(e.getBlock().getType() == Material.WALL_SIGN || e.getBlock().getType() == Material.SIGN_POST){
            Sign sign = (Sign)e.getBlock().getState();
            
            if(sign.getLine(0).contains(ChatColor.WHITE + "[" + ChatColor.GOLD + "City" + ChatColor.WHITE + "]")){
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "You cannot break city signs");
            }
        }
        
        if(e.getBlock().getType() == Material.LAPIS_ORE){
            e.setCancelled(true);
            Dye d = new Dye();
            d.setColor(DyeColor.BLUE);
            ItemStack i = d.toItemStack();
            i.setAmount(getDropQuantity(Material.LAPIS_ORE));
            
            e.getPlayer().getInventory().addItem(i.clone());
            e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.STEP_SOUND, e.getBlock().getTypeId());
            queueRespawn(e.getBlock());
            return;
        }
        
        if (resources.containsKey(e.getBlock().getType())) {
            e.setCancelled(true);
            breakResource(e.getPlayer(), e.getBlock());
            e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.STEP_SOUND, e.getBlock().getTypeId());
        } else if (queue.contains(e.getBlock().getLocation())) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler(ignoreCancelled = false)
    public void placeResource(BlockPlaceEvent e) {
        if (resources.containsKey(e.getBlock().getType())) {
            e.setCancelled(true);
        }
    }

    public void loadDiamonds(Set<Location> diamondLocations) {
        for (Location loc : diamondLocations) {
            if (loc.getBlock().getType() == Material.DIAMOND_ORE)
                loc.getBlock().setType(Material.AIR);
            diamonds.add(loc);
        }
    }

    public void spawnDiamonds() {
        for (Location loc : diamonds)
            loc.getBlock().setType(Material.DIAMOND_ORE);
    }

    private void breakResource(Player player, Block block) {
        Material type = block.getType();
        Kit kit = PlayerMeta.getPlayerMeta(player).getKit();
        Resource resource = resources.get(type);

        if (type.equals(Material.GRAVEL)) {
            ItemStack[] drops = getGravelDrops();
            for (ItemStack stack : drops)
                if (stack.getAmount() > 0)
                    player.getInventory().addItem(stack);
        }else {
            Material dropType = resource.drop;
            int qty = getDropQuantity(type);
            if ((type.name().contains("ORE") && kit == Kit.MINER)
                    || (type.name().contains("LOG") && kit == Kit.FARMER || (type.name().contains("MELON") || type.name().contains("CROPS") && kit == Kit.FARMER)))
                qty *= rand.nextFloat() < 0.9 ? 2 : 1;
            player.getInventory().addItem(new ItemStack(dropType, qty));
        }

        if (resource.xp > 0) {
            player.giveExp(resource.xp);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F,
                    (rand.nextFloat() * 0.2F) + 0.9F);
        }

        queueRespawn(block);
    }

    private void queueRespawn(final Block block) {
        final Material type = block.getType();
        block.setType(getRespawnMaterial(type));
        queue.add(block.getLocation());
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                block.setType(type);
                queue.remove(block.getLocation());
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
            }
        }, resources.get(type).delay * 20L);
    }

    private int getDropQuantity(Material type) {
        switch (type) {
        case MELON_BLOCK:
            return 3 + rand.nextInt(5);
        case REDSTONE_ORE:
        case GLOWING_REDSTONE_ORE:
            return 4 + (rand.nextBoolean() ? 1 : 0);
        default:
            return 1;
        }
    }

    private Material getRespawnMaterial(Material type) {
        switch (type) {
        case LOG:
        case MELON_BLOCK:
            return Material.AIR;
        default:
            return Material.COBBLESTONE;
        }
    }

    private ItemStack[] getGravelDrops() {
        ItemStack arrows = new ItemStack(Material.ARROW, Math.max(
                rand.nextInt(5) - 2, 0));
        ItemStack flint = new ItemStack(Material.FLINT, Math.max(
                rand.nextInt(4) - 2, 0));
        ItemStack feathers = new ItemStack(Material.FEATHER, Math.max(
                rand.nextInt(4) - 2, 0));
        ItemStack string = new ItemStack(Material.STRING, Math.max(
                rand.nextInt(5) - 3, 0));
        ItemStack bones = new ItemStack(Material.BONE, Math.max(
                rand.nextInt(4) - 2, 0));
        return new ItemStack[] { arrows, flint, feathers, string, bones };
    }
    
    private void addResource(Material type, int xp, int delay) {
        resources.put(type, new Resource(getDropMaterial(type), xp, delay));
    }

    private Material getDropMaterial(Material type) {
        switch (type) {
        case COAL_ORE:
            return Material.COAL;
        case DIAMOND_ORE:
            return Material.DIAMOND;
        case LAPIS_ORE:
            return Material.LAPIS_BLOCK;
        case EMERALD_ORE:
            return Material.EMERALD;
        case GLOWING_REDSTONE_ORE:
        case REDSTONE_ORE:
            return Material.REDSTONE;
        case MELON_BLOCK:
            return Material.MELON;
        case GRAVEL:
            return null;
        default:
            return type;
        }
    }
}
