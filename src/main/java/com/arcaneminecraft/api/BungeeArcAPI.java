package com.arcaneminecraft.api;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeArcAPI extends Plugin {

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new MainCommand());
    }

    public class MainCommand extends Command {
        private MainCommand() {
            super("barcaneapi", null, "");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (args.length == 0) {
                // General stuff
                return;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                return;
            }
        }
    }

}
