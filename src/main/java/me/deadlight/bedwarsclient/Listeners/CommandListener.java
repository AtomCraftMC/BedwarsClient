package me.deadlight.bedwarsclient.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    @EventHandler
    public void onExecute(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.contains("cosmetic") || message.contains("cosmetics") || message.contains("procosmetics") || message.contains("pc")) {
            if (!event.getPlayer().hasPermission("client.bypass")) {
                event.setCancelled(true);
            }
        }

    }

}
