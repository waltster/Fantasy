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
package me.waltster.Fantasy.manager;

import org.bukkit.entity.Player;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.StatType;

public class StatsManager {
    private FantasyMain plugin;
    private ConfigManager config;
    public static final int UNDEF_STAT = -42;

    public StatsManager(FantasyMain instance, ConfigManager config) {
        this.plugin = instance;
        this.config = config;
    }

    public int getStat(StatType s, Player p) {
        if(s == StatType.SHOTBOW_XP){
        	// TODO: Return ShotbowXP
        }
        
        return config.getConfiguration("stats.yml").getConfig().getInt(p.getName() + "." + s.name());
    }

    public void setValue(StatType s, Player p, int value) {
        if(s == StatType.SHOTBOW_XP){
        	// TODO: SET ShotbowXP
        }
        
        config.getConfiguration("stats.yml").getConfig().set(p.getName() + "." + s.name(), value);
        config.getConfiguration("stats.yml").save();
    }

    public void incrementStat(StatType s, Player p) {
        incrementStat(s, p, 1);
    }

    public void incrementStat(StatType s, Player p, int amount) {
        if(p.hasPermission("silver")){
            amount *= 2;
        }else if(p.hasPermission("gold")){
            amount *= 3;
        }else if(p.hasPermission("platinum")){
            amount *= 4;
        }
        
        setValue(s, p, getStat(s, p) + amount);
    }
}
