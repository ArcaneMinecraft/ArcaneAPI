package com.arcaneminecraft.api;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ArcaneAPITest {

    @Test
    public void checkURLCreation() {
        BaseComponent empty = ArcaneText.url("");
        BaseComponent not_url = ArcaneText.url("http:/test");
        BaseComponent url_dot = ArcaneText.url("arcaneminecraft.com");
        BaseComponent url_http_no_dot = ArcaneText.url("http://localhost");
        BaseComponent multiple_hyperlink = ArcaneText.url("https://arcaneminecraft.com arcaneminecraft.com http://arcaneminecraft.com");
        BaseComponent hyperlink_around_text = ArcaneText.url("Go to arcaneminecraft.com for fun vanilla experience! (or check out http://localhost too)");

        assertNull("An empty string", empty.getExtra().get(0).getClickEvent());
        assertNull("'http:/text' which is not a URL", not_url.getExtra().get(0).getClickEvent());
        assertNotNull("URL with dot", url_dot.getExtra().get(0).getClickEvent());
        assertNotNull("URL with http but no dot", url_http_no_dot.getExtra().get(0).getClickEvent());

        assertNotNull("Multiple hyperlinks", multiple_hyperlink.getExtra().get(0).getClickEvent());
        assertNotNull("Multiple hyperlinks", multiple_hyperlink.getExtra().get(2).getClickEvent());
        assertNotNull("Multiple hyperlinks", multiple_hyperlink.getExtra().get(4).getClickEvent());

        assertNull("Text in hyperlink around text", hyperlink_around_text.getExtra().get(0).getClickEvent());
        assertNotNull("Link in hyperlink around text", hyperlink_around_text.getExtra().get(4).getClickEvent());
        assertNull("Link in hyperlink around text", hyperlink_around_text.getExtra().get(12).getClickEvent());
        assertNotNull("Link in hyperlink around text", hyperlink_around_text.getExtra().get(20).getClickEvent());
    }

    @Test
    public void playerComponentCreation() {
        // TODO: Fix tests that are commented out
        Player mp = mock(Player.class);
        when(mp.getName()).thenReturn("SimonOrJ");
        when(mp.getDisplayName()).thenReturn("Simon Chuu");
        when(mp.getUniqueId()).thenReturn(UUID.fromString("39d83509-f85f-492a-ba8d-f54ad74c2682"));

        BaseComponent mcPlayer = ArcaneText.playerComponentSpigot(mp);
        assertEquals("Simon Chuu", mcPlayer.toPlainText());
        assertEquals("/msg SimonOrJ ", mcPlayer.getClickEvent().getValue());
        //assertEquals("Player entity hover", "{name:\"SimonOrJ\", id:\"39d83509-f85f-492a-ba8d-f54ad74c2682\"}", mcPlayer.getHoverEvent().getValue()[0].toPlainText());

        ProxiedPlayer bp = mock(ProxiedPlayer.class);
        when(bp.getName()).thenReturn("SimonOrJ");
        when(bp.getDisplayName()).thenReturn("Simon Chuu");
        when(bp.getUniqueId()).thenReturn(UUID.fromString("39d83509-f85f-492a-ba8d-f54ad74c2682"));

        BaseComponent bcPlayer = ArcaneText.playerComponentSpigot(mp, "random detail");
        assertEquals("Simon Chuu", bcPlayer.toPlainText());
        assertEquals("/msg SimonOrJ ", bcPlayer.getClickEvent().getValue());
        StringBuilder hoverValue = new StringBuilder();
        for (BaseComponent bc : bcPlayer.getHoverEvent().getValue())
            hoverValue.append(bc.toPlainText());
        //assertEquals("Player custom detailed Hover", "SimonOrJ random detail\n39d83509-f85f-492a-ba8d-f54ad74c2682", hoverValue.toString());
    }

/* TODO: Usage is no longer used in this way
    @Test
    public void usageText() {
        assertEquals("Usage: /arcane <isBestServer>", ArcaneText.usage("/arcane <isBestServer>").toPlainText());
        assertEquals("Usage: /me <action ...>", ArcaneText.usage("commands.me.usage").toPlainText());
    }
*/

    @Test
    public void outOfRangeTest() {
        assertNull(ArcaneText.numberOutOfRange(1,1,2));
        assertSame("argument.integer.big",((TranslatableComponent) ArcaneText.numberOutOfRange(3,1,2)).getTranslate());
        assertSame("argument.integer.low",((TranslatableComponent) ArcaneText.numberOutOfRange(0,1,2)).getTranslate());
    }

    @Test
    public void checkExistenceOfRequiredArcaneColorFields() {
        assertSame(ChatColor.class, ArcaneColor.HEADING.getClass());
        assertSame(ChatColor.class, ArcaneColor.CONTENT.getClass());
        assertSame(ChatColor.class, ArcaneColor.FOCUS.getClass());
        assertSame(ChatColor.class, ArcaneColor.NEGATIVE.getClass());
        assertSame(ChatColor.class, ArcaneColor.POSITIVE.getClass());
        assertSame(ChatColor.class, ArcaneColor.MAP.getClass());
        assertSame(ChatColor.class, ArcaneColor.DONOR.getClass());
        assertSame(ChatColor.class, ArcaneColor.RESET.getClass());
        assertSame(ChatColor.class, ArcaneColor.META.getClass());
    }
}