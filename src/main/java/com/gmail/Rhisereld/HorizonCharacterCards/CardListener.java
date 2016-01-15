package com.gmail.Rhisereld.HorizonCharacterCards;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gmail.Rhisereld.HorizonProfessions.ProfessionAPI;

public class CardListener implements Listener
{
	FileConfiguration config;
	FileConfiguration data;
	ProfessionAPI prof;
	
	public CardListener(FileConfiguration config, FileConfiguration data, ProfessionAPI prof)
	{
		this.config = config;
		this.data = data;
		this.prof = prof;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onRightClick(PlayerInteractEntityEvent event)
	{
		//Looking for players only.
		if (!(event.getRightClicked() instanceof Player))
			return;
		
		//Shift-click only.
		Player clicker = event.getPlayer();
		if (!clicker.isSneaking())
			return;
		
		Player player = (Player) event.getRightClicked();
		new Card(config, data, player).view(prof, clicker);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		//Set the player's nickname according to their character card.
		Player player = event.getPlayer();
		player.setDisplayName(new Card(config, data, player).getName());
	}
}
