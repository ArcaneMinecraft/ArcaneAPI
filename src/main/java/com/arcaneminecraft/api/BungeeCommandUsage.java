package com.arcaneminecraft.api;

@SuppressWarnings("unused")
public enum BungeeCommandUsage {
    APPLY           ("apply", null, "arcane.command.apply", "Apply for the greylist"),
    GREYLIST        ("greylist", null, "arcane.command.greylist"),
    BADGE           ("badge", "[badge name]", null, "Toggles or sets your tag", "b", "tag"),
    BADGEADMIN      ("badgeadmin", "<>", "arcane.command.badgeadmin", "Administrative options for player tags"), // TODO: Usage
    FINDPLAYER      ("findplayer", "<part of name>", null, "Find player name from part of name", "find"),
    FIRSTSEEN       ("firstseen", "[player]", null, "Get the date when player first joined the game", "seenf"),
    SEEN            ("seen", "<player>", null, "Get the date when player last joined the game"),
    LINKS           ("links", null, "arcane.command.links"),
    WEBSITE         ("website", null, "arcane.command.links"),
    FORUM           ("forum", null, "arcane.command.links"),
    DISCORD         ("discord", null, "arcane.command.links"),
    LIST            ("list"),
    ME              ("me", "commands.me.usage"),
    MSG             ("msg", "commands.message.usage", null, null, "tell", "m", "w"),
    REPLY           ("reply", "<private message ...>", null, null, "r"),
    NEWS            ("news"),
    PING            ("ping", "[player]"),
    SLAP            ("slap", "<player>", "arcane.command.slap"),
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
