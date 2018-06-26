package com.arcaneminecraft.api;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class BungeeArcAPI extends Plugin {


    @Override
    public void onEnable() {
        getLogger().info("ArcaneAPI Loaded successfully.");
        getProxy().getPluginManager().registerCommand(this, new MainCommand());
    }

    public class MainCommand extends Command {
        public MainCommand() {
            super("arcaneapi", null, "arcane api");
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
