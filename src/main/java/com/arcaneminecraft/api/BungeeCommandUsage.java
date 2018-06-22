package com.arcaneminecraft.api;

public enum BungeeCommandUsage {
    AFK             ("afk"),
    APPLY           ("apply"),
    GREYLIST        ("greylist",    null,                   "arcane.command.greylist"),
    REPLY           ("reply",       "<private message ...>"),
    LINKS           ("links"),
    ME              ("me",          "commands.me.usage"),
    MSG             ("msg",         "commands.message.usage"),
    NEWS            ("news"),
    PING            ("ping",        "[player]"),
    SEEN            ("seen",        "<player>"),
    SLAP            ("slap",        "<player>",             "arcane.command.slap"),
    FINDPLAYER      ("findplayer",  "<part of name>"),
    FIRSTSEEN       ("firstseen",   "[player]"),
    STAFFCHAT       ("a",           "<staff message ...>",  "arcane.command.a"),
    STAFFCHATTOGGLE ("atoggle",     null,                   "arcane.command.a");

    private final String name;
    private final String usage;
    private final String permission;
    private final boolean vanillaNode;

    BungeeCommandUsage(String name){
        this.name = name;
        this.usage = "/" + name;
        this.permission = null;
        this.vanillaNode = false;
    }

    BungeeCommandUsage(String name, String usage){
        this.name = name;
        if (usage.startsWith("commands.")) {
            this.usage = usage;
            this.vanillaNode = true;
        } else {
            this.usage = "/" + name + " " + usage;
            this.vanillaNode = false;
        }
        this.permission = null;
    }

    BungeeCommandUsage(String name, String usage, String permission){
        this.name = name;
        if (usage != null && usage.startsWith("commands.")) {
            this.usage = usage;
            this.vanillaNode = true;
        } else {
            this.usage = "/" + name + (usage == null ? "" : " " + usage);
            this.vanillaNode = false;
        }
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return "/" + name;
    }

    public String getUsage() {
        return usage;
    }

    public boolean usageIsVanillaNode() {
        return vanillaNode;
    }

    public String getPermission() {
        return permission;
    }
}
