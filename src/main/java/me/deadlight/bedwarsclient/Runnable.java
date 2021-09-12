package me.deadlight.bedwarsclient;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import redis.clients.jedis.Jedis;
import ro.Fr33styler.ClashWars.Games.Game;
import ro.Fr33styler.ClashWars.Handler.GameState;
import ro.Fr33styler.ClashWars.Handler.Manager.GameManager;

import java.util.ArrayList;
import java.util.List;

public class Runnable {

    public static void startSendingData() {
        //getting all arenas via API
        GameManager manager = BedwarsClient.getInstance().gameManager;
        //states: //ingame, starting, open

        String serverName = BedwarsClient.getInstance().theServerName;

        //SocketClient client = BedwarsClient.getInstance().Sclient;

        Bukkit.getScheduler().runTaskTimerAsynchronously(BedwarsClient.getInstance(), new java.lang.Runnable() {
            @Override
            public void run() {
                try {
                    List<List<String>> finalList = new ArrayList<>();

                    List<Game> gameList = manager.getGames();
                    for (Game game : gameList) {
                        List<String> arenaList = new ArrayList<>();
                        //name, arenaid, max, currentcount, state, startCount, gametype, playerslist, server
                        //name
                        arenaList.add(game.getName());
                        //arena ID
                        arenaList.add(String.valueOf(game.getID()));
                        //max players
                        arenaList.add(String.valueOf(game.getSettings().getMax()));
                        //currentcount
                        arenaList.add(String.valueOf(game.getPlayers().size()));
                        //state
                        GameState gameState = game.getState();
                        if (gameState.equals(GameState.RESETING)) {
                            continue;
                        }
                        if (gameState.equals(GameState.END)) {
                            continue;
                        }
                        if (gameState.equals(GameState.WAITING)) {
                            if (game.getPlayers().size() >= game.getSettings().getMin()) {
                                //then its starting
                                arenaList.add("starting");
                            } else {
                                arenaList.add("open");
                            }
                        } else if (gameState.equals(GameState.IN_GAME)) {
                            arenaList.add("ingame");
                        } else {
                            arenaList.add("other");
                        }
                        //startCount
                        arenaList.add(String.valueOf(game.getSettings().getMin()));
                        //game type
                        arenaList.add(String.valueOf(game.getMode()));
                        //playerslist
                        arenaList.add(Utils.listToString(game.getPlayers()));
                        //server
                        arenaList.add(serverName);
                        finalList.add(arenaList);
                    }


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "arenas");
                    jsonObject.put("data", finalList);
                    jsonObject.put("server", serverName);

                    Jedis j = null;
                    try {
                        j = BedwarsClient.pool.getResource();
                        j.auth("piazcraftmc");
                        j.set(serverName, jsonObject.toJSONString());
                        j.expire(serverName, 1);
                    } finally {
                        if (j != null) {
                            j.close();
                        }
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }


                //BedwarsClient.getInstance().Sclient.sendMessage();


            }
        }, 0,3);


    }


}
