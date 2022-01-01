package me.YorickBM.TwerkingCrops.CustomTimer;

import me.YorickBM.TwerkingCrops.Core;
import me.YorickBM.TwerkingCrops.Materials;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/*
 * Created by Yorick, Last modified on: 14-1-2019
 */
public class EventHandlerCustomTimer implements Listener {
	@EventHandler
	  public void Placeblock(BlockPlaceEvent e)
	  {
		if(Materials.isTypeAllowed(Materials.GetType(e.getBlock()))
				&& !Core.getInstance().GetWorldBlacklist().IsBlacklisted(e.getBlock().getLocation().getWorld().getName())
				&& !Core.getInstance().GetCropBlacklist().IsBlacklisted(Materials.GetType(e.getBlock()).toString()))
			Core.getInstance().seedsForTimer.add(new SeedType(Materials.GetType(e.getBlock()), e.getBlock().getLocation()));
	  }
	  
	  @EventHandler
	  public void removeBlock(BlockBreakEvent e)
	  {
	    Materials.EMaterial blk = Materials.GetType(e.getBlock());
	    Location loc = e.getBlock().getLocation();
	    
	    Core.getInstance().seedsForTimer.removeIf(s -> s.check(blk, loc));
	  }
}
