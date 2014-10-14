package gavin.pinoccio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ReadCommand extends CommandBase
{
		
	  public PinoccioAPI pinoccioAPI = new PinoccioAPI();
	
	  public int troopId;
	  public int scoutId;
	  public String apiKey;
	
	  private List aliases;
	  public String[] commands = {"d2", "d3", "d4", "d5", "d6", "d7", "d8"};
	  public ReadCommand(String key, int troop, int scout)
	  {
	    this.aliases = new ArrayList();
	    this.aliases.add("read");
	    this.aliases.add("r");
	    this.apiKey = key;
	    this.troopId = troop;
	    this.scoutId = scout;
	    
	  }

	  @Override
	  public String getCommandName()
	  {
	    return "read";
	  }

	  @Override
	  public String getCommandUsage(ICommandSender icommandsender)
	  {
	    return "read <pin>";
	  }

	  @Override
	  public List getCommandAliases()
	  {
	    return this.aliases;
	  }

	  @Override
	  public void processCommand(ICommandSender icommandsender, String[] astring)
	  {
	    if(astring.length == 0)
	    {
	    	Minecraft.getMinecraft().thePlayer.sendChatMessage("Not enough pins entered, please use one");
	    	throw new WrongUsageException("commands.read.usage", new Object[0]);
	    }
	    if(astring.length > 1) {
	    	Minecraft.getMinecraft().thePlayer.sendChatMessage("Too many pins, please only use one");
	    	throw new CommandException("commands.read.usage", new Object[0]);
	    }
	    boolean pass = false;
	    int meta = 0;
	    for(int i = 0; i < this.commands.length; i++) {
	    	if (astring[0].equals(this.commands[i])) {
	    		pass = true;
	    		meta = i;
	    		i = this.commands.length;
	    	}
	    }
	    if(!pass) {
	    	Minecraft.getMinecraft().thePlayer.sendChatMessage("Incorrect pin, must be: d2, d3, d4, d5, d6, d7 or d8");
	    	throw new CommandException("commands.read.usage", new Object[0]);
	    }
	    try {
		    JsonElement stuff = this.pinoccioAPI.runJsonBitlashCommand(this.troopId, this.scoutId, "pin.report.digital", this.apiKey);
	    	JsonObject data = stuff.getAsJsonObject();
	    	String[] reply = data.toString().split(",");
	    	String newString = "";
	    
	    	if(meta == 0) {
	    		newString = reply[meta + 11].split("\\[")[1];            		
	    	} else {
	    		newString = reply[meta + 11];
	    	}          
	    	for(int i = 0; i < reply.length ; i++) {
	    		System.out.println(i + reply[i]);
	    	}
	    	if(newString.equals("1")) {
//	    		Minecraft.getMinecraft().thePlayer.sendChatMessage("Pin off");
	    		throw new CommandException("commands.read.usage", new Object[0]);
	    	} else if(newString.equals("0")) {
	    	} else {
//	    		Minecraft.getMinecraft().thePlayer.sendChatMessage("Pin not set");
	    		throw new CommandException("commands.read.usage", new Object[0]);
	    	}
	    } catch (IOException e) {
	    	Minecraft.getMinecraft().thePlayer.sendChatMessage("Command could not be sent, please check your Pinoccio Scout");
	    	throw new CommandException("commands.read.failed", new Object[0]);
	    }
	  }

	  @Override
	  public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
	  {
	    return true;
	  }

	  @Override
	  public List addTabCompletionOptions(ICommandSender icommandsender,
	      String[] astring)
	  {
	    return null;
	  }

	  @Override
	  public boolean isUsernameIndex(String[] astring, int i)
	  {
	    return false;
	  }

	  @Override
	  public int compareTo(Object o)
	  {
	    return 0;
	  }
	}