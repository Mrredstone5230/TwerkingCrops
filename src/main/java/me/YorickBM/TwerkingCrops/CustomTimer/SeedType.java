package me.YorickBM.TwerkingCrops.CustomTimer;

import me.YorickBM.TwerkingCrops.Materials;
import me.YorickBM.TwerkingCrops.ToolBox;
import org.bukkit.Location;

public class SeedType {
	
	private Materials.EMaterial _type;
	private Location _location;
	
	public SeedType(Materials.EMaterial type, Location location) {
		_type = type;
		_location = location;
	}
	
	public SeedType(String type, String location) {
		this(Materials.EMaterial.valueOf(type), ToolBox.stringToLocation(location.split(",")));
	}
	
	public Materials.EMaterial getType() {
		return _type;
	}
	
	public Location getLocation() {
		return _location;
	}
	
	public String toString() {
		String type = _type.toString();
		String location = ToolBox.locationToString(_location);
		return type + ";" + location;
	}
	
	public boolean check(Materials.EMaterial mat, Location loc) {
		if(_type == mat && _location.getBlockX() == loc.getBlockX() && _location.getBlockY() == loc.getBlockY() && _location.getBlockZ() == loc.getBlockZ()) {
			return true;
		} else {
			return false;
		}
	}

}
