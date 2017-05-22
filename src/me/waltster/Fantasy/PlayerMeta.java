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

/**
 * This class is used to provide a way to store and retrieve player information relating to the current game.
 * Note that this is not static, and all unsaved data is lost when the server stops.
 */
public class PlayerMeta {
	private static HashMap<String, PlayerMeta> playerMap = new HashMap<String, PlayerMeta>();

	private boolean alive, needsRevived,abilityEnabled;
	private Race race;
	private Kit kit;

	/**
	 * Create a new meta object for a player.
	 * @param player The player that the metadata belongs to.
	 * @param alive True if the player is "alive" in the game, and false if the player is in the lobby or dead
	 * @param r The race that the player is.
	 * @param k The class that the player is.
	 */
	private PlayerMeta(boolean alive, Race r, Kit k){
		this.alive = false;
		this.abilityEnabled = false;
		this.race = r;
		this.kit = k;
	}

	/**
	 * Fetch the metadata for the player by their name.
	 * @param name The name of the player to fetch the metadata for.
	 * @return The metadata, or a blank object if not (inserts blank into map).
	 */
	public static PlayerMeta getPlayerMeta(String name){
		if(playerMap.get(name) == null){
			PlayerMeta meta = new PlayerMeta(false, Race.NONE, Kit.NONE);
			playerMap.put(name, meta);
		}

		return playerMap.get(name);
	}

	/**
	 * Fetch the metadata for the player by their object.
	 * @param p The payer to fetch the metadata for.
	 * @return The metadata, or blank object if not (see <i>getPlayerMeta(...)</i>)
	 */
	public static PlayerMeta getPlayerMeta(Player p){
		return getPlayerMeta(p.getName());
	}

	/**
	 * Get the race that the player is
	 * @return The race the player is currently playing as, or Race.NONE if none.
	 */
	public Race getRace(){
		return race;
	}

	/**
	 * Set the player's race.
	 * @param r The race to set for the player.
	 */
	public void setRace(Race r){
		this.race = r;
	}

	/**
	 * Get the player's current class.
	 * @return The class the player is playing as.
	 */
	public Kit getKit(){
		return kit;
	}

	/**
	 * Set the player's current class.
	 * @param k The class to set for the player.
	 */
	public void setKit(Kit k){
		this.kit = k;
	}

	/**
	 * Check if the player is in the game.
	 * @return True if the player is in the game, or false if not or otherwise.
	 */
	public boolean isAlive(){
		return alive;
	}

	/**
	 * Set if the player is in the game or not.
	 * @param a The value to set: true if alive, false if not.
	 */
	public void setAlive(boolean a){
		this.alive = a;
	}

	/**
	 * Set if the player needs to be revived (30s).
	 * @param n The value to set: true if alive, false if not.
	 */
	public void setNeedsRevived(boolean n){
		this.needsRevived = n;
	}

	/**
	 * Check if the player needs to be revived.
	 * @return True if the player needs to be revived, false if not.
	 */
	public boolean needsRevived(){
		return this.needsRevived;
	}
	
	public void setAbilityEnabled(boolean e){
		this.abilityEnabled = e;
	}
	
	public boolean abilityEnabled(){
		return this.abilityEnabled;
	}
}
