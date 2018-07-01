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
@SuppressWarnings({"WeakerAccess", "unused"})
public final class ArcaneText {
    /**
     * Activates URL text by splitting string by space and analyzing each word for URL presence.
     * @param SpaceDelimitedString String with spaces that contains URL
     * @return Text with activated (clickable) URL within words
     */
    public static BaseComponent url(String SpaceDelimitedString) {
        return url(SpaceDelimitedString.split(" "), 0);
    }

    /**
     * Activates URL text by analyzing each word for URL presence.
     * @param ArrayWithLink String array that contains URL
     * @return Text with activated (clickable) URL within words
     */
    public static BaseComponent url(String[] ArrayWithLink) {
        return url(ArrayWithLink, 0);
    }

    /**
     * Activates URL text by analyzing each word for URL presence, starting from specified index.
     * Especially useful for command entries where message doesn't begin at first index of array.
     * @param ArrayWithLink String array that contains URL
     * @param fromIndex Index to make message from
     * @return Text with activated (clickable) URL within words
     */
    public static BaseComponent url(String[] ArrayWithLink, int fromIndex) {
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
    public static BaseComponent urlSingle(String url) {
        TextComponent ret = new TextComponent(
                url.startsWith("http://") || url.startsWith("https://")
                        ? url
                        : "http://" + url
        );
        ret.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return ret;
    }

    /**
     * @param name Name to set
     * @param displayName Name to display
     * @param uuid UUID to display
     * @return Clickable name text as a component with hover text
     */
    public static BaseComponent playerComponent(String name, String displayName, String uuid) {
        return playerComponent(name, displayName, uuid, null, true);
    }

    /**
     * @param name Name to set
     * @param displayName Name to display
     * @param uuid UUID to display
     * @param detail Details to display on hover in gray, italic text
     * @return Clickable name text as a component with hover text
     */
    public static BaseComponent playerComponent(String name, String displayName, String uuid, String detail, boolean clickable) {
        if (uuid == null || uuid.equals("")) {
            BaseComponent ret = new TextComponent(displayName);
            if (detail != null)
                ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(detail).color(ChatColor.GRAY).italic(true).create()));
            return ret;
        }

        BaseComponent ret = new TextComponent(displayName);

        if (detail == null) {
            // TODO: 1.13 might change show_entity hover for players.
            ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY,
                    new ComponentBuilder("{name:\"" + name + "\", id:\"" + uuid + "\"}").create()
            ));
        } else {
            ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(name + " ").color(ChatColor.RESET) // Color is reset here because this keeps getting italicized
                            .append(detail, ComponentBuilder.FormatRetention.NONE).italic(true).color(ChatColor.GRAY).append("\n")
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
    public static BaseComponent playerComponentSpigot(org.bukkit.command.CommandSender sender) {
        return playerComponentSpigot(sender, null);
    }

    /**
     * @param sender Sender to make clickable component from
     * @param detail Details to display on hover in gray, italic text
     * @return Clickable name text as a component with hover text
     */
    public static BaseComponent playerComponentSpigot(org.bukkit.command.CommandSender sender, String detail) {
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
                new ComponentBuilder("{name:\"" + e.getName() + "\", type:\"" + e.getType() + "\", uuid:\"" + e.getUniqueId() + "\"}").create()));
        return ret;
    }

    /**
     * @param sender Sender to make clickable component from
     * @return Clickable name text as a component with hover text
     */
    public static BaseComponent playerComponentBungee(net.md_5.bungee.api.CommandSender sender) {
        return playerComponentBungee(sender, null);
    }

    /**
     * @param sender Sender to make clickable component from
     * @param detail Details to display on hover in gray, italic text
     * @return Clickable name text as a component with hover text
     */
    public static BaseComponent playerComponentBungee(net.md_5.bungee.api.CommandSender sender, String detail) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            return playerComponent(p.getName(), p.getDisplayName(), p.getUniqueId().toString(), detail, true);
        }
        return new TextComponent("Server");
    }

    /**
     * @param translate Translatable node from Mojang's translation list
     * @return Usage: "translated node"
     * @deprecated use usage(BaseComponent usage) with TranslatableComponent instead
     */
    @Deprecated
    public static BaseComponent usageTranslatable(String translate) {
        BaseComponent ret = new TranslatableComponent("commands.generic.usage", new TranslatableComponent(translate));
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * @param usage Usage
     * @return Usage: "translated node"
     */
    public static BaseComponent usage(BaseComponent usage) {
        BaseComponent ret = new TranslatableComponent("commands.generic.usage", usage);
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * @param usage Usage in text
     * @return Usage: "text"
     */
    public static BaseComponent usage(String usage) {
        BaseComponent ret;
        if (usage.startsWith("commands."))
            ret = new TranslatableComponent("commands.generic.usage", new TranslatableComponent(usage));
        else
            ret = new TranslatableComponent("commands.generic.usage", usage);
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
    public static BaseComponent numberOutOfRange(int n, int min, int max) {
        BaseComponent ret;
        if (n < min)
            ret = new TranslatableComponent("commands.generic.num.tooSmall", String.valueOf(n), String.valueOf(min));
        else if (n > max)
            ret = new TranslatableComponent("commands.generic.num.tooBig", String.valueOf(n), String.valueOf(max));
        else
            return null;

        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * @param player Player name attempted to find
     * @return Player not found message
     */
    public static BaseComponent playerNotFound(String player) {
        BaseComponent ret = new TranslatableComponent("commands.generic.player.notFound", player);
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * @return TranslatableComponent of "commands.generic.permission"
     */
    public static BaseComponent noPermissionMsg() {
        BaseComponent ret = new TranslatableComponent("commands.generic.permission");
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * @return TextComponent saying the action must be done by a player
     */
    public static BaseComponent noConsoleMsg() {
        return new TextComponent("You must be a player.");
    }
}