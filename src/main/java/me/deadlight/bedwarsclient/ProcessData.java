package me.deadlight.bedwarsclient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ro.Fr33styler.ClashWars.Games.Game;
import ro.Fr33styler.ClashWars.Handler.GameType;
import java.util.HashMap;

public class ProcessData {
    static JSONParser parser = new JSONParser();
    public static void ProcessIncomingData(String data) throws ParseException {

        if (data.equalsIgnoreCase("name-give-me-plz")) {
            //naming
            BedwarsClient.getInstance().Sclient.sendMessage("secret-esm-yo!" + BedwarsClient.getInstance().theServerName);
            return;
        }
        if (data.equalsIgnoreCase(("plz-stop-urself"))) {
            BedwarsClient.getInstance().Sclient.stopConnection();
        }
        JSONObject json = (JSONObject) parser.parse(data);
        //comingplayer, rejoinplayer

        String type = (String) json.get("type");



        if (type.equalsIgnoreCase("comingplayer")) {
            //add in queue
            String playerName = (String)json.get("player");
            String arrow = (String) json.get("arrow");
            String death = (String) json.get("death");
            if (Utils.comingPlayers.containsKey(playerName)) {
                return;
            }
            String gameID = (String) json.get("game");


            Game game = BedwarsClient.getInstance().gameManager.getGame(Integer.parseInt(gameID), GameType.BEDWARS);
            Utils.comingPlayers.put(playerName, game);
            HashMap<String, String> cosmeticsMap = new HashMap<>();
            cosmeticsMap.put("arrow", arrow);
            cosmeticsMap.put("death", death);
            Utils.cosmeticMap.put(playerName, cosmeticsMap);


        } else if (type.equalsIgnoreCase("rejoinplayer")) {

        }
    }

}
