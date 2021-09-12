package me.deadlight.bedwarsclient;

import org.bukkit.Bukkit;
import ro.Fr33styler.ClashWars.Games.Game;
import ro.Fr33styler.ClashWars.Games.GameTeam;

import java.util.List;

public class MatchChecks {

    public MatchChecks() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(BedwarsClient.getInstance(), new java.lang.Runnable() {
            @Override
            public void run() {

                List<Game> games = BedwarsClient.getInstance().bedwarsMain.getManager().getGames();
                for (Game game : games) {

                    if (game.isGameStarted()) {

                        for (GameTeam island : game.getIslands()) {
                            if (island.getTeam().size() == 0) {

                            }
                        }




                    }


                }

            }
        }, 0, 20 * 10);



    }

}
