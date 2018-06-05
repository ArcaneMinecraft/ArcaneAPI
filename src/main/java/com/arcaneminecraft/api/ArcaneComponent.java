package com.arcaneminecraft.api;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Nameable;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ArcaneComponent {
    public static BaseComponent url(String[] ArrayWithLink) {
        return url(ArrayWithLink, 0);
    }

    public static BaseComponent url(String[] ArrayWithLink, int fromIndex) {
        BaseComponent ret = new TextComponent();
        for (int i = fromIndex; i < ArrayWithLink.length; i++) {
            if (i != fromIndex) ret.addExtra(" ");
            if (ArrayWithLink[i].matches(".+\\..+|http(s?):\\/\\/.+")) {
                String sent = ArrayWithLink[i].startsWith("http://") || ArrayWithLink[i].startsWith("https://") ? ArrayWithLink[i] : "http://" + ArrayWithLink[i];
                TextComponent linked = new TextComponent(ArrayWithLink[i]);
                linked.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, sent));
                ret.addExtra(linked);
            } else {
                ret.addExtra(ArrayWithLink[i]);
            }
        }
        return ret;
    }

    public static BaseComponent player(CommandSender sender) {
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

    public static BaseComponent usage(String usage) {
        return usagePrefix(usage);
    }

    public static BaseComponent usageTranslatable(String translate) {
        return usagePrefix(new TranslatableComponent(translate));
    }

    public static BaseComponent usagePrefix(BaseComponent translatable) {
        BaseComponent ret = new TranslatableComponent("commands.generic.usage", translatable);
        ret.setColor(ChatColor.RED);
        return ret;
    }

    public static BaseComponent usagePrefix(String usage) {
        BaseComponent ret = new TranslatableComponent("commands.generic.usage", usage);
        ret.setColor(ChatColor.RED);
        return ret;
    }
}