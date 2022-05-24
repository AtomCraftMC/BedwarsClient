package me.deadlight.bedwarsclient.Messanger;

import java.util.ArrayList;
import java.util.List;

public class ClientParty {
    public String leader;
    public List<String> members;

    public ClientParty(String leader, List<String> members) {

        this.leader = leader;
        this.members = members;

    }

}
