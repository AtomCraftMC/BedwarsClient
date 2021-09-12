package me.deadlight.bedwarsclient.Listeners;
import me.deadlight.bedwarsclient.BedwarsClient;
import me.deadlight.bedwarsclient.Utils;
import me.neznamy.tab.api.EnumProperty;
import me.neznamy.tab.api.TABAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;
import ro.Fr33styler.ClashWars.Api.GameEndEvent;
import ro.Fr33styler.ClashWars.Api.GameLeaveEvent;
import ro.Fr33styler.ClashWars.Api.GameStartEvent;
import ro.Fr33styler.ClashWars.Games.Game;
import ro.Fr33styler.ClashWars.Games.GameTeam;
import ro.Fr33styler.ClashWars.Handler.GameType;
import sv.file14.procosmetics.cosmetic.arroweffect.AbstractArrowEffectType;
import sv.file14.procosmetics.cosmetic.deatheffect.AbstractDeathEffectType;
import sv.file14.procosmetics.user.User;

import java.util.HashMap;

public class PluginListeners implements Listener {
    static JSONParser parser = new JSONParser();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws ParseException {
        Bukkit.getScheduler().scheduleSyncDelayedTask(BedwarsClient.getInstance(), new Runnable() {
            @Override
            public void run() {
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
                event.getPlayer().performCommand("spawn");
                event.getPlayer().setGameMode(GameMode.SURVIVAL);
            }
        }, 2);
        String jresult = null;
        Jedis j = null;
        try {
            j = BedwarsClient.pool.getResource();
            j.auth("piazcraftmc");
            jresult = j.get("ip-" + event.getPlayer().getName());
        } finally {
            if (j != null) {
                j.close();
            }
        }
        if (jresult != null) {
            String ipdata = jresult;
            JSONObject json = (JSONObject) parser.parse(ipdata);
            String gameid = (String) json.get("game");
            try {
                Player player = event.getPlayer();
                Game game = BedwarsClient.getInstance().gameManager.getGame(Integer.parseInt(gameid), GameType.BEDWARS);
                if (!game.isGameStarted() || game.getSettings().getMax() != game.getPlayers().size() || game.getSettings().canJoin()) {
//                    Bukkit.getScheduler().scheduleSyncDelayedTask(BedwarsClient.getInstance(), new Runnable() {
//                        @Override
//                        public void run() {
//                            player.teleport(game.getLobby());
//                        }
//                    }, 4);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(BedwarsClient.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            //BedwarsClient.getInstance().gameManager.addPlayer(Utils.comingPlayers.get(player.getName()), player);
                            boolean result = player.performCommand("bw join " + game.getID());
                            if (!result) {
                                Utils.sendPlayerToServer(player);
                            }
//                            //setting cosmetics
//                            User cosmeticUser = sv.file14.procosmetics.api.API.getUser(event.getPlayer());
//                            cosmeticUser.unequipCosmetics(true);
//                            cosmeticUser.fullyUnequipCosmetics(true);
//                            HashMap<String, String> settingCosmetics = Utils.cosmeticMap.get(event.getPlayer().getName());
//                            String arrow = settingCosmetics.get("arrow");
//                            String death = settingCosmetics.get("death");
//                            try {
//                                if (!arrow.equalsIgnoreCase("null")) {
//                                    AbstractArrowEffectType.getType(arrow).equip(cosmeticUser, true);
//                                }
//                                if (!death.equalsIgnoreCase("null")) {
//                                    AbstractDeathEffectType.getType(death).equip(cosmeticUser, true);
//                                }
//                            } catch (Exception exception) {
//                                exception.printStackTrace();
//                            }

                        }
                    }, 4);



                } else {
                    event.getPlayer().sendMessage(Utils.colorify("&cIn match por/shoro shode bod..."));
                    //Utils.comingPlayers.remove(event.getPlayer().getName());
                    event.getPlayer().performCommand("spawn");
                    Utils.sendPlayerToServer(event.getPlayer());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (!event.getPlayer().hasPermission("client.admin")) {
                event.getPlayer().performCommand("spawn");
                Utils.sendPlayerToServer(event.getPlayer());
            }
        }
    }


    @EventHandler
    public void arenaLeavingEvent(GameLeaveEvent event) {
        try {
            Game game = BedwarsClient.getInstance().gameManager.getGame(event.getPlayer());
            BedwarsClient.getInstance().gameManager.removePlayer(event.getPlayer(), game);
            //Utils.comingPlayers.remove(event.getPlayer().getName());
        } catch (Exception e) {

        }

        try {
            TABAPI.getPlayer(event.getPlayer().getUniqueId()).removeTemporaryValue(EnumProperty.CUSTOMTABNAME);
            event.getPlayer().performCommand("spawn");
            Utils.sendPlayerToServer(event.getPlayer());
        } catch (Exception ex) {
        }

    }


    @EventHandler
    public void arenaFinishEvent(GameEndEvent event) {
        try {
            event.getPlayers().forEach(player -> {
                player.performCommand("spawn");
                Utils.sendPlayerToServer(player);
                TABAPI.getPlayer(player.getUniqueId()).removeTemporaryValue(EnumProperty.CUSTOMTABNAME);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @EventHandler
    public void arenaStartEvent(GameStartEvent event) {
        try {
            for (GameTeam island : event.getGame().getIslands()) {
                String teamColor = island.getColor().getTeamColor();

                //setting tab
                island.getTeam().forEach(player -> {
                    try {
                        TABAPI.getPlayer(player.getUniqueId()).setValueTemporarily(EnumProperty.CUSTOMTABNAME, teamColor + player.getName());
                    } catch (Exception e) {

                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @EventHandler
    public void playerLeaveListener(PlayerQuitEvent event) {
        try {
            Game game = BedwarsClient.getInstance().gameManager.getGame(event.getPlayer());
            BedwarsClient.getInstance().gameManager.removePlayer(event.getPlayer(), game);
            //Utils.comingPlayers.remove(event.getPlayer().getName());
        } catch (Exception e) {

        }
    }






}
