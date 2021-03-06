package com.sk89q.craftbook.gates.world;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

import com.sk89q.craftbook.ic.AbstractIC;
import com.sk89q.craftbook.ic.AbstractICFactory;
import com.sk89q.craftbook.ic.ChipState;
import com.sk89q.craftbook.ic.IC;
import com.sk89q.craftbook.ic.RestrictedIC;
import com.sk89q.craftbook.util.BlockUtil;
import com.sk89q.craftbook.util.SignUtil;

public class TimedExplosion extends AbstractIC {

    int ticks;
    float yield;
    boolean flamey;

    public TimedExplosion(Server server, Sign block) {
        super(server, block);
        load();
    }

    public void load() {
        try {
            ticks = Integer.parseInt(getSign().getLine(2));
        }
        catch(NumberFormatException e) {
            ticks = -1;
        }

        try {
            yield = Float.parseFloat(getSign().getLine(3).replace("!", ""));
        }
        catch(NumberFormatException e) {
            yield = -1;
        }

        flamey = getSign().getLine(3).endsWith("!");
    }

    @Override
    public String getTitle() {
        return "Timed Explosive";
    }

    @Override
    public String getSignTitle() {
        return "TIME BOMB";
    }

    @Override
    public void trigger(ChipState chip) {
        if(chip.getInput(0)) {
            Block infront = getSign().getBlock().getRelative(SignUtil.getBack(getSign().getBlock()).getOppositeFace());
            TNTPrimed tnt = (TNTPrimed) getSign().getWorld().spawnEntity(BlockUtil.getBlockCentre(infront), EntityType.PRIMED_TNT);
            tnt.setIsIncendiary(flamey);
            if(ticks > 0)
                tnt.setFuseTicks(ticks);
            if(yield > 0)
                tnt.setYield(yield);
        }
    }

    public static class Factory extends AbstractICFactory implements
    RestrictedIC {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(Sign sign) {

            return new TimedExplosion(getServer(), sign);
        }

        @Override
        public String getDescription() {
            return "Spawn tnt with custom fuse and yield.";
        }

        @Override
        public String[] getLineHelp() {
            String[] lines = new String[] {
                    "time in ticks",
                    "explosion radius (ending with ! makes fire)"
            };
            return lines;
        }
    }
}
