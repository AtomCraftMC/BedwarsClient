package me.deadlight.bedwarsclient;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import ro.Fr33styler.ClashWars.Games.Game;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.*;


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


        List<ServerObject> servers = new ArrayList<>();
        Jedis j = null;
        try {
            j = BedwarsClient.pool.getResource();
            j.auth("piazcraftmc");
            if (j.get("lobbylist") == null) {
                return "auth";
            }
            List<String> lobbyServers = Arrays.asList(j.get("lobbylist").split(":"));
            for (String server : lobbyServers) {
                if (j.get("count-" + server) == null) {
                    continue;
                }
                int count = Integer.parseInt(j.get("count-" + server));
                ServerObject object = new ServerObject(server, count);
                servers.add(object);
            }
        } finally {
            if (j != null)  {
                j.close();
            }
        }


        Collections.sort(servers);

        if (servers.size() == 0) {
            return "auth";
        }

        return servers.get(0).name;




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
