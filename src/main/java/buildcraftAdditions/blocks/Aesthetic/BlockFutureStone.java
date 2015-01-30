package buildcraftAdditions.blocks.Aesthetic;

import net.minecraft.block.material.Material;

/**
 * Copyright (c) 2014, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Buildcraft Additions is distributed under the terms of GNU GPL v3.0
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */
public class BlockFutureStone extends BlockAesthetic {
    public BlockFutureStone() {
        super(Material.rock);
        this.setBlockName("Future Stone");
        //Setting the texture here and in ItemsAndBlocks.java, still no result.
        this.setBlockTextureName("bcadditions:futureStone.png");
    }
}
