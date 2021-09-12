package me.deadlight.bedwarsclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TopPlayerDataObject{
    public String playerName;
    public int kills;
    public int wins;
    //player!kills!wins@
    public TopPlayerDataObject(String playerName, int kills, int wins) {
        this.playerName = playerName;
        this.kills = kills;
        this.wins = wins;
    }
    public void setKills(int count) {
        this.kills = count;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }

    public static List<TopPlayerDataObject> translateStringListToObjects(String list) {
        List<String> thelist = Arrays.asList(list.split("@"));
        List<TopPlayerDataObject> finalList = new ArrayList<>();

        for (String stringObject : thelist) {
            String[] dataFromObject = stringObject.split("!");
            TopPlayerDataObject newObj = new TopPlayerDataObject(dataFromObject[0], Integer.parseInt(dataFromObject[1]), Integer.parseInt(dataFromObject[2]));
            finalList.add(newObj);
        }

        return finalList;
    }


    public static String translateObjectsToString(List<TopPlayerDataObject> list) {
        boolean first = false;
        StringBuilder finalString = new StringBuilder();
        for (TopPlayerDataObject topPlayerDataObject : list) {
            if (!first) {
                first = true;
                finalString = new StringBuilder(topPlayerDataObject.playerName + "!" + topPlayerDataObject.kills + "!" + topPlayerDataObject.wins);
            } else {

                finalString.append("@").append(topPlayerDataObject.playerName).append("!").append(topPlayerDataObject.kills).append("!").append(topPlayerDataObject.wins);

            }

        }
        return finalString.toString();

    }


}
