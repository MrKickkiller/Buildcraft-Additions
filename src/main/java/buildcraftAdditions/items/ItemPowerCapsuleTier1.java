package buildcraftAdditions.items;

/**
 * Copyright (c) 2014, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Buildcraft Additions is distributed under the terms of GNU GPL v3.0
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import buildcraftAdditions.BuildcraftAdditions;

public class ItemPowerCapsuleTier1 extends BatteryBase {
    IIcon icon;
	
	public ItemPowerCapsuleTier1(){
		this.maxStackSize = 1;
		setCreativeTab(BuildcraftAdditions.bcadditions);
		this.setUnlocalizedName("PowerCapsuleTier1");
        this.setNoRepair();
	}
	
	@Override
	public int getCapacity(){
        return 60000;
	}

    @Override
    public String getType() {
        return "(Tier 1): ";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        icon = par1IconRegister.registerIcon("bcadditions:T1_battery");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return icon;
    }

}
