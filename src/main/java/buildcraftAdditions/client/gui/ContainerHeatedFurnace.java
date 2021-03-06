package buildcraftAdditions.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import buildcraftAdditions.tileEntities.TileHeatedFurnace;

/**
 * Copyright (c) 2014, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Buildcraft Additions is distributed under the terms of GNU GPL v3.0
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */
public class ContainerHeatedFurnace extends ContainerBase {
	public TileHeatedFurnace furnace;

	public ContainerHeatedFurnace(IInventory inventory, TileHeatedFurnace furnace) {
		super();
		furnace.sync = true;
		this.furnace = furnace;

		this.addSlotToContainer(new Slot(furnace, 0, 56, 34));
		this.addSlotToContainer(new SlotOutput(furnace, 1, 116, 34));

		for (int inventoryRowIndex = 0; inventoryRowIndex < 3; ++inventoryRowIndex) {
			for (int inventoryColumnIndex = 0; inventoryColumnIndex < 9; ++inventoryColumnIndex) {
				this.addSlotToContainer(new Slot(inventory, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 84 + inventoryRowIndex * 18));
			}
		}
		for (int hotbbarIndex = 0; hotbbarIndex < 9; ++hotbbarIndex) {
			this.addSlotToContainer(new Slot(inventory, hotbbarIndex, 8 + hotbbarIndex * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		furnace.sync = false;
	}
}
