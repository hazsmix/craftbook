package com.sk89q.craftbook.gates.world;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import com.sk89q.craftbook.ic.AbstractIC;
import com.sk89q.craftbook.ic.AbstractICFactory;
import com.sk89q.craftbook.ic.ChipState;
import com.sk89q.craftbook.ic.IC;
import com.sk89q.craftbook.util.SignUtil;
import com.sk89q.worldedit.blocks.ItemID;

public class BonemealTerraformer extends AbstractIC {

    int radius;

    public BonemealTerraformer(Server server, Sign block) {
        super(server, block);
        load();
    }

    private void load() {
        try {
            radius = Integer.parseInt(getSign().getLine(3));
            if(radius > 15) {
                radius = 15;
                getSign().setLine(3, "15");
                getSign().update();
            }
        }
        catch(NumberFormatException e) {
            radius = 10;
        }
    }

    @Override
    public String getTitle() {
        return "Bonemeal Terraformer";
    }

    @Override
    public String getSignTitle() {
        return "TERRAFORMER";
    }

    @Override
    public void trigger(ChipState chip) {
        if(chip.getInput(0))
            terraform();
    }

    public void terraform() {

        Random random = new Random();
        for(int x = -radius + 1; x < radius; x++)
            for(int y = -radius + 1; y < radius; y++)
                for(int z = -radius + 1; z < radius; z++) {
                    if(random.nextInt(30) == 0) {
                        int rx = getSign().getLocation().getBlockX() - x;
                        int ry = getSign().getLocation().getBlockY() - y;
                        int rz = getSign().getLocation().getBlockZ() - z;
                        Block b = getSign().getWorld().getBlockAt(rx,ry,rz);
                        if(b.getType() == Material.DIRT) {
                            if(consumeBonemeal())
                                b.setType(Material.GRASS);
                            return;
                        }
                        if(b.getType() == Material.GRASS && b.getRelative(0,1,0).getType() == Material.AIR && random.nextInt(15) == 0) {
                            if(consumeBonemeal()) {
                                int t = random.nextInt(6);
                                if(t == 0) {
                                    b.getRelative(0, 1, 0).setType(Material.LONG_GRASS);
                                    b.getRelative(0, 1, 0).setData((byte)1);
                                }
                                else if(t == 1)
                                    b.getRelative(0, 1, 0).setType(Material.YELLOW_FLOWER);
                                else if(t == 2)
                                    b.getRelative(0, 1, 0).setType(Material.RED_ROSE);
                                else {
                                    b.getRelative(0, 1, 0).setType(Material.LONG_GRASS);
                                    b.getRelative(0, 1, 0).setData((byte)1);
                                }
                            }
                            return;
                        }
                    }
                }
    }

    public boolean consumeBonemeal() {
        Block chest = SignUtil.getBackBlock(getSign().getBlock()).getRelative(0,1,0);
        if(chest.getType() == Material.CHEST) {
            Chest c = (Chest)chest.getState();
            HashMap<Integer, ItemStack> over = c.getInventory().removeItem(new ItemStack(ItemID.INK_SACK, 1, (short) 15));
            if(over.size() == 0)
                return true;
        }

        return false;
    }

    public static class Factory extends AbstractICFactory {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(Sign sign) {

            return new BonemealTerraformer(getServer(), sign);
        }

        @Override
        public String getDescription() {
            return "Terraforms an area using bonemeal.";
        }

        @Override
        public String[] getLineHelp() {
            String[] lines = new String[] {
                    "radius",
                    null
            };
            return lines;
        }
    }
}