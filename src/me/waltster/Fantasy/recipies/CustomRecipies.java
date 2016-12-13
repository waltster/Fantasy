package me.waltster.Fantasy.recipies;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;

public class CustomRecipies {
	private static ArrayList<ShapedRecipe> recipies;
	
	public static void initializeRecipies(){
		recipies = new ArrayList<ShapedRecipe>();
		ItemStack current = new ItemStack(Material.GRASS);
		ItemMeta meta = current.getItemMeta();
		
		// Begin cobblestone recipe: 9 small stones (clay balls) = 1 cobblestone
		ShapedRecipe cobblestoneFromSmallStones = new ShapedRecipe(new ItemStack(Material.COBBLESTONE, 1));
		cobblestoneFromSmallStones.shape("xxx", "xxx", "xxx");
		cobblestoneFromSmallStones.setIngredient('x', Material.CLAY_BALL);
		recipies.add(cobblestoneFromSmallStones);
		// End cobblestone recipe
		
		// Begin bandage recipe: 3 leather and 3 paper = 1 bandage
		current = new ItemStack(Material.PAPER);
		meta = current.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Bandage");
		current.setItemMeta(meta);
		ShapedRecipe bandageRecipe = new ShapedRecipe(current);
		bandageRecipe.shape("", "xxx", "ooo");
		bandageRecipe.setIngredient('x', Material.PAPER);
		bandageRecipe.setIngredient('o', Material.LEATHER);
		recipies.add(bandageRecipe);
		// End bandage recipe
		
		// Begin antibiotics recipe: 1 bucket of milk over a potion of healing = 1 antibiotics
		current = new ItemStack(Material.MILK_BUCKET);
		meta = current.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Antibiotics");
		current.setItemMeta(meta);
		ShapedRecipe antibioticsRecipe = new ShapedRecipe(current);
		antibioticsRecipe.shape("", " x ", " o ");
		antibioticsRecipe.setIngredient('x', Material.MILK_BUCKET);
		Potion healingPotion = new Potion(PotionType.REGEN);
		antibioticsRecipe.setIngredient('o', healingPotion.toItemStack(1).getType());
		recipies.add(antibioticsRecipe);
		// End antibiotics recipe
		
		for(ShapedRecipe r : recipies){
			Bukkit.addRecipe(r);
		}
	}
}
