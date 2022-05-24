package me.deadlight.bedwarsclient.Messanger;
import me.deadlight.bedwarsclient.BedwarsClient;
import me.deadlight.bedwarsclient.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ro.Fr33styler.ClashWars.Handler.Party.Party;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        if (Utils.incomingParties.containsKey(e.getPlayer().getName())) {

            Player leader = e.getPlayer();

            ClientParty clientParty = Utils.incomingParties.get(leader.getName());
            Party party = new Party(leader, clientParty.members.size());
            Bukkit.getScheduler().runTaskLaterAsynchronously(BedwarsClient.getInstance(), new Runnable() {
                @Override
                public void run() {

                    for (String member : clientParty.members) {

                        Player p = Bukkit.getPlayer(member);
                        if (p != null) {
                            party.getMembers().add(p);
                        } else {
                            leader.sendMessage(Utils.colorify("&c" + member + " ghader be ozviat dar party shoma nist!"));
                        }
                    }
                }
            }, 20 * 2);

        }

    }


}
