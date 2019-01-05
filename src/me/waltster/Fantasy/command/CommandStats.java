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
package me.waltster.Fantasy.command;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.waltster.Fantasy.StatType;
import me.waltster.Fantasy.manager.StatsManager;

public class CommandStats implements CommandExecutor {
    private StatsManager manager;

    public CommandStats(StatsManager manager) {
        this.manager = manager;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                List<StatType> types = new LinkedList<StatType>(
                        Arrays.asList(StatType.values()));
                Iterator<StatType> iterator = types.iterator();
                while (iterator.hasNext()) {
                    StatType type = iterator.next();
                    boolean keep = false;
                    for (String arg : args) {
                        if (type.name().toLowerCase()
                                .contains(arg.toLowerCase()))
                            keep = true;
                    }
                    if (!keep)
                        iterator.remove();
                }
                listStats((Player) sender,
                        types.toArray(new StatType[types.size()]));
            } else {
                listStats((Player) sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only a player can use that command!");
        }

        return true;
    }

    private void listStats(Player player) {
        listStats(player, StatType.values());
    }

    private void listStats(Player player, StatType[] stats) {
        String GRAY = ChatColor.GRAY.toString();
        String AQUA = ChatColor.AQUA.toString();

        player.sendMessage(GRAY + "=========[ " + ChatColor.GOLD.toString() + "Stats" + GRAY
                + " ]=========");

        for (StatType stat : stats) {
            if (stat == null)
                continue;
            String name = WordUtils.capitalize(stat.name().toLowerCase()
                    .replace('_', ' '));

            player.sendMessage(ChatColor.GOLD.toString() + name + ": " + AQUA
                    + manager.getStat(stat, player));
        }
        player.sendMessage(GRAY + "=========================");
    }
}
