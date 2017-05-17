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
package me.waltster.Fantasy;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class PlayerMeta {
	private static HashMap<String, PlayerMeta> playerMap = new HashMap<String, PlayerMeta>();
	
	private String player;
	private boolean alive, needsRevived;
	private Race race;
	private Kit kit;
	
	private PlayerMeta(String player, boolean alive, Race r, Kit k){
		this.player = player;
		this.alive = false;
		this.race = r;
		this.kit = k;
	}
	
	public static PlayerMeta getPlayerMeta(String name){
		if(playerMap.get(name) == null){
			PlayerMeta meta = new PlayerMeta(name, false, Race.HUMAN, Kit.CIVILIAN);
			playerMap.put(name, meta);
		}
		
		return playerMap.get(name);
	}
	
	public static PlayerMeta getPlayerMeta(Player p){
		return getPlayerMeta(p.getName());
	}
	
	public Race getRace(){
		return race;
	}
	
	public void setRace(Race r){
		this.race = r;
	}
	
	public Kit getKit(){
		return kit;
	}
	
	public void setKit(Kit k){
		this.kit = k;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public void setAlive(boolean a){
		this.alive = a;
	}

	public void setNeedsRevived(boolean n){
		this.needsRevived = n;
	}
	
	public boolean needsRevived(){
		return this.needsRevived;
	}
}
