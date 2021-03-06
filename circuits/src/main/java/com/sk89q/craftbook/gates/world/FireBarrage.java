package com.sk89q.craftbook.gates.world;

import org.bukkit.Server;
import org.bukkit.block.Sign;

import com.sk89q.craftbook.ic.AbstractICFactory;
import com.sk89q.craftbook.ic.ChipState;
import com.sk89q.craftbook.ic.IC;
import com.sk89q.craftbook.ic.RestrictedIC;

/**
 * @author Me4502
 */
public class FireBarrage extends FireShooter {

    public FireBarrage(Server server, Sign sign) {

        super(server, sign);
    }

    @Override
    public String getTitle() {

        return "Fire Barrage";
    }

    @Override
    public String getSignTitle() {

        return "FIRE BARRAGE";
    }

    @Override
    public void trigger(ChipState chip) {

        if (chip.getInput(0)) shootFire(5);
    }

    public static class Factory extends AbstractICFactory implements
    RestrictedIC {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(Sign sign) {

            return new FireBarrage(getServer(), sign);
        }

        @Override
        public String getDescription() {
            return "Shoots a barrage of fire.";
        }

        @Override
        public String[] getLineHelp() {
            String[] lines = new String[] {
                    "speed:spread",
                    "vertical gain"
            };
            return lines;
        }
    }
}