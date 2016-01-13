package com.gmail.Rhisereld.HorizonCharacterCards;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class CardListener implements Listener
{
	FileConfiguration config;
	FileConfiguration data;
	
	public CardListener(FileConfiguration config, FileConfiguration data)
	{
		this.config = config;
		this.data = data;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onRightClick(PlayerInteractEntityEvent event)
	{
		//Looking for players only.
		if (!(event.getRightClicked() instanceof Player))
			return;
		
		Player player = (Player) event.getRightClicked();
		Player clicker = event.getPlayer();
		new Card(config, data, player).view(clicker);
	}
}
