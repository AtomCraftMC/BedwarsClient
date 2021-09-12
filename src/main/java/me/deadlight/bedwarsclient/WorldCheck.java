package me.deadlight.bedwarsclient;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorldCheck {
    public HashMap<String, Integer> stucking = new HashMap<>();
    public List<String> players = new ArrayList<>();

    public WorldCheck(BedwarsClient bedwarsClient) {
        startCheck(bedwarsClient);
    }


    public void startCheck(BedwarsClient bedwarsClient) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BedwarsClient.getInstance(), new java.lang.Runnable() {
            @Override
            public void run() {
                List<Player> worldPlayers = bedwarsClient.getServer().getWorld("world").getPlayers();
                for (Player player : worldPlayers) {

                    if (stucking.containsKey(player.getName())) {
                        int seconds = stucking.get(player.getName());
                        if (seconds >= 5) {
                            Utils.sendPlayerToServer(player);
                            stucking.remove(player.getName());
                        } else {
                            stucking.put(player.getName(), seconds + 1);
                        }

                    } else {

                        if (player.hasPermission("client.bypass")) {
                            continue;
                        }
                        stucking.put(player.getName(), 1);
                        player.sendTitle(Utils.colorify("&e⚠"), Utils.colorify("&cDar hale Enteghal..."));

                    }
                }

                List<String> newPlayers = new ArrayList<>();
                for (Player player : worldPlayers) {
                    newPlayers.add(player.getName());
                }

                for (String oldPlayer : players) {
                    if (!newPlayers.contains(oldPlayer)) {
                        stucking.remove(oldPlayer);
                    }
                }
                players = newPlayers;




            }
        }, 0, 20);
    }

}
