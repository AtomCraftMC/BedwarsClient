package me.deadlight.bedwarsclient.Messanger;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.deadlight.bedwarsclient.Utils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ro.Fr33styler.ClashWars.Handler.Party.Party;

import java.util.Arrays;
import java.util.List;

public class IncomingParties implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        if (channel.equalsIgnoreCase("BedwarsSystem")) {

            ClientParty clientParty = translateMessage(message);
            Utils.incomingParties.put(clientParty.leader, clientParty);
        }

    }


    public ClientParty translateMessage(byte[] message) {

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        String msg = new String(message);
        String[] args = msg.split(":");
        //convert args to List<String>
        List<String> argsList = Arrays.asList(args);

        //leader@member:member:member
        String leader = args[0].split("@")[0];
        String anotherMember = args[0].split("@")[1];
        //clear arg 0 from args
        args[0] = anotherMember;
        return new ClientParty(leader, argsList);


    }





}
