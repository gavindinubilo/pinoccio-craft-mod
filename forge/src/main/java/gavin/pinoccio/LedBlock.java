package gavin.pinoccio;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LedBlock extends Block {
	public PinoccioAPI pinoccioAPI = new PinoccioAPI();
	
	public int troopId;
	public int scoutId;
	public String apiKey;
	public String color = "led.red";

	protected LedBlock(Material mat, String api, int troop, int scout) {
		
		super(mat);
		this.setBlockTextureName("pinoccio:ledBlock");
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setBlockName("ledBlock");
		this.troopId = troop;
		this.scoutId = scout;
		this.apiKey = api;
	}
	
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
		Minecraft.getMinecraft().thePlayer.sendChatMessage("Current command: " + this.color);
		

        return true;
    }
	public void onNeighborBlockChange(World world, int x, int y, int z, Block id)
    {
		Block upBlock = world.getBlock(x, y + 1, z);
		if(Block.getIdFromBlock(upBlock) == 35){
			int data = world.getBlockMetadata(x, y + 1, z);
			switch(data) {
				case 0: this.color = "led.white";
						break;
				case 1: this.color = "led.orange";
						break;
				case 2: this.color = "led.magenta";
						break;
				case 3: this.color = "led.setrgb(135,206,250)";
						break;
				case 4: this.color = "led.yellow";
						break;
				case 5: this.color = "led.setrgb(50, 205, 50)";
						break;
				case 6: this.color = "led.setrgb(255, 105, 180)";
						break;
				case 7: this.color = "led.setrgb(125, 125, 125)";
						break;
				case 8: this.color = "led.setrgb(200,200,200)";
						break;
				case 9: this.color = "led.cyan";
						break;
				case 10: this.color = "led.purple";
						 break;
				case 11: this.color = "led.blue";
						 break;
				case 12: this.color = "led.sethex(a52a2a)";
						 break;
				case 13: this.color = "led.green";
						 break;
				case 14: this.color = "led.red";
						 break;
				default: this.color = "led.off";
						 break;
			}
		}
		
        if (!world.isRemote)
        {
            if (world.isBlockIndirectlyGettingPowered(x, y, z))
            {
            	try {
					this.pinoccioAPI.runBitlashCommand(this.troopId, this.scoutId, this.color, this.apiKey);
				} catch (Exception e) {
					Minecraft.getMinecraft().thePlayer.sendChatMessage("Command could not be sent, please check your Pinoccio Scout");
				}
            	this.isProvidingStrongPower(world, x, y, z, z);
            } else {
            	try {
					this.pinoccioAPI.runBitlashCommand(this.troopId, this.scoutId, "led.off", this.apiKey);
				} catch (Exception e) {
					Minecraft.getMinecraft().thePlayer.sendChatMessage("Command could not be sent, please check your Pinoccio Scout");
				}
            	this.isProvidingWeakPower(world, x, y, z, z);
            }
            	
        }
    }
	
}
