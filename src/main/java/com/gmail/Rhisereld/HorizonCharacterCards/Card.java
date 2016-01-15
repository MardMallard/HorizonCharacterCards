package com.gmail.Rhisereld.HorizonCharacterCards;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.Rhisereld.HorizonProfessions.ProfessionAPI;

public class Card 
{
	FileConfiguration config;
	FileConfiguration data;
	UUID ownerUUID;
	String name;
	int age;
	String gender;
	String race;
	String description;
	String path;
	String currentCard;

	/**
	 * Constructor for fetching card.
	 * 
	 * @param ownerUUID
	 */
	public Card(FileConfiguration config, FileConfiguration data, Player player)
	{
		this.config = config;
		this.data = data;
		
		ownerUUID = player.getUniqueId();
		name = data.getString("cards." + ownerUUID + ".currentCard", player.getName());
		data.set("cards." + ownerUUID + ".currentCard", name);
		path = "cards." + ownerUUID + "." + name + ".";
		age = data.getInt(path + "age", config.getInt("default age"));
		gender = data.getString(path + "gender", config.getString("default gender"));
		race = data.getString(path + "race", config.getString("default race"));
		description = data.getString(path + "description", config.getString("default description"));
		data.set(path + ".created", true);
	}
	
	/**
	 * setName() sets the name of the character card.
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 */
	void setName(String name) throws IllegalArgumentException
	{
		if (name.length() > config.getInt("max name length", 30))
			throw new IllegalArgumentException("Name is too long.");
		
		data.set("cards." + ownerUUID + "." + name + ".age", age);
		data.set("cards." + ownerUUID + "." + name + ".gender", gender);
		data.set("cards." + ownerUUID + "." + name + ".race", race);
		data.set("cards." + ownerUUID + "." + name + ".description", description);
		data.getConfigurationSection("cards." + ownerUUID + ".").set(this.name, null);
		path = "cards." + ownerUUID + "." + name + ".";
		currentCard = name;
		data.set("cards." + ownerUUID + ".currentCard", currentCard);
		data.set(path + ".created", true);
		
		this.name = name;
		
		//Set nickname in chat.
		Bukkit.getPlayer(ownerUUID).setDisplayName(name);
	}
	
	/**
	 * createCard() creates a new card for the player by a given name.
	 * The card has all default values when created.
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 */
	void createCard(String name) throws IllegalArgumentException
	{
		if (name.length() > config.getInt("max name length", 30))
			throw new IllegalArgumentException("Name is too long.");
		
		Set<String> cards;
		try 
		{ 
			cards = data.getConfigurationSection("cards." + ownerUUID).getKeys(false);
			cards.remove("currentCard");
		} 
		catch (NullPointerException e)
		{ cards = new HashSet<String>(); }
		
		
		if (cards.contains(name))
			throw new IllegalArgumentException("You already have a card by that name.");
		
		if (cards.size() >= config.getInt("max cards allowed"))
			throw new IllegalArgumentException("You already have the maximum number of permitted cards.");
		
		setCurrentCard(name);
		data.set(path + ".created", true);
	}
	
	/**
	 * deleteCard() removes the card from the player.
	 * If they have other cards, their current card is set to the next card found.
	 * Otherwise, a new card is created for their player name.
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 */
	void deleteCard(String name) throws IllegalArgumentException
	{
		Set<String> cards;
		try 
		{
			cards = data.getConfigurationSection("cards." + ownerUUID).getKeys(false);
			cards.remove("currentCard");
		}
		catch (NullPointerException e) { cards = new HashSet<String>(); }
		
		if (!cards.contains(name))
			throw new IllegalArgumentException("You do not have a card by that name.");
		
		data.getConfigurationSection("cards." + ownerUUID + ".").set(name, null);
		
		if (data.getString("cards." + ownerUUID + ".currentCard").equalsIgnoreCase(name))
			if (cards.isEmpty())
				data.set("cards." + ownerUUID + ".currentCard", Bukkit.getPlayer(ownerUUID).getName());
			else
				data.set("cards." + ownerUUID + ".currentCard", cards.iterator().next());
	}
	
	/**
	 * getName() returns the name of the character card.
	 * 
	 * @return
	 */
	String getName()
	{
		return name;
	}
	
	/**
	 * setAge() sets the age of the character card.
	 * If the age is not between the minimum and maximum ages specified in the configuration, 
	 * it will throw an IllegalArgumentException.
	 * 
	 * @param age
	 * @throws IllegalArgumentException
	 */
	void setAge(int age) throws IllegalArgumentException
	{
		int minAge = config.getInt("min age", 5);
		int maxAge = config.getInt("max age", 150);
		
		if (age < minAge)
			throw new IllegalArgumentException("The minimum age is " + minAge + ".");
		if (age > maxAge)
			throw new IllegalArgumentException("The maximum age is " + maxAge + ".");
		
		this.age = age;
		data.set(path + "age", age);
	}
	
	/**
	 * getAge() returns the age of the character card.
	 * 
	 * @return
	 */
	int getAge()
	{
		return age;
	}
	
	/**
	 * setGender() returns the gender of the character card.
	 * 
	 * @param gender
	 * @throws IllegalArgumentException
	 */
	void setGender(String gender) throws IllegalArgumentException
	{
		List<String> validGenders = config.getStringList("valid genders");
		
		for (String g: validGenders)
			if (gender.equalsIgnoreCase(g))
			{
				this.gender = g;
				data.set(path + "gender", g);
				return;
			}
		
		String message = "That is not a valid gender. Valid genders are:";
		
		for (String g: validGenders)
			message += " " + g;
		throw new IllegalArgumentException(message);
	}
	
	/**
	 * getGender() returns the gender of the character card.
	 * 
	 * @return
	 */
	String getGender()
	{
		return gender;
	}
	
	/**
	 * setRace sets the race of the character card.
	 * If the race is not one of the valid races specified in configuration,
	 * it will throw an IllegalArgumentException
	 * 
	 * @param race
	 * @throws IllegalArgumentException
	 */
	void setRace(String race) throws IllegalArgumentException
	{
		List<String> validRaces = config.getStringList("valid races");
		
		for (String r: validRaces)
			if (race.equalsIgnoreCase(r))
			{
				this.race = r;
				data.set(path + "race", r);
				return;
			}
		
		String message = "That is not a valid race. Valid races are:";
		
		for (String r: validRaces)
			message += " " + r;
		throw new IllegalArgumentException(message);
	}
	
	/**
	 * getRace() returns the race of the character card.
	 * 
	 * @return
	 */
	String getRace()
	{
		return race;
	}
	
	/**
	 * setDescription() sets the description of the character card.
	 * If the description is longer than the max description length specified in configuration,
	 * it will throw an IllegalArgumentException
	 * 
	 * @param description
	 * @throws IllegalArgumentException
	 */
	void setDescription(String description) throws IllegalArgumentException
	{
		if (description.length() > config.getInt("max description length", 100))
			throw new IllegalArgumentException("That description is too long.");
		
		this.description = description;
		data.set(path + "description", description);
	}
	
	/**
	 * getDescription() returns the description of the character card.
	 * 
	 * @return
	 */
	String getDescription()
	{
		return description;
	}
	
	/**
	 * addDescription() adds more to the end of the existing character card description.
	 * If the resulting description is longer than the maximum description length specified in configuration,
	 * it throws an IllegalArgumentException.
	 * 
	 * @param description
	 */
	void addDescription(String description)
	{
		if (description.length() + this.description.length() > config.getInt("max description length", 100))
			throw new IllegalArgumentException("Total description is too long");
		
		this.description += description;
		data.set(path + "description", this.description);
	}
	
	/**
	 * setCurrentCard() changes the card that the player is currently using.
	 * 
	 * @param name
	 */
	private void setCurrentCard(String name)
	{
		currentCard = name;
		data.set("cards." + ownerUUID + ".currentCard", currentCard);
	}
	
	/**
	 * switchCard() changes the card that the player is currently using.
	 * An IllegalArgumentException will be thrown if the card does not currently exist.
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 */
	void switchCard(String name) throws IllegalArgumentException
	{
		Set<String> cards;
		
		try 
		{ 
			cards = data.getConfigurationSection("cards." + ownerUUID).getKeys(false);
			cards.remove("currentCard");
		} 
		catch (NullPointerException e)
		{ cards = new HashSet<String>(); }
		
		if (cards.isEmpty() || !cards.contains(name))
			throw new IllegalArgumentException("You don't have a card by that name.");
		
		setCurrentCard(name);
	}
	
	void view(ProfessionAPI prof, Player player)
	{
		String pronoun = getPronoun();
		String lookConjugated = conjugate("look");
		String hasConjugated = conjugate("has");
		Player cardOwner = Bukkit.getPlayer(ownerUUID);
		String health = getHealthDescription(player);
		ItemStack[] contents = cardOwner.getInventory().getContents();
		int freeSlots = 0;
		for (ItemStack i: contents)
			if (i == null)
				freeSlots++;
		int threatLevel = getThreatLevel(cardOwner);

		player.sendMessage(new String[]{ChatColor.GREEN + "*---------*",
			ChatColor.GREEN + "* " + ChatColor.WHITE + "Oh, this is " + name + ", " + getDescribe() + ".",
			ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + lookConjugated + " like a " + race + "."});
		if (health != null)
			player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + lookConjugated + " " + getHealthDescription(player) + ".");
		if (cardOwner.getFoodLevel() <= 6)
			player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + lookConjugated + " malnourished.");
		if (freeSlots < 18)
			player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + lookConjugated + " burdened with a lot of "
					+ "items.");
		if (threatLevel >= 10)
			player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + lookConjugated + " armed to the teeth.");
		else if (threatLevel >= 1)
			player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + hasConjugated + " a weapon strapped to "
					+ getPossessive() + " hip.");
		if (prof != null)
			if (prof.isValidProfession("engineer") && prof.hasTier(ownerUUID, "engineer", prof.getTiers().size() - 1))
				player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + hasConjugated + 
						" a belt full of gadgets for working with electronics.");
			else if (prof.isValidProfession("labourer") && prof.hasTier(ownerUUID, "labourer", prof.getTiers().size() - 1))
				player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + hasConjugated + 
						" thick dirt-caked leather gloves designed for hard work.");
			else if (prof.isValidProfession("medic") && prof.hasTier(ownerUUID, "medic", prof.getTiers().size() - 1))
				player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + hasConjugated + 
						" a belt full of sterile-looking medical equipment.");
			else if (prof.isValidProfession("pilot") && prof.hasTier(ownerUUID, "pilot", prof.getTiers().size() - 1))
				player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + hasConjugated + 
						" a hands-off navigational HUD covering " + getPossessive() + " eyes.");
			else if (prof.isValidProfession("hunter") && prof.hasTier(ownerUUID, "hunter", prof.getTiers().size() - 1))
				player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + lookConjugated + 
						" like " + pronoun + " can take a punch.");
		player.sendMessage(ChatColor.GREEN + "* " + ChatColor.WHITE + description); 
		player.sendMessage(ChatColor.GREEN + "*---------*");
	}
	
	/**
	 * getDescribe() returns a string describing the player based on their age and gender.
	 * Age pieces: young, [nothing], elderly
	 * Gender pieces: girl, boy, woman, man, person
	 * 
	 * @param card
	 * @return
	 */
	private String getDescribe()
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		if (age < 16)
			if (gender.equalsIgnoreCase("female"))
				return "a young girl";
			else if (gender.equalsIgnoreCase("male"))
				return "a young boy";
			else
				return "a young person";
		
		if (age < 36)
			stringBuilder.append("a young ");
		else if (age < 56)
			stringBuilder.append("a ");
		else
			stringBuilder.append("an elderly ");
		
		if (gender.equalsIgnoreCase("female"))
			stringBuilder.append("woman");
		else if (gender.equalsIgnoreCase("male"))
			stringBuilder.append("man");
		else
			stringBuilder.append("person");
		
		return stringBuilder.toString();
	}
	
	/**
	 * getPronown() returns a pronoun based on the player's gender.
	 * Female: she
	 * Male: he
	 * Otherwise: they
	 * 
	 * @param gender
	 * @return
	 */
	private String getPronoun()
	{
		if (gender.equalsIgnoreCase("female"))
			return "She";
		if (gender.equalsIgnoreCase("male"))
			return "He";
		return "They";
	}
	
	/**
	 * 
	 * 
	 * @param player
	 * @return
	 */
	private String getHealthDescription(Player player)
	{
		if (player.getHealth() <= 6)
			return "seriously injured";
		if (player.getHealth() < 20)
			return "slightly injured";
		return null;
	}
	
	private String conjugate(String string)
	{
		if (string.equalsIgnoreCase("has"))
			if (gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("male"))
				return string;
			else
				return "have";
		
		if (gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("male"))
			string += "s";
		
		return string;
	}
	
	private int getThreatLevel(Player player)
	{
		int threat = 0;
		
		for (String w: config.getConfigurationSection("weapon threat levels").getKeys(false))
			if (Material.getMaterial(w) != null)
				for (int i = 0; i < 9; i++)
				    if (player.getInventory().getItem(i) != null 
				    		&& player.getInventory().getItem(i).getType().equals(Material.getMaterial(w)))
				    	threat += config.getInt("weapon threat levels." + w);
		
		return threat;
	}
	
	private String getPossessive()
	{
		if (gender.equalsIgnoreCase("female"))
			return "her";
		if (gender.equalsIgnoreCase("male"))
			return "his";
		else
			return "their";
	}
}
