/**
 * ArcaneCommons Class.
 * This class is to be shared between all the other plugins, favorably with
 * Arcane plugins.
 *
 * @author Simon Chuu (SimonOrJ)
 */
package com.arcaneminecraft.api;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Nameable;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public final class ArcaneText {
    public static BaseComponent url(String SpaceDelimitedString) {
        return url(SpaceDelimitedString.split(" "), 0);
    }

    public static BaseComponent url(String[] ArrayWithLink) {
        return url(ArrayWithLink, 0);
    }

    public static BaseComponent url(String[] ArrayWithLink, int fromIndex) {
        BaseComponent ret = new TextComponent();
        for (int i = fromIndex; i < ArrayWithLink.length; i++) {
            if (i != fromIndex) ret.addExtra(" ");
            if (ArrayWithLink[i].matches(".+\\..+|http(s?):\\/\\/.+")) {
                ret.addExtra(urlSingle(ArrayWithLink[i]));
            } else {
                ret.addExtra(ArrayWithLink[i]);
            }
        }
        return ret;
    }

    public static BaseComponent urlSingle(String url) {
        TextComponent ret = new TextComponent(
                url.startsWith("http://") || url.startsWith("https://")
                        ? url
                        : "http://" + url
        );
        ret.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return ret;
    }

    public static BaseComponent playerComponent(String name, String displayName, String uuid) {
        BaseComponent ret = new TextComponent(displayName);
        ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ComponentBuilder("{name:\"" + name + "\", id:\"" + uuid + "\"}").create()));
        ret.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + name + " "));
        return ret;
    }

    public static BaseComponent playerComponentSpigot(org.bukkit.command.CommandSender sender) {
        if (!(sender instanceof Player)) {
            if (sender instanceof ConsoleCommandSender)
                return new TextComponent("Server");
            return new TextComponent(((Nameable) sender).getCustomName());
        }
        Player p = (Player) sender;
        BaseComponent ret = new TextComponent(p.getDisplayName());
        ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ComponentBuilder("{name:\"" + p.getName() + "\", id:\"" + p.getUniqueId() + "\"}").create()));
        ret.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + p.getName() + " "));
        return ret;
    }

    public static BaseComponent playerComponentBungee(net.md_5.bungee.api.CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) {
            return new TextComponent("Server");
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        BaseComponent ret = new TextComponent(p.getDisplayName());
        ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ComponentBuilder("{name:\"" + p.getName() + "\", id:\"" + p.getUniqueId() + "\"}").create()));
        ret.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + p.getName() + " "));
        return ret;
    }

    @Deprecated
    public static BaseComponent usageTranslatable(String translate) {
        BaseComponent ret = new TranslatableComponent("commands.generic.usage", new TranslatableComponent(translate));
        ret.setColor(ChatColor.RED);
        return ret;
    }

    public static BaseComponent usage(String usage) {
        BaseComponent ret;
        if (usage.startsWith("commands."))
            ret = new TranslatableComponent("commands.generic.usage", new TranslatableComponent(usage));
        else
            ret = new TranslatableComponent("commands.generic.usage", usage);
        ret.setColor(ChatColor.RED);
        return ret;
    }

    public static BaseComponent playerNotFound(String player) {
        BaseComponent ret = new TranslatableComponent("commands.generic.player.notFound", player);
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * Generic no permission message
     * @return TranslatableComponent of "commands.generic.permission"
     */
    public static BaseComponent noPermissionMsg() {
        BaseComponent ret = new TranslatableComponent("commands.generic.permission");
        ret.setColor(ChatColor.RED);
        return ret;
    }

    /**
     * Returns a "no-console" message
     * @return TextComponent saying player required
     */
    public static BaseComponent noConsoleMsg() {
        BaseComponent ret = new TextComponent("You must be a player.");
        return ret;
    }
}