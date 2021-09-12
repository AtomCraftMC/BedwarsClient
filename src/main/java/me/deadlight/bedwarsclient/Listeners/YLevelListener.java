package me.deadlight.bedwarsclient.Listeners;
import me.deadlight.bedwarsclient.BedwarsClient;
import me.deadlight.bedwarsclient.Utils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import ro.Fr33styler.ClashWars.Games.Game;
import ro.Fr33styler.ClashWars.Handler.GameType;
import ro.Fr33styler.ClashWars.Handler.Tools.Serializer;

public class YLevelListener implements Listener {

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {

        Game game = BedwarsClient.getInstance().gameManager.getGame(event.getPlayer());
        if (game != null) {

            if (Utils.YLimitMap.containsKey(game.getID())) {
                //read from there and prevent
                int Y = Utils.YLimitMap.get(game.getID());
                if (event.getBlockPlaced().getLocation().getBlockY() > Y) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Utils.colorify("&cShoma nemitavanid bishtar az in bala beravid!"));
                }
            } else {
                String color = game.getIslands().get(0).getColor().getName().toUpperCase();
                Location teamSpawnLocation = Serializer.getDeserializedLocation(BedwarsClient.getInstance().bedwarsMain.getDatabase(GameType.BEDWARS).getString("Game." + game.getID() + ".Teams." + color + ".Bed"));
                Utils.YLimitMap.put(game.getID(), teamSpawnLocation.getBlockY() + 35);

                if (event.getBlockPlaced().getLocation().getBlockY() > teamSpawnLocation.getBlockY() + 35) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Utils.colorify("&cShoma nemitavanid bishtar az in bala beravid!"));
                }
            }
        }
    }


}
