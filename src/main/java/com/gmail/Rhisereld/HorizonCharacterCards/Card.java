package com.gmail.Rhisereld.HorizonCharacterCards;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
		age = data.getInt(path + "age");
		gender = data.getString(path + "gender", config.getString("default gender"));
		race = data.getString(path + "race", config.getString("default race"));
		description = data.getString(path + "description", config.getString("default description"));
	}
	
	/**
	 * setName() sets the name of the character card.
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 */
	public void setName(String name) throws IllegalArgumentException
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
		
		this.name = name;
		
		//Set nickname in chat.
		Bukkit.getPlayer(ownerUUID).setDisplayName(name);
	}
	
	/**
	 * getName() returns the name of the character card.
	 * 
	 * @return
	 */
	public String getName()
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
	public void setAge(int age) throws IllegalArgumentException
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
	public int getAge()
	{
		return age;
	}
	
	/**
	 * setGender() returns the gender of the character card.
	 * 
	 * @param gender
	 * @throws IllegalArgumentException
	 */
	public void setGender(String gender) throws IllegalArgumentException
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
	public String getGender()
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
	public void setRace(String race) throws IllegalArgumentException
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
	public String getRace()
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
	public void setDescription(String description) throws IllegalArgumentException
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
	public String getDescription()
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
	public void addDescription(String description)
	{
		if (description.length() + this.description.length() > config.getInt("max description length", 100))
			throw new IllegalArgumentException("Total description is too long");
		
		this.description += description;
		data.set(path + "description", this.description);
	}
	
	public void view(Player player)
	{
		String pronoun = getPronoun();
		String lookConjugated = conjugate("look");
		
		player.sendMessage(new String[]{ChatColor.GREEN + "*---------*",
			ChatColor.GREEN + "* " + ChatColor.WHITE + "Oh, this is " + name + ", " + getDescribe() + ".",
			ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + lookConjugated + " like a " + race + ".",
			ChatColor.GREEN + "* " + ChatColor.WHITE + pronoun + " " + lookConjugated + " " + getHealthDescription(player) + ".",
			ChatColor.GREEN + "* " + ChatColor.WHITE + description,
			ChatColor.GREEN + "*---------*"});
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
		if (gender.equalsIgnoreCase("Female"))
			return "She";
		if (gender.equalsIgnoreCase("Male"))
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
		return "completely healthy";
	}
	
	private String conjugate(String string)
	{
		if (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"))
			string += "s";
		
		return string;
	}
}
