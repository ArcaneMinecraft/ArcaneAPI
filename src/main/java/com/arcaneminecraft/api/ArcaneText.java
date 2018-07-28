package com.arcaneminecraft.api;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * ArcaneCommons Class.
 * This class is to be shared between all the other plugins, favorably with
 * Arcane plugins.
 *
 * @author Simon Chuu (SimonOrJ)
 */
@SuppressWarnings({"unused"})
public interface ArcaneText {
    String THIS_NETWORK_NAME_SHORT = "Arcane";
    String THIS_NETWORK_NAME = "Arcane Survival";

    /**
     * @return the full name of this network, "Arcane Survival".
     */
    static String getThisNetworkName() {
        return THIS_NETWORK_NAME;
    }

    /**
     * @return the short name of this network, "Arcane".
     */
    static String getThisNetworkNameShort() {
        return THIS_NETWORK_NAME_SHORT;
    }

    /**
     * Activates URL text by splitting string by space and analyzing each word for URL presence.
     * @param SpaceDelimitedString String with spaces that contains URL
     * @return Text with activated (clickable) URL within words
     */
    static BaseComponent url(String SpaceDelimitedString) {
        return url(SpaceDelimitedString.split(" "), 0);
    }

    /**
     * Activates URL text by analyzing each word for URL presence.
     * @param ArrayWithLink String array that contains URL
     * @return Text with activated (clickable) URL within words
     */
    static BaseComponent url(String[] ArrayWithLink) {
        return url(ArrayWithLink, 0);
    }

    /**
     * Activates URL text by analyzing each word for URL presence, starting from specified index.
     * Especially useful for command entries where message doesn't begin at first index of array.
     * @param ArrayWithLink String array that contains URL
     * @param fromIndex Index to make message from
     * @return Text with activated (clickable) URL within words
     */
    static BaseComponent url(String[] ArrayWithLink, int fromIndex) {
        BaseComponent ret = new TextComponent();
        for (int i = fromIndex; i < ArrayWithLink.length; i++) {
            if (i != fromIndex) ret.addExtra(" ");
            //noinspection RegExpRedundantEscape (the extra escapes are required!!!)
            if (ArrayWithLink[i].matches(".+\\..+|http(s?):\\/\\/.+")) {
                ret.addExtra(urlSingle(ArrayWithLink[i]));
            } else {
                ret.addExtra(ArrayWithLink[i]);
            }
        }
        return ret;
    }

    /**
     * Activates string as a clickable URL.
     * @param url String that is definitely a URL
     * @return Text with activated (clickable) URL
     */
    static BaseComponent urlSingle(String url) {
        TextComponent ret = new TextComponent(url);
        ret.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                url.startsWith("http://") || url.startsWith("https://")
                        ? url
                        : "http://" + url
        ));
        return ret;
    }

    /**
     * @param name Name to set
     * @param displayName Name to display
     * @param uuid UUID to display
     * @return Clickable name text as a component with hover text
     */
    static BaseComponent playerComponent(String name, String displayName, String uuid) {
        return playerComponent(name, displayName, uuid, null, true);
    }

    /**
     * @param name Name to set
     * @param displayName Name to display
     * @param uuid UUID to display
     * @param detail Details to display on hover in gray, italic text
     * @return Clickable name text as a component with hover text
     */
    static BaseComponent playerComponent(String name, String displayName, String uuid, String detail) {
        return playerComponent(name, displayName, uuid, detail, true);
    }
    /**
     * @param name Name to set
     * @param displayName Name to display
     * @param uuid UUID to display
     * @param detail Details to display on hover in gray, italic text
     * @param clickable Makes the text activate with a click event
     * @return Clickable name text as a component with hover text
     */
    static BaseComponent playerComponent(String name, String displayName, String uuid, String detail, boolean clickable) {
        if (displayName == null)
            displayName = name;
        if (uuid == null || uuid.equals("")) {
            BaseComponent ret = new TextComponent(displayName);
            if (detail != null)
                ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(detail).color(ChatColor.GRAY).italic(true).create()));
            return ret;
        }

        BaseComponent ret = new TextComponent(displayName);

        if (detail == null) {
            // TODO: The following does not work properly with current MC 1.13-pre7.
            //ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY,
            //        new ComponentBuilder("{name:\"" + name + "\", type:\"minecraft:player\", id:" + uuid + "}").create()
            //));
            // BEGIN Temporary fix
            ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(name + "\n") // Color is reset here because this keeps getting italicized
                            .append("Type: minecraft:player", ComponentBuilder.FormatRetention.NONE).append("\n")
                            .append(uuid, ComponentBuilder.FormatRetention.NONE).create()
            ));
            // END Temporary fix
        } else {
            ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(name + " ").color(ChatColor.RESET) // Color is reset here because this keeps getting italicized
                            .append(detail, ComponentBuilder.FormatRetention.NONE).italic(true).color(ChatColor.GRAY).append("\n")
                            .append("Type: minecraft:player", ComponentBuilder.FormatRetention.NONE).append("\n")
                            .append(uuid, ComponentBuilder.FormatRetention.NONE).create()
            ));

        }
        if (clickable)
            ret.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + name + " "));

        return ret;
    }

    /**
     * @param sender Sender to make clickable component from
     * @return Clickable name text as a component with hover text
     */
    static BaseComponent playerComponentSpigot(org.bukkit.command.CommandSender sender) {
        return playerComponentSpigot(sender, null);
    }

    /**
     * @param sender Sender to make clickable component from
     * @param detail Details to display on hover in gray, italic text
     * @return Clickable name text as a component with hover text
     */
    static BaseComponent playerComponentSpigot(org.bukkit.command.CommandSender sender, String detail) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            return playerComponent(p.getName(), p.getDisplayName(), p.getUniqueId().toString(), detail, true);
        }
        if (sender instanceof ConsoleCommandSender) {
            return new TextComponent("Server");
        }

        Entity e = (Entity)sender;
        BaseComponent ret = new TextComponent(e.getCustomName());
        ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY,
                new ComponentBuilder("{name:\"" + e.getName() + "\", type:\"" + e.getType() + "\", id:\"" + e.getUniqueId() + "\"}").create()));
        return ret;
    }

    /**
     * @param sender Sender to make clickable component from
     * @return Clickable name text as a component with hover text
     */
    static BaseComponent playerComponentBungee(net.md_5.bungee.api.CommandSender sender) {
        return playerComponentBungee(sender, null);
    }

    /**
     * @param sender Sender to make clickable component from
     * @param detail Details to display on hover in gray, italic text
     * @return Clickable name text as a component with hover text
     */
    static BaseComponent playerComponentBungee(net.md_5.bungee.api.CommandSender sender, String detail) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            return playerComponent(p.getName(), p.getDisplayName(), p.getUniqueId().toString(), detail, true);
        }
        return new TextComponent("Server");
    }

    /**
     * @param usage Usage
     * @return Usage: "translated node"
     */
    static BaseComponent usage(BaseComponent usage) {
        BaseComponent ret = new TranslatableComponent("Usage: %s", usage); // There is no new translatable node
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * @param usage Usage in text
     * @return Usage: "text"
     */
    static BaseComponent usage(String usage) {
        BaseComponent ret = new TextComponent("Usage: "); // There is no new translatable node
        if (usage.startsWith("commands."))
            ret.addExtra(new TranslatableComponent(usage));
        else
            ret.addExtra(usage);
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * Checks if the number if greater than or less than min or max, then sends appropriate text.
     * @param n Number to check
     * @param min Minimum bound
     * @param max Maximum bound
     * @return Appropriate message if n is out of bounds, else null.
     */
    static BaseComponent numberOutOfRange(int n, int min, int max) {
        BaseComponent ret;
        if (n < min)
            ret = new TranslatableComponent("argument.integer.low", min, n);
        else if (n > max)
            ret = new TranslatableComponent("argument.integer.big", max, n);
        else
            return null;

        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * @param player Player name attempted to find
     * @return Player not found message
     * @deprecated Player is no longer specified in default translation
     */
    @Deprecated
    static BaseComponent playerNotFound(String player) {
        return playerNotFound();
    }

    /**
     * @return Unknown player message
     */
    static BaseComponent playerNotFound() {
        BaseComponent ret = new TranslatableComponent("argument.player.unknown");
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * @return TranslatableComponent of "commands.generic.permission"
     * @deprecated MC 1.13 no longer have "not enough permission" message. Use noCommandPermission() or argumentInvalid()
     */
    @Deprecated
    static BaseComponent noPermissionMsg() {
        return noCommandPermission();
    }

    /**
     * @return Unknown command or insufficient permissions
     */
    static BaseComponent noCommandPermission() {
        BaseComponent ret = new TextComponent("commands.help.failed");
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * @return Incorrect argument for command
     */
    static BaseComponent argumentInvalid() {
        BaseComponent ret = new TextComponent("command.unknown.argument");
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * @return TextComponent saying the action must be done by a player
     */
    static BaseComponent noConsoleMsg() {
        return new TextComponent("You must be a player.");
    }
}