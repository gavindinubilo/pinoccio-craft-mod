package gavin.pinoccio;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.BlockClay;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = PinoccioCraft.modid, version = PinoccioCraft.version)

public class PinoccioCraft {

	public static final String modid = "Pinoccio";
	public static final String version = "1.0";
	public static Block ledBlock;
	public static Block scoutScriptBlock;
	public static Block pinReadBlockOff;
	public static Block pinReadBlockOn;
	public static Item pinoccioHat;
	
	public int troopId;
	public int scoutId;
	public String apiKey;
	
	private static final String[] multiBlockNames = { 
		"d2", "d3", "d4", "d5", "d6", "d7", "d8"
	};
	
	
	@Instance ( "gui" )
    public static PinoccioCraft instance;
	
	
	
	@EventHandler 
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();
        System.out.println(config.getConfigFile().listFiles());
        
        // Here we add a comment to our new property.
        String[] defCommands = {"led.red"};
        String[] defPins = {"d2", "d3", "d4", "d5", "d6", "d7", "d8"};
        
        Property key = config.get(Configuration.CATEGORY_GENERAL, "APIKey", "Change This");
        key.comment = "Insert your api key here";
        
        Property troop= config.get(Configuration.CATEGORY_GENERAL, "TroopId", 1);
        troop.comment = "Insert your troopid here";
        
        Property scout = config.get(Configuration.CATEGORY_GENERAL, "ScoutId", 1);
        scout.comment = "Insert your scoutid here";
        
        Property commands = config.get(Configuration.CATEGORY_GENERAL, "Commands", defCommands);
        commands.comment = "Add commands here (seperated by a line break), I suggest adding to the bottom and not the top";

        Property pins = config.get(Configuration.CATEGORY_GENERAL, "Pins", defPins);
        pins.comment = "Insert the pins here (I suggest not editing these)";
        
        this.apiKey = key.getString();
        this.troopId = troop.getInt();
        this.scoutId = scout.getInt();
        
        config.save();
        String[] commandList = commands.getStringList();
        String[] pinList = pins.getStringList();
		
        ledBlock = new LedBlock(Material.rock, key.getString(), troop.getInt(), scout.getInt());        
		GameRegistry.registerBlock(ledBlock, "ledBlock");
		scoutScriptBlock = new ScoutScriptBlock(Material.rock, key.getString(), troop.getInt(), scout.getInt(), commands.getStringList());
		GameRegistry.registerBlock(scoutScriptBlock, ScoutScriptItemBlock.class, "scoutScriptBlock");
				
		for (int ix = 0; ix < commandList.length; ix++) {
			ItemStack cloth = new ItemStack(new BlockClay(), 1, ix);
			ItemStack multiBlockStack = new ItemStack(scoutScriptBlock, 1, ix);
			
			GameRegistry.addShapelessRecipe(multiBlockStack, cloth, cloth);
			LanguageRegistry.addName(multiBlockStack, commandList[multiBlockStack.getItemDamage()]);
		}		
	}
	
	public void init(FMLInitializationEvent event) {

	}
	@EventHandler
	  public void serverLoad(FMLServerStartingEvent event)
	  {
	    event.registerServerCommand(new ReadCommand(this.apiKey, this.troopId, this.scoutId));
	    event.registerServerCommand(new RunCommand(this.apiKey, this.troopId, this.scoutId));
	    event.registerServerCommand(new LedCommand(this.apiKey, this.troopId, this.scoutId));
	  }
}
