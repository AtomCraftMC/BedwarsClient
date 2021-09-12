package me.deadlight.bedwarsclient;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import ro.Fr33styler.ClashWars.Games.Game;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.List;


public class Utils {

    public static HashMap<Integer, HashMap<String, TopPlayerDataObject>> arenasInfo = new HashMap<>();

    public static HashMap<String, HashMap<String, String>> cosmeticMap = new HashMap<>();


    public static HashMap<Integer, Integer> YLimitMap = new HashMap<>();

    public static String colorify(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String prefix = "&7[&cBedwarsClient&7]&r";

    public static HashMap<String, Game> comingPlayers = new HashMap<>();

    public static String listToString(List<Player> uuidList) {

        String finalString = "";
        boolean first = true;
        for (Player player : uuidList) {
            if (first) {
                finalString = finalString + player.getName();
                first = false;
            } else {
                finalString = finalString + "@" + player.getName();
            }

        }
        return finalString;

    }

    public static String calculateTheRightBedwarsLobby() {
        String result1 = null;
        String result2 = null;
        Jedis j = null;
        try {
            j = BedwarsClient.pool.getResource();
            j.auth("piazcraftmc");
            result1 = j.get("count-blobby1");
            result2 = j.get("count-blobby2");
        } finally {
            if (j != null)  {
                j.close();
            }
        }


        if (result1 != null || result2 != null) {

            if (result1 == null) {
                return "blobby2";
            }
            if (result2 == null) {
                return "blobby1";
            }
            int count1 = Integer.parseInt(result1);
            int count2 = Integer.parseInt(result2);
            if (count1 == count2) {
                return "blobby1";
            }

            if (count1 > count2) {
                return "blobby2";
            } else {
                return "blobby1";
            }

        } else {
            return "auth";
        }

    }

    public static void sendPlayerToServer(Player player) {
        try {
            String server = calculateTheRightBedwarsLobby();
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(BedwarsClient.getInstance(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        }
        catch (Exception e) {
            player.sendMessage(ChatColor.RED+"Error when trying to connect to server ");
        }
    }



}
