/*
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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.SplashPotion;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import me.waltster.Fantasy.listener.SoulboundListener;

/**
 * Used to provide Kits to the gamemode.
 * 
 * @author Walt Pach (walt@waltster.me)
 */
public enum Kit {
	/**
	 * Human kits.
	 */
	CIVILIAN(Material.BREAD, 0){
		{
			lore.add("You are the civilian.");
			lore.add("");
			lore.add("You are the basic human,");
			lore.add("you live in your own home");
			lore.add("but love to explore and");
			lore.add("work hard. You have basic");
			lore.add("tools and clothes.");

			spawnItems.add(new ItemStack(Material.WOOD_SWORD));
			spawnItems.add(new ItemStack(Material.WOOD_AXE));
			spawnItems.add(new ItemStack(Material.MELON, 15));

			armor = new ItemStack[]{
					new ItemStack(Material.AIR),
					new ItemStack(Material.LEATHER_LEGGINGS),
					new ItemStack(Material.LEATHER_CHESTPLATE),
					new ItemStack(Material.AIR),
			};
		}
	},
	BUILDER(Material.WORKBENCH, 5000){
		{
			lore.add("You are the builder.");
			lore.add("");
			lore.add("You have the materials to build");
			lore.add("your towers, cities, and houses");
			lore.add("that can reach to the sky.");
			lore.add("Whether you build walls around");
			lore.add("cities, or houses for your friends");
			lore.add("you've got the materials.");

			spawnItems.add(new ItemStack(Material.WOOD_SWORD));
			spawnItems.add(new ItemStack(Material.STONE_AXE));
			spawnItems.add(new ItemStack(Material.STONE_PICKAXE));
			spawnItems.add(new ItemStack(Material.MELON, 15));

			armor = new ItemStack[]{
					new ItemStack(Material.AIR),
					new ItemStack(Material.LEATHER_LEGGINGS),
					new ItemStack(Material.LEATHER_CHESTPLATE),
					new ItemStack(Material.AIR),
			};
		}
	},
	FARMER(Material.WHEAT, 5000){
		{
			lore.add("You are the farmer.");
			lore.add("");
			lore.add("You are 100% a farmer. You've");
			lore.add("spent your time from childhood");
			lore.add("farming the ground. Now you're");
			lore.add("a master of the ground, and you");
			lore.add("provide for your friends and family.");
			lore.add("Enjoy plenty of food, and 2x when");
			lore.add("foraging.");

			spawnItems.add(new ItemStack(Material.WOOD_SWORD));
			spawnItems.add(new ItemStack(Material.WOOD_SPADE));
			spawnItems.add(new ItemStack(Material.SEEDS, 32));
			spawnItems.add(new ItemStack(Material.MELON_SEEDS));
			spawnItems.add(new ItemStack(Material.WHEAT, 2));
			spawnItems.add(new ItemStack(Material.MELON, 15));

			armor = new ItemStack[]{
					new ItemStack(Material.AIR),
					new ItemStack(Material.LEATHER_LEGGINGS),
					new ItemStack(Material.LEATHER_CHESTPLATE),
					new ItemStack(Material.AIR),
			};
		}
	},
	MARINER(Material.BOAT_BIRCH, 5000){
		{
			lore.add("You are the sailor.");
			lore.add("");
			lore.add("Your expertiece makes you a");
			lore.add("formitable foe in any sea-based");
			lore.add("combat. You get +1 attack when");
			lore.add("in water, and if you're in a boat");
			lore.add("then you get +2 defense.");
		}
	},
	/**
	 * Dwarvish classes
	 */
	MINER(Material.IRON_PICKAXE, 0){
		{
			lore.add("You are the miner.");
			lore.add("");
			lore.add("With your advanced skill and");
			lore.add("dedication with your pickaxe");
			lore.add("you have the renouned ability");
			lore.add("to recieve 2x the materials with");
			lore.add("some mines. As well as a portable");
			lore.add("furnace with coal.");

			spawnItems.add(new ItemStack(Material.WOOD_SWORD));
			spawnItems.add(new ItemStack(Material.STONE_PICKAXE));
			spawnItems.get(1).addEnchantment(Enchantment.DIG_SPEED, 1);
			spawnItems.add(new ItemStack(Material.COAL, 5));
			spawnItems.add(new ItemStack(Material.FURNACE, 1));

			armor = new ItemStack[]{
					new ItemStack(Material.AIR),
					new ItemStack(Material.LEATHER_LEGGINGS),
					new ItemStack(Material.LEATHER_CHESTPLATE),
					new ItemStack(Material.AIR),
			};
		}
	},
	BLACKSMITH(Material.ANVIL, 5000){
		{
			lore.add("You are the blacksmith.");
			lore.add("");
			lore.add("With your natural skill at");
			lore.add("crafting metal into usefull");
			lore.add("items, you've mastered the");
			lore.add("ability to turn your smelted");
			lore.add("ingots into true art. You are");
			lore.add("immune to fire.");

			spawnItems.add(new ItemStack(Material.STONE_SWORD));
			spawnItems.add(new ItemStack(Material.WOOD_AXE));
			spawnItems.add(new ItemStack(Material.WORKBENCH));
			spawnItems.add(new ItemStack(Material.MELON, 15));

			armor = new ItemStack[]{
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.IRON_CHESTPLATE),
					new ItemStack(Material.AIR),
			};
		}
	},
	WARRIOR(Material.IRON_SWORD, 7000){
		{
			lore.add("You are the warrior.");
			lore.add("");
			lore.add("With your natural dwarvish");
			lore.add("afinity to battle, you are");
			lore.add("absolutely the right choice");
			lore.add("to weild a battle axe. You're");
			lore.add("equiped with a set of armor");
			lore.add("too. It's a good thing the");
			lore.add("dwarves have you on their side!");

			spawnItems.add(new ItemStack(Material.STONE_SWORD));
			spawnItems.get(0).addEnchantment(Enchantment.KNOCKBACK, 1);
			spawnItems.add(new ItemStack(Material.IRON_AXE));
			spawnItems.get(1).addEnchantment(Enchantment.DAMAGE_ALL, 2);
			spawnItems.get(1).addEnchantment(Enchantment.DURABILITY, 1);
			spawnItems.add(new ItemStack(Material.MELON, 15));

			armor = new ItemStack[]{
					new ItemStack(Material.LEATHER_BOOTS),
					new ItemStack(Material.LEATHER_LEGGINGS),
					new ItemStack(Material.IRON_CHESTPLATE),
					new ItemStack(Material.IRON_HELMET),
			};

			armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		}
	},

	/**
	 * Elvish classes
	 */
	ARCHER(Material.BOW, 5000){
		{
			lore.add("You are the archer.");
			lore.add("");
			lore.add("Strong and gracefull you draw");
			lore.add("your bow. It's taken you");
			lore.add("years to master the skill, but");
			lore.add("now you are renouned as one");
			lore.add("of the best in the land.");
			lore.add("With your light equipment, you");
			lore.add("are given a speed boost while");
			lore.add("wearing light armor.");

			spawnItems.add(new ItemStack(Material.BOW));
			spawnItems.add(new ItemStack(Material.ARROW, 16));
			spawnItems.add(new ItemStack(Material.MELON, 15));

			armor = new ItemStack[]{
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.LEATHER_CHESTPLATE),
					new ItemStack(Material.AIR),
			};
		}
	},
	ENCHANTER(Material.EXP_BOTTLE, 5000){
        {
            lore.add("You are the enchanter.");
            lore.add("");
            lore.add("Expirience? You got it.");
            lore.add("Use your talent for infusing");
            lore.add("magic with physical objects");
            lore.add("to become one of the most powerful");
            lore.add("adversaries on the map. Spawn");
            lore.add("with a dagger, xp, and a few");
            lore.add("books to help you out.");
            lore.add("");
            lore.add("This class can use Enchanting Tables");

            spawnItems.add(new ItemStack(Material.WOOD_SWORD));
            spawnItems.add(new ItemStack(Material.EXP_BOTTLE, 5));
            spawnItems.add(new ItemStack(Material.BOOK, 8));
            spawnItems.add(new ItemStack(Material.BOOK_AND_QUILL, 1));
            spawnItems.add(new ItemStack(Material.MELON, 10));

            armor = new ItemStack[]{
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.LEATHER_CHESTPLATE),
                    new ItemStack(Material.AIR),
            };
        }
    },
	MAN_IN_BLACK(Material.COAL, 5000){
    	{
    		lore.add("You are the spark.");
    		lore.add("");
    		lore.add("Flames and sparks follow your path,");
    		lore.add("as long as you aren't on anything");
    		lore.add("flammible. You also have a small knife,");
    		lore.add("but you're not the strongest person");
    		lore.add("");
    		lore.add("Avoid flames and water at all costs!");
    		
    		spawnItems.add(new ItemStack(Material.WOOD_SWORD));
    		
    		Potion harmingPotion = new Potion(PotionType.INSTANT_DAMAGE, 1, true);
    		ItemStack potion = harmingPotion.toItemStack(1);
    		ItemMeta meta = potion.getItemMeta();
    		meta.setDisplayName(ChatColor.DARK_RED + "Breath");
    		meta.setLore(new ArrayList<String>());
    		meta.getLore().add(ChatColor.GOLD + "Use with caution");
    		potion.setItemMeta(meta);
    		
    		
    		spawnItems.add(potion);
    		spawnItems.add(new ItemStack(Material.ENDER_PEARL, 5));
    		
    		ItemStack abilityToggle = new ItemStack(Material.APPLE);
    		meta = abilityToggle.getItemMeta();
    		meta.setDisplayName(ChatColor.DARK_RED + "Toggle Ability");
    		meta.setLore(new ArrayList<String>());
    		meta.getLore().add(ChatColor.GOLD + "Click to toggle your");
    		meta.getLore().add(ChatColor.GOLD + "ghost ability.");
    		abilityToggle.setItemMeta(meta);
    		spawnItems.add(abilityToggle);
    		
            spawnItems.add(new ItemStack(Material.MELON, 10));

            armor = new ItemStack[]{
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
            };
    	}
    },
	SPARKY(Material.APPLE, 5000){
    	{
    		lore.add("You are the spark.");
    		lore.add("");
    		lore.add("Flames and sparks follow your path,");
    		lore.add("as long as you aren't on anything");
    		lore.add("flammible. You also have a small knife,");
    		lore.add("but you're not the strongest person");
    		
    		spawnItems.add(new ItemStack(Material.WOOD_SWORD));
    	
    		Potion harmingPotion = new Potion(PotionType.INSTANT_DAMAGE, 1, true);
    		ItemStack potion = harmingPotion.toItemStack(1);
    		ItemMeta meta = potion.getItemMeta();
    		meta.setDisplayName(ChatColor.DARK_RED + "Breath");
    		meta.setLore(new ArrayList<String>());
    		meta.getLore().add(ChatColor.GOLD + "Use with caution");
    		potion.setItemMeta(meta);
    		
    		spawnItems.add(potion);
    		spawnItems.add(new ItemStack(Material.ENDER_PEARL, 5));
    		
    		ItemStack abilityToggle = new ItemStack(Material.APPLE);
    		meta = abilityToggle.getItemMeta();
    		meta.setDisplayName(ChatColor.DARK_RED + "Toggle Ability");
    		meta.setLore(new ArrayList<String>());
    		meta.getLore().add(ChatColor.GOLD + "Click to toggle your");
    		meta.getLore().add(ChatColor.GOLD + "ghost ability.");
    		abilityToggle.setItemMeta(meta);
    		spawnItems.add(abilityToggle);
    		
            spawnItems.add(new ItemStack(Material.MELON, 10));

            armor = new ItemStack[]{
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
            };
    	}
    },
	NONE(Material.BEDROCK, 0){
		{
		}
	};

	/**
	 * Initializes each of the classes.
	 */
	static{
		for(Kit k : values()){
			k.init();
		}
	}

	Race race;
	ItemStack icon;
	List<String> lore = new ArrayList<String>();
	List<ItemStack> spawnItems = new ArrayList<ItemStack>();
	ItemStack[] armor;
	int cost;

	/**
	 * New instance of Kit
	 *
	 * @param m The material to show on the kit selector
	 * @param cost The cost, in ShotbowXP, of this kit
	 */
	Kit(Material m, int cost){
		icon = new ItemStack(m);
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(getName());
		meta.setLore(new ArrayList<String>());
		icon.setItemMeta(meta);
		this.cost = cost;
	}

	/**
	 * Initialize the race. Called statically on line #268.
	 */
	private void init(){
		for(int i = 0; i < lore.size(); i++){
			String s = lore.get(i);
			s = ChatColor.AQUA + s;
			lore.set(i, s);
		}

		ItemMeta meta = icon.getItemMeta();
		meta.setLore(lore);
		icon.setItemMeta(meta);
	}

	/**
	 * Give a player a kit's contents and effects.
	 *
	 * @param p The player to give the kit to.
	 */
	public void giveKitToPlayer(Player p){
		PlayerInventory inv = p.getInventory();
		inv.clear();

		for(PotionEffect effect : p.getActivePotionEffects()){
			p.removePotionEffect(effect.getType());
		}

		for(ItemStack item : spawnItems){
			ItemStack i = item.clone();
			SoulboundListener.soulbind(i);
			inv.addItem(i);
		}

		for(ItemStack item : this.armor){
			SoulboundListener.soulbind(item);
		}

		inv.setArmorContents(this.armor);

		addEffects(p);
	}

	/**
	 * Return the cost of this kit.
	 *
	 * @return
	 */
	public int getCost(){
	    return cost;
	}

	/**
	 * Add effects that belong to the kit, to the player.
   *
	 * @param p The player to put effects on.
	 */
	private void addEffects(Player p){
		if(this == Kit.CIVILIAN || this == Kit.FARMER){
			return;
		}else if(this == Kit.BUILDER){
			p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0, true), true);
		}else if(this == Kit.MINER){
			p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true), true);
		}else if(this == Kit.WARRIOR){
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, true), true);
		}else if(this == Kit.BLACKSMITH){
			p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true), true);
		}else if(this == Kit.ARCHER){
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true), true);
		}else if(this == Kit.MAN_IN_BLACK || this == Kit.SPARKY){
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 1, true), true);
			
			if(this == Kit.SPARKY) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true), true);
			}
		}
	}

	/**
	 * Get the name of the kit where the first letter is upercase and the rest are
	 * lowercase. Use Kit.name for storing information in permissions or database
	 * however, to ensure reliability.
	 *
	 * @return The name of the kit, where the first letter is capitalized.
	 */
	public String getName(){
		return name().substring(0, 1) + name().substring(1).toLowerCase();
	}

	/**
	 * Check whether or not a player owns a class.
	 *
	 * @param p The player to check if they own this class.
	 * @return True if player owns class, otherwise false.
	 */
	public boolean doesPlayerOwnClass(Player p){
		return p.isOp() || p.hasPermission("fantasy.kit." + getName().toLowerCase()) || this == Kit.CIVILIAN || this == Kit.MINER || this == Kit.ARCHER;
	}

	/**
	 * Show the class selector to a player, but only include the classes that are
	 * included with Race $r.
	 *
	 * @param p The Player to show the class selector to.
	 * @param r The Race that owns these kits (archer views archer kits, etc.)
	 */
	public static void showKitSelector(Player p, Race r){
		Inventory inv = Bukkit.createInventory(p, ((r.kits.size() + 8) / 9) * 9, "Select Your Class");

		for(Kit k : r.getKits()){
			if(k.icon.getType() == Material.BEDROCK){
				continue;
			}

			ItemStack item = k.icon.clone();
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();

			lore.add(ChatColor.AQUA + "----------");

			if(k.doesPlayerOwnClass(p)){
				lore.add(ChatColor.GREEN + "Unlocked");
			}else{
				lore.add(ChatColor.RED + "Locked. Use /buy to purchase");
			}

			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.addItem(item);
		}

		p.openInventory(inv);
	}
}
