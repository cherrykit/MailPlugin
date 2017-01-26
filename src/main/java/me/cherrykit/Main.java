package me.cherrykit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		Mail.getConnection();
	}
	
	@Override
	public void onDisable() {
		Mail.closeConnection();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//Registers mail command
		if (cmd.getName().equalsIgnoreCase("mail") && sender instanceof Player) {
			Player p = (Player) sender;
			
			//Registers type of command
			String type;
            try {
            	type = args[0];
            } catch (Exception e) {
            	type = "";
            }
			
            //Differs between send, clear and read
            switch (type) {
            
            case "send" :
            	
            	try { 
            		String addressee = args[1];
            		String message = "";
            		
            		//Turn remaining args into one string
            		int length = args.length;
            		for (int i = 2; i < length; i++) {
            			message = message + args[i] + " ";
            		}
            		
            		Mail.saveMail(addressee, message, p.getName());
            		
            		p.sendMessage(ChatColor.GREEN + "Your mail has been sent.");
            		
            	} catch (Exception e) {
            		p.sendMessage("Syntax: /mail send <player> <message>");
            	}
            	
            	break;
            	
            case "clear" :
            	
            	Mail.deleteMail(p.getName());
            	p.sendMessage(ChatColor.GREEN + "Your mail has been deleted.");
            	
            	break;
            	
            case "read" :
            	
            	ArrayList<String> mail = Mail.getMail(p.getName());
            	
            	//Sends player a message for each mail
            	if (!mail.isEmpty()) {
            		for (int i = 0; i < mail.size(); i++) {
                		p.sendMessage(ChatColor.GOLD + mail.get(i));
                	}
            	}
            	
            	break;
            	
            default:
            	
            	p.sendMessage(ChatColor.RED + "Syntax: /mail <read/clear/send> (player) (message)");
            	
            }
			return true;
		}
		return false;
	}
	
}
