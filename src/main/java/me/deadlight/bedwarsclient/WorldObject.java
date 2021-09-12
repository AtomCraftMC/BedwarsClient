package me.deadlight.bedwarsclient;

import org.bukkit.entity.Player;

public class WorldObject {

    public String playerName;
    public String previousWorld;

    public WorldObject(String playerName, String previousWorld) {
        this.playerName = playerName;
        this.previousWorld = previousWorld;
    }

}
