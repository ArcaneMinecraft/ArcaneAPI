package com.arcaneminecraft.api;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

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
     * Gets string in translated form.
     * @param locale Language locale for the message; null for default.
     * @param translatable Translatable String
     * @param args arguments in case of  placeholders(%s) to replace
     * @return the translated form of string.
     */
    static BaseComponent translatable(Locale locale, String translatable, Object... args) {
        String msg = locale == null
                ? ResourceBundle.getBundle("i18n.Messages").getString(translatable)
                : ResourceBundle.getBundle("i18n.Messages", locale).getString(translatable);

        if (args.length == 0)
            return new TextComponent(msg);
        else
            return new TranslatableComponent(msg, args);
    }

    static String translatableString(Locale locale, String translatable) {
        return locale == null
                ? ResourceBundle.getBundle("i18n.Messages").getString(translatable)
                : ResourceBundle.getBundle("i18n.Messages", locale).getString(translatable);
    }

    /**
     * Gets string chosen at random from list in translated form.
     * @param locale Language locale for the message; null for default.
     * @param translatable Translatable String Array (comma-spliced)
     * @param args arguments in case of  placeholders(%s) to replace
     * @return the translated form of string.
     */
    static BaseComponent translatableListRandom(Locale locale, String translatable, Object... args) {
        String[] msgs = (locale == null
                ? ResourceBundle.getBundle("i18n.Messages").getString(translatable)
                : ResourceBundle.getBundle("i18n.Messages", locale).getString(translatable)
        ).split("\n");

        String msg = msgs[new Random().nextInt(msgs.length)];

        if (args.length == 0)
            return new TextComponent(msg);
        else
            return new TranslatableComponent(msg, args);
    }

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
        ComponentBuilder cb = new ComponentBuilder("");
        StringBuilder sb = new StringBuilder();
        for (int i = fromIndex; i < ArrayWithLink.length; i++) {
            if (i != fromIndex) sb.append(' ');

            if (ArrayWithLink[i].matches("\\S+\\.\\S+|http(s?)://\\S+")) {
                cb.append(TextComponent.fromLegacyText(sb.toString()), ComponentBuilder.FormatRetention.FORMATTING);
                cb.append(urlSingle(ArrayWithLink[i]));
                sb = new StringBuilder();
            } else if (ArrayWithLink[i].matches("/?r/\\S+")) {
                // Reddit
                cb.append(TextComponent.fromLegacyText(sb.toString()), ComponentBuilder.FormatRetention.FORMATTING);

                String sr = ChatColor.stripColor(ArrayWithLink[i]);
                String url = "https://reddit.com" + (sr.startsWith("/") ? "" : "/") + sr;

                cb.append(urlSingleSpecial(ArrayWithLink[i], url));
                sb = new StringBuilder();
            } else {
                sb.append(ArrayWithLink[i]);
            }
        }
        if (sb.length() != 0)
            cb.append(TextComponent.fromLegacyText(sb.toString()), ComponentBuilder.FormatRetention.FORMATTING);

        Iterator<BaseComponent> i = Arrays.asList(cb.create()).iterator();
        BaseComponent ret = i.next();
        i.forEachRemaining(c -> {
            if (!c.toPlainText().isEmpty())
                ret.addExtra(c);
        });

        return ret;
    }

    /**
     * Activates string as a clickable URL.
     * @param url String that is definitely a URL
     * @return Text with activated (clickable) URL
     */
    static BaseComponent urlSingle(String url) {
        String u = ChatColor.stripColor(url);
        String d;

        // Shorten URL text if too long
        if (u.length() > 35) {
            int first = 29 + url.length() - u.length();
            if (url.charAt(first - 1) == '\u00A7')
                first++;
            int last = url.length() - 6;
            if (url.charAt(last - 1) == '\u00A7')
                last--;
            d = url.substring(0, first);
            d += "\u2026";
            d += url.substring(last);
        } else {
            d = url;
        }

        return urlSingleSpecial(d, u);
    }

    /**
     * Activates string as a clickable URL.
     * @param text The text
     * @param url String that is definitely a URL
     * @return Text with activated (clickable) URL
     */
    static BaseComponent urlSingleSpecial(String text, String url) {
        BaseComponent ret;
        BaseComponent[] lt = TextComponent.fromLegacyText(text);

        if (lt.length == 1) {
            ret = lt[0];
        } else {
            Iterator<BaseComponent> i = Arrays.asList(lt).iterator();
            ret = i.next();
            i.forEachRemaining(c -> {
                c.setClickEvent(null);
                if (!c.toPlainText().isEmpty())
                    ret.addExtra(c);
            });
        }

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
        boolean fromDiscord = detail != null && detail.equalsIgnoreCase("Server: Discord");

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
            ComponentBuilder cb = new ComponentBuilder(name + " ").color(ChatColor.RESET) // Color is reset here because this keeps getting italicized
                    .append(detail, ComponentBuilder.FormatRetention.NONE).italic(true).color(ChatColor.GRAY).append("\n");

            if (!fromDiscord)
                cb.append("Type: minecraft:player", ComponentBuilder.FormatRetention.NONE).append("\n");

            cb.append(uuid, ComponentBuilder.FormatRetention.NONE);

            ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, cb.create()));

        }
        if (clickable && !fromDiscord)
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
     * This calculates second difference and whether the difference is in the past on its own.
     * @param time Timestamp to translate into a string
     * @param locale Locale of the time
     * @param timeZone TimeZone of the player
     * @param focusColor The ChatColor of the focus parts
     * @return Parsed time information (relative time if less than 7 days ago)
     */
    static BaseComponent timeText(Timestamp time, Locale locale, TimeZone timeZone, ChatColor focusColor) {
        // difference in seconds
        int diff = (int) ((System.currentTimeMillis() - time.getTime()) / 1000);
        boolean past;
        if (diff < 0) {
            past = false;
            diff = Math.abs(diff);
        } else {
            past = true;
        }
        return timeText(time, diff, past, locale, timeZone, focusColor);
    }

    /**
     * This calculates the timestamp on its own.
     * @param diff Time difference in seconds
     * @param past If the time was in past
     * @param locale Locale of the time
     * @param timeZone TimeZone of the player
     * @param focusColor The ChatColor of the focus parts
     * @return Parsed time information (relative time if less than 7 days ago)
     */
    static BaseComponent timeText(int diff, boolean past, Locale locale, TimeZone timeZone, ChatColor focusColor) {
        long ms = diff * 1000;

        if (past)
            ms = System.currentTimeMillis() - ms;
        else
            ms = System.currentTimeMillis() + ms;

        Timestamp time = new Timestamp(ms);
        return timeText(time, diff, past, locale, timeZone, focusColor);
    }

    /**
     * @param time Timestamp to translate into a string
     * @param diff Time difference in seconds
     * @param past If the time was in past
     * @param locale Locale of the time
     * @param timeZone TimeZone of the player
     * @param focusColor The ChatColor of the focus parts
     * @return Parsed time information (relative time if less than 7 days ago)
     */
    static BaseComponent timeText(Timestamp time, int diff, boolean past, Locale locale, TimeZone timeZone, ChatColor focusColor) {
        TimeZone zone = timeZone == null ? TimeZone.getDefault() : timeZone;
        Locale loc = locale == null ? Locale.getDefault() : locale;

        BaseComponent ret = new TextComponent();
        if (diff < 60) {
            // Within a minute
            BaseComponent sec = new TextComponent(
                    diff + " second" + (diff == 1 ? "" : "s")
            );
            sec.setColor(focusColor);
            if (!past) ret.addExtra("in ");
            ret.addExtra(sec);
            if (past) ret.addExtra(" ago");
        } else if (diff < 3600) {
            // Within an hour
            int m = diff / 60;

            BaseComponent hour = new TextComponent(
                    m + " minute" + (m == 1 ? "" : "s")
            );
            hour.setColor(focusColor);
            if (!past) ret.addExtra("in ");
            ret.addExtra(hour);
            if (past) ret.addExtra(" ago");
        } else if (diff < 86400) {
            // Within a day
            int h = diff / 3600;

            BaseComponent day = new TextComponent(
                    h + " hour" + (h == 1 ? "" : "s")
            );
            day.setColor(focusColor);
            if (!past) ret.addExtra("in ");
            ret.addExtra(day);
            if (past) ret.addExtra(" ago");
        } else if (diff < 604800) {
            // Within a week
            int d = diff / 86400;
            int h = diff / 3600 % 24;

            BaseComponent day = new TextComponent(
                    d + " day" + (d == 1 ? "" : "s")
            );
            day.setColor(focusColor);
            if (!past) ret.addExtra("in ");
            ret.addExtra(day);
            ret.addExtra(
                    " and " + h + " hour" + (h == 1 ? "" : "s" )
            );
            if (past) ret.addExtra(" ago");
        } else {
            // Over a week
            ret.addExtra("on ");

            DateFormat d = DateFormat.getDateInstance(DateFormat.LONG, loc);
            d.setTimeZone(zone);

            BaseComponent date = new TextComponent(d.format(time));
            date.setColor(focusColor);
            ret.addExtra(date);

            DateFormat t = DateFormat.getTimeInstance(DateFormat.FULL, loc);
            t.setTimeZone(zone);
            ret.addExtra(" at " + t.format(time));
        }

        DateFormat d = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, loc);
        d.setTimeZone(zone);

        ret.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(d.format(time)).create())
        );

        return ret;
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
            ret = new TranslatableComponent("argument.integer.low", String.valueOf(min), String.valueOf(n));
        else if (n > max)
            ret = new TranslatableComponent("argument.integer.big", String.valueOf(max), String.valueOf(n));
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