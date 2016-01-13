package com.gmail.Rhisereld.HorizonCharacterCards;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class HorizonCommandParser implements CommandExecutor
{
	FileConfiguration config;
	FileConfiguration data;
	
	public HorizonCommandParser(FileConfiguration config, FileConfiguration data)
	{
		this.config = config;
		this.data = data;
	}
	
    /**
     * onCommand() is called when a player enters a command recognised by Bukkit to belong to this plugin.
     * After that it is up to the contents of this method to determine what the commands do.
     * Returning false displays a command usage guide to the player.
     * 
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) 
	{
		//Main command
		if (commandLabel.equalsIgnoreCase("card"))
		{	
			//card
			if (args.length < 1)
				giveCommandGuide(sender);
				
			//card view
			else if (args[0].equalsIgnoreCase("view"))
				viewCard(sender);
			
			//card set
			else if (args[0].equalsIgnoreCase("set"))
			{
				if (args.length < 3)
					sender.sendMessage(ChatColor.RED + "Insufficient arguments");
				
				//card set name [name]
				else if (args[1].equalsIgnoreCase("name"))
					setName(sender, args[1]);
				
				//card set age [age]
				else if (args[1].equalsIgnoreCase("age"))
					setAge(sender, args[1]);
				
				//card set gender [gender]
				else if (args[1].equalsIgnoreCase("gender") || args[1].equalsIgnoreCase("sex"))
					setGender(sender, args[1]);
				
				//card set race [race]
				else if (args[1].equalsIgnoreCase("race"))
					setRace(sender, args[1]);
				
				//card set description [description]
				else if (args[1].equalsIgnoreCase("description"))
					setDescription(sender, args[1]);
				else
					givePossibleAttributes(sender);
			}
			
			return true;
		}
		
		return false;
	}

	/**
	 * giveCommandGuide() displays a list of commands that the player has access to.
	 * 
	 * @param sender
	 */
	private void giveCommandGuide(CommandSender sender) 
	{
		if (!sender.hasPermission("horizoncards.help"))
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to view the command guide.");
			return;
		}
		
		sender.sendMessage("----------<" + ChatColor.DARK_GREEN + " Horizon Character Card Commands " + ChatColor.WHITE + ">----------");
		sender.sendMessage(ChatColor.DARK_GREEN + "Horizon Character Cards allows you to keep track of your character's attributes.");
		
		if (sender.hasPermission("horizoncards.view"))
		{
			sender.sendMessage(ChatColor.GREEN + "/card view");
			sender.sendMessage("View your character card.");
		}
		
		if (sender.hasPermission("horizoncards.set.name") || sender.hasPermission("horizoncards.set.age") 
				|| sender.hasPermission("horizoncards.set.gender") || sender.hasPermission("horizoncards.set.race")
				|| sender.hasPermission("horizoncards.set.description"))
		{
			sender.sendMessage(ChatColor.GREEN + "/card set");
			sender.sendMessage("Set an attribute in your character card.");
		}
	}
	
	/**
	 * viewCard() displays the sender's character card.
	 * 
	 * @param sender
	 */
	private void viewCard(CommandSender sender) 
	{
		if (!sender.hasPermission("horizoncards.view"))
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to view your character card.");
			return;
		}
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "That command can only be used by players.");
			return;
		}
		
		Player player = (Player) sender;
		Card card = new Card(config, data, player);
		
		//TODO
	}
	
	/**
	 * setName() sets the sender's name in their character card.
	 * 
	 * @param sender
	 * @param name
	 */
	private void setName(CommandSender sender, String name)
	{
		if (!sender.hasPermission("horizoncards.set.name") || !sender.hasPermission("horizoncards.set"))
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to set your name.");
			return;
		}
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "That command can only be used by players.");
			return;
		}
		
		Player player = (Player) sender;
		try { new Card(config, data, player).setName(name); }
		catch (IllegalArgumentException e)
		{ sender.sendMessage(ChatColor.RED + e.getMessage());}
	}
	
	/**
	 * setAge() sets the sender's age in their character card.
	 * 
	 * @param sender
	 * @param age
	 */
	private void setAge(CommandSender sender, String age)
	{
		if (!sender.hasPermission("horizoncards.set.age") || !sender.hasPermission("horizoncards.set"))
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to set your age.");
			return;
		}
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "That command can only be used by players.");
			return;
		}
		
		Player player = (Player) sender;
		try { new Card(config, data, player).setAge(Integer.parseInt(age)); }
		catch (NumberFormatException e)
		{ sender.sendMessage(ChatColor.RED + "That is not a valid number."); }
		catch (IllegalArgumentException e)
		{ sender.sendMessage(ChatColor.RED + e.getMessage()); }
	}
	
	/**
	 * setGender() sets the sender's gender in their character card.
	 * 
	 * @param sender
	 * @param gender
	 */
	private void setGender(CommandSender sender, String gender)
	{
		if (!sender.hasPermission("horizoncards.set.gender") || !sender.hasPermission("horizoncards.set"))
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to set your gender.");
			return;
		}
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "That command can only be used by players.");
			return;
		}
		
		Player player = (Player) sender;
		try { new Card(config, data, player).setGender(gender); }
		catch (IllegalArgumentException e)
		{ sender.sendMessage(ChatColor.RED + e.getMessage());}
	}
	
	/**
	 * setRace() sets the sender's race in their character card.
	 * 
	 * @param sender
	 * @param race
	 */
	private void setRace(CommandSender sender, String race)
	{
		if (!sender.hasPermission("horizoncards.set.race") || !sender.hasPermission("horizoncards.set"))
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to set your race.");
			return;
		}
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "That command can only be used by players.");
			return;
		}
		
		Player player = (Player) sender;
		try { new Card(config, data, player).setRace(race); }
		catch (IllegalArgumentException e)
		{ sender.sendMessage(ChatColor.RED + e.getMessage());}
	}
	
	/**
	 * setDescription() sets the sender's description in their character card.
	 * 
	 * @param sender
	 * @param description
	 */
	private void setDescription(CommandSender sender, String description)
	{
		if (!sender.hasPermission("horizoncards.set.decription") || !sender.hasPermission("horizoncards.set"))
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to set your description.");
			return;
		}
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "That command can only be used by players.");
			return;
		}
		
		Player player = (Player) sender;
		try { new Card(config, data, player).setDescription(description); }
		catch (IllegalArgumentException e)
		{ sender.sendMessage(ChatColor.RED + e.getMessage());}
	}
	
	/**
	 * givePossibleAttributes() displays all the character card attributes that the sender is allowed to change.
	 * 
	 * @param sender
	 */
	private void givePossibleAttributes(CommandSender sender)
	{	
		if (!(sender.hasPermission("horizoncards.set.name") || sender.hasPermission("horizoncards.set.age") 
				|| sender.hasPermission("horizoncards.set.gender") || sender.hasPermission("horizoncards.set.race")
				|| sender.hasPermission("horizoncards.set.description")) || sender.hasPermission("horizoncards.set"))
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to set any attributes in your character card.");
			return;
		}
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "That command can only be used by players.");
			return;
		}
		
		sender.sendMessage(ChatColor.GREEN + "Set card attributes using /card set [attribute] [value]");
		
		String message = "Possible attributes:";
		
		if (sender.hasPermission("horizoncards.set.name"))
			message += " name";
		
		if (sender.hasPermission("horizoncards.set.age"))
			message += " age";
		
		if (sender.hasPermission("horizoncards.set.gender"))
			message += " gender";
		
		if (sender.hasPermission("horizoncards.set.race"))
			message += " race";
		
		if (sender.hasPermission("horizoncards.set.description"))
			message += " description";
		
		sender.sendMessage(ChatColor.GREEN + message);
	}
}
