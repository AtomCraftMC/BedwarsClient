package me.deadlight.bedwarsclient.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TntModifier implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTntPrime(BlockPlaceEvent event) {
        Location blockLoc = event.getBlockPlaced().getLocation();
        if (event.getBlockPlaced().getType() == Material.TNT) {
            event.setCancelled(true);
            spawnModifiedPrimeTNT(blockLoc);
        }
    }


    public void spawnModifiedPrimeTNT(Location location) {
        Entity entity = location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        TNTPrimed primedTnt = (TNTPrimed) entity;
        primedTnt.setFuseTicks(20 * 3);

    }


}
