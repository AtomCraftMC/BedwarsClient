package me.deadlight.bedwarsclient.Listeners;

import me.deadlight.bedwarsclient.BedwarsClient;
import me.deadlight.bedwarsclient.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ro.Fr33styler.ClashWars.Api.GameEndEvent;
import ro.Fr33styler.ClashWars.Api.GameStartEvent;
import ro.Fr33styler.ClashWars.Api.PlayerKillEvent;
import ro.Fr33styler.ClashWars.Games.Game;

import java.util.HashMap;

public class InfoHandlers implements Listener {


    @EventHandler
    public void onGameStart(GameStartEvent event) {
        HashMap<String, TopPlayerDataObject> playersData = new HashMap<>();
        for (Player player : event.getGame().getPlayers()) {
            playersData.put(player.getName(), new TopPlayerDataObject(player.getName(), 0, 0));
        }
        Utils.arenasInfo.put(event.getGame().getID(), playersData);

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player killer = event.getPlayer().getKiller();
        if (killer != null) {

            Game playerGame = BedwarsClient.getInstance().gameManager.getGame(killer);
            if (playerGame == null) {
                return;
            }
            HashMap<String, TopPlayerDataObject> userData = Utils.arenasInfo.get(playerGame.getID());
            TopPlayerDataObject object = userData.get(killer.getName());
            object.setKills(object.kills + 1);
            userData.put(killer.getName(), object);
            Utils.arenasInfo.put(playerGame.getID(), userData);

        }


    }

    @EventHandler
    public void onGameFinish(GameEndEvent event) {


    }


}
