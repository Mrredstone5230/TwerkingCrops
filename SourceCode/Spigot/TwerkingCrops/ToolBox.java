package Spigot.TwerkingCrops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Spigot.TwerkingCrops.Materials.EMaterial;
import Spigot.TwerkingCrops.Commands.SetFunctie;
import Spigot.TwerkingCrops.CustomTimer.SeedType;
import net.md_5.bungee.api.ChatColor;

/*
 * Created by Yorick, Created on: 14-1-2019
 */
public class ToolBox {

	@SuppressWarnings("unchecked")
	public static void createJSONFromList(List<BlockData> list, String FileName) {
		try {
			createFile(Core.getInstance().getDataFolder() + "/Data/" + FileName);
		} catch (IOException e1) {
			Core.getInstance().getLogger().log(Level.SEVERE, "Failed to create Melon & Pumpkin .dat file...", e1);
			e1.printStackTrace();
		}
		
		JSONObject obj = new JSONObject();  
		obj.put("Count", list.size());
		for(int i=0; i< list.size(); i++)
		{
			JSONObject total = new JSONObject(); 
			total.put("Type", list.get(i).getTypeS());
			total.put("Stem", list.get(i).getStemS());
			total.put("Block", list.get(i).getBlockS());
			
			obj.put(i, total);
		}
		
		try (FileWriter file = new FileWriter(Core.getInstance().getDataFolder() + "/Data/" + FileName)) {
			obj.writeJSONString(file);
		} catch (IOException e) {
			Core.getInstance().getLogger().log(Level.SEVERE, "Failed to write Melon & Pumpkin data to .dat file...", e);
		}
	}
	 
	 public static void createFile(String fullPath) throws IOException {
		    File file = new File(fullPath);
		    if(file.exists()) {
		    	return;
		    }
		    file.getParentFile().mkdirs();
		    file.createNewFile();
		}

	 
	 private static int index = 0;
	 public static int getNum() {
		 return index++;
	 }
	 
	 public static void SaveCropsToConfig() {
		  index = 0;
		  Core.getInstance().seedsForTimer.stream().forEach(x -> Core.getInstance().cfgm.getSeeds().set("DATA." + getNum() + "", x.toString()));
		  Core.getInstance().cfgm.getSeeds().set("Ints.SEEDS", index);		  
		  
		  Core.getInstance().cfgm.saveSeeds();
	  }
	  public static void InitLocations(int seeds) {

		  for (int i = 0; i < seeds; i++) {
	          String[] data = Core.getInstance().cfgm.getSeeds().getString("DATA." + i + "").split(";");

	          if(EMaterial.valueOf(data[0]) != null) {
	        	  Core.getInstance().seedsForTimer.add(new SeedType(data[0], data[1]));
	          }else {
	        	  System.out.println("Error on loading: " + data[0]);
	          }
		  }
	  }
	  
	 
	  public static void UpdateConfig(String Func, String... params)
	  {
	    if (Func.equalsIgnoreCase("Set"))
	    {
	      Core.getInstance().getConfig().set("Custom." + params[1], params[0].toUpperCase());
	      Core.getInstance().saveConfig();
	    }
	  }
	  public static String cc(String msg)
	  {
	    String Functions = "";
	    
	    List<String> functions2 = Core.getInstance().Functions;
	    String arr;
	    for (int i = 0; i < functions2.size(); i++)
	    {
	      arr = functions2.get(i);
	      Functions = Functions + arr + "/";
	    }
	    return ChatColor.translateAlternateColorCodes('&', msg
	      .replace("%Func%", SetFunctie.Func)
	      .replace("%Bool%", SetFunctie.Bool)
	      
	      .replace("%Functions%", method(Functions))
	      .replace("%Reason%", SetFunctie.Reason)
	      );
	  }
	  public static String method(String str)
	  {
	    if ((str != null) && (str.length() > 0) && (str.charAt(str.length() - 1) == '/')) {
	      str = str.substring(0, str.length() - 1);
	    }
	    return str;
	  }
	  public static void addFunction(String Name, String ConfigLocation, String State)
	  {
		Core.getInstance().Functions.add(Name);
		if(Core.getInstance().getConfig().getString(ConfigLocation) != null) {
			if (Core.getInstance().getConfig().getString(ConfigLocation).equals(State)) {
	    	Core.getInstance().getLogger().log(Level.INFO, Name + " succesfully disabled");
			} else {
				Core.getInstance().getLogger().log(Level.INFO, Name + " succesfully enabled");
			}
		} else {
			Core.getInstance().getLogger().log(Level.WARNING, Name + " failed to load!");
		}
	  }
	  
	  public static void SaveStemsToConfig() {  
		  List<BlockData> a = new ArrayList<BlockData>();
		  
		  if(Core.getInstance().StemToBlock.size() >= 1) {
			  for (Entry<Location, HashMap<Location, EMaterial>> entry : Core.getInstance().StemToBlock.entrySet()) {			  
				  Location locStem = entry.getKey();
				  HashMap<Location, EMaterial> BlockMap = entry.getValue();
				  
				  Location locBlock = null;
				  EMaterial mat = null;
				  for (Entry<Location, EMaterial> BlockEntry : BlockMap.entrySet()) {
					  locBlock = BlockEntry.getKey();
					  mat = BlockEntry.getValue();
				  }
				  try {
					  BlockData data = new BlockData(locBlock, locStem, mat);
					  a.add(data);
				  } catch (NullPointerException e) {
					  Core.getInstance().getLogger().log(Level.WARNING, "Did not succesfully safe all Melon & Pumpkin Data... (Report tis error if it missed data, other wise disregard)", e);
				  }
				}
		  }
		  
		  createJSONFromList(a, "Data.dat");
	  }
	  public static void LoadStemsFromConfig() {	
		  JSONParser parser = new JSONParser();
		  try {
			  Object obj = null;
			  try {
				  FileReader fileReader = new FileReader(Core.getInstance().getDataFolder() + "/Data/Data.dat");
				  obj = parser.parse(fileReader);
			  } catch  (FileNotFoundException e ) {}
			  JSONObject JsonObj = (JSONObject) obj;
			  long count;
			  
			  try {
				  count = (long) JsonObj.get("Count");  
			  } catch (NullPointerException e) {
				  count = 0;
			  }
			 
			  
			  for(int i = 0; i < count; i++) {
				  JSONObject dataObj = (JSONObject) JsonObj.get(i + "");
				  String Type = (String) dataObj.get("Type");
				  String Stem = (String) dataObj.get("Stem");
				  String Block = (String) dataObj.get("Block");
				  
				  BlockData blockData = new BlockData(Block, Stem, Type);
				  
				  HashMap<Location, EMaterial> x = new HashMap<Location, EMaterial>();
				  x.put(blockData.getBlock(), blockData.getType());
				  
				  Core.getInstance().BlockToStem.put(blockData.getBlock(), blockData.getStem());
				  Core.getInstance().StemToBlock.put(blockData.getStem(), x);
			  }
		  } catch (IOException | ParseException e) {
			  Core.getInstance().getLogger().log(Level.SEVERE, "Failed to load Melon & Pumpkin data from .dat file...", e);
		  }
	  }
	  
	  public static void CheckFuncties()
	  {
	    addFunction("CustomTime", "Custom.CustomTime", "FALSE");
	    addFunction("Particles", "Custom.Particles", "FALSE");
	    addFunction("Randomizer", "Custom.Randomizer", "FALSE");
	    addFunction("TwerkPerSecond", "Custom.TwerkPerSecond", "FALSE");
	    addFunction("DisableBoneMeal", "Custom.DisableBoneMeal", "FALSE");
	    
	    Core.getInstance().Functions.add("Twerking");
	  }	  
	  public static boolean checkFunctionState(String Function) {
		  if(Core.getInstance().getConfig().getString("Custom." + Function).equals("TRUE")) {
			  return true;
		  }
		return false;
	  }
	  
	  public static String locationToString(Location loc) {
		  return loc.getBlock().getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
	  }
	  public static Location stringToLocation(String[] loc) {
		  return new Location(Bukkit.getWorld(loc[0]), Double.valueOf(loc[1]), Double.valueOf(loc[2]), Double.valueOf(loc[3]));
	  }
}