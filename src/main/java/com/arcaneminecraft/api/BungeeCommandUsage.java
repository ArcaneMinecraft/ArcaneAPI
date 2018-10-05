package com.arcaneminecraft.api;

@SuppressWarnings("unused")
public enum BungeeCommandUsage {
    CREATIVE        ("creative", null, "bungeecord.server.creative", "Move to the creative server"),
    SURVIVAL        ("survival", null, null, "Move to the survival server"),
    EVENT           ("event", "[event name]", "arcane.command.event", "Move to the event server"),
    APPLY           ("apply", null, null, "Apply for the greylist"),
    GREYLIST        ("greylist", "<player ...>", "arcane.command.greylist"),
    BADGE           ("badge", "[hide|reset|prefix priority|prefix name]", null, "Change your tag - click on text to select", "b", "tag"),
    BADGEADMIN      ("badgeadmin", "<clear|list|reset|set|setpriority|settemp> [player] ...", "arcane.command.badgeadmin", "Administrative options for player tags", "ba"),
    FINDPLAYER      ("findplayer", "<part of name>", "arcane.command.findplayer", "Find player name from part of name", "find"),
    FIRSTSEEN       ("firstseen", "[player]", "arcane.command.seen", "Get the date when player first joined the game", "seenf"),
    SEEN            ("seen", "<player>", "arcane.command.seen", "Get the date when player last joined the game"),
    LINKS           ("links", null, "arcane.command.links"),
    WEBSITE         ("website", null, "arcane.command.links"),
    DONATE          ("donate", null, "arcane.command.links"),
    RULES           ("rules", null, "arcane.command.links"),
    FORUM           ("forum", null, "arcane.command.links"),
    DISCORD         ("discord", "[link|unlink]", "arcane.command.discord"),
    LIST            ("list"),
    ME              ("me", "<action ...>", "arcane.command.me"),
    MSG             ("msg", "<player> <private message ...>", "arcane.command.msg", null, "tell", "m", "w"),
    REPLY           ("reply", "<private message ...>", "arcane.command.msg", null, "r"),
    NEWS            ("news"),
    OPTION          ("option", "[option name] [value]"),
    PING            ("ping", "[player]"),
    SLAP            ("slap", "<player>", "arcane.command.slap"),
    SPY             ("spy", "<toggles|command|sign|xray> ...", "arcane.command.spy"),
    STAFFCHAT       ("a", "<staff message ...>", "arcane.command.a"),
    STAFFCHATTOGGLE ("atoggle", null, "arcane.command.a", null, "at");

    private final String name;
    private final String usage;
    private final String permission;
    private final String description;
    private final String[] aliases;

    BungeeCommandUsage(String name) {
        this.name = name;
        this.usage = "/" + name;
        this.permission = null;
        this.description = null;
        this.aliases = new String[]{};
    }

    BungeeCommandUsage(String name, String usage) {
        this(name, usage, null, null);
    }

    BungeeCommandUsage(String name, String usage, String permission) {
        this(name, usage, permission, null);
    }

    BungeeCommandUsage(String name, String usage, String permission, String description, String... aliases) {
        this.name = name;
        if (usage == null) {
            this.usage = "/" + name;
        } else {
            if (usage.startsWith("commands.")) {
                this.usage = usage;
            } else {
                this.usage = "/" + name + " " + usage;
            }
        }
        this.permission = permission;
        this.description = description;
        this.aliases = aliases;
    }

    /**
     * @return command name without slash
     */
    public String getName() {
        return name;
    }

    /**
     * @return command with leading slash
     */
    public String getCommand() {
        return "/" + name;
    }

    /**
     * @return usage with leading slash; Not Null
     */
    public String getUsage() {
        return usage;
    }

    /**
     * @return command description; empty string if none
     */
    public String getDescription() {
        return description == null ?  "" : description;
    }

    /**
     * @return command aliases in array.
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Checks if usage is a vanilla translatable node
     * @return true if usage is a vanilla node
     */
    public boolean usageIsVanillaNode() {
        return usage.startsWith("command.");
    }

    /**
     * @return Returns permission node, null if none
     */
    public String getPermission() {
        return permission;
    }
}
