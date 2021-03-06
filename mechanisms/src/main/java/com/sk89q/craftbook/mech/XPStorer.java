package com.sk89q.craftbook.mech;

import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.craftbook.AbstractMechanic;
import com.sk89q.craftbook.AbstractMechanicFactory;
import com.sk89q.craftbook.bukkit.MechanismsPlugin;
import com.sk89q.worldedit.BlockWorldVector;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.blocks.ItemID;
import com.sk89q.worldedit.bukkit.BukkitUtil;

public class XPStorer extends AbstractMechanic {

    public static class Factory extends AbstractMechanicFactory<XPStorer> {

        MechanismsPlugin plugin;

        public Factory(MechanismsPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public XPStorer detect(BlockWorldVector pt) {

            int type = BukkitUtil.toWorld(pt).getBlockTypeIdAt(BukkitUtil.toLocation(pt));

            if (type == BlockID.MOB_SPAWNER) return new XPStorer(pt, plugin);

            return null;
        }
    }

    MechanismsPlugin plugin;

    /**
     * Construct the mechanic for a location.
     *
     * @param pt
     */
    private XPStorer(BlockWorldVector pt, MechanismsPlugin plugin) {

        super();
        this.plugin = plugin;
    }

    @Override
    public void onRightClick(PlayerInteractEvent event) {

        if(!plugin.wrap(event.getPlayer()).hasPermission("craftbook.mech.xpstore.use")) return;
        Inventory store = Bukkit.createInventory(event.getPlayer(), 9, "Experience Points");
        ItemStack XP = new ItemStack(ItemID.BOTTLE_O_ENCHANTING, 0);
        while(event.getPlayer().getTotalExperience() > 15) {
            event.getPlayer().setTotalExperience(event.getPlayer().getTotalExperience() - 15);
            XP.setAmount(XP.getAmount() + 1);
        }
        if(!(XP.getAmount() > 0))
            return;
        event.getPlayer().setLevel(0);
        event.getPlayer().openInventory(store);
        store.setItem(0, XP);

        event.setCancelled(true);
    }

    @Override
    public void unload() {

    }

    @Override
    public void unloadWithEvent(ChunkUnloadEvent event) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {

    }
}