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
