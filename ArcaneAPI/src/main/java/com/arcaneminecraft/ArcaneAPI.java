package com.arcaneminecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.arcaneminecraft.api.ArcaneCommons;

public class ArcaneAPI extends JavaPlugin {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Possible commands: /arc, /arcane, blah
		
		// Build author string
		Object[] alist = getDescription().getAuthors().toArray();
		StringBuilder authors = new StringBuilder();
		for (int i = 0; i < alist.length - 1; i++) {
			authors.append(alist[i]).append(", ");
		}
		
		authors.append("and ").append(alist[(alist.length - 1)]);
		
		sender.sendMessage(ArcaneCommons.tagMessage("Arcane" + getDescription().getVersion() + ".  Written with <3 by " + authors + "."));

		return true;
	}
}