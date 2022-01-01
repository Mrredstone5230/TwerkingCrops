package me.YorickBM.TwerkingCrops;

import org.bukkit.Location;

public class BlockData {
	
	private Location block;
	private Location stem;
	private Materials.EMaterial type;
	
	public BlockData(Location blk, Location stm, Materials.EMaterial mat) {
		setBlock(blk);
		setStem(stm);
		setType(mat);
	}
	public BlockData(String blk, String stm, String mat) {
		setBlock(blk);
		setStem(stm);
		setType(mat);
	}
	public BlockData(String blk, String stm, Materials.EMaterial mat) {
		setBlock(blk);
		setStem(stm);
		setType(mat);
	}

	public Location getBlock() {
		return block;
	}
	public String getBlockS() {
		return ToolBox.locationToString(block);
	}

	public void setBlock(Location block) {
		this.block = block;
	}
	public void setBlock(String block) {
		this.block = ToolBox.stringToLocation(block.split(","));
	}

	public Location getStem() {
		return stem;
	}
	public String getStemS() {
		return ToolBox.locationToString(stem);
	}

	public void setStem(Location stem) {
		this.stem = stem;
	}
	public void setStem(String stem) {
		this.stem = ToolBox.stringToLocation(stem.split(","));
	}

	public Materials.EMaterial getType() {
		return type;
	}
	public String getTypeS() {
		return type.toString();
	}

	public void setType(Materials.EMaterial type) {
		this.type = type;
	}
	public void setType(String type) {
		this.type = Materials.EMaterial.valueOf(type);
	}
}
