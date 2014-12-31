package buildcraftAdditions.client.gui.gui;

/**
 * Copyright (c) 2014, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Buildcraft Additions is distributed under the terms of GNU GPL v3.0
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

import buildcraftAdditions.client.gui.containers.ContainerBasicCoil;
import buildcraftAdditions.client.gui.containers.ContainerChargingStation;
import buildcraftAdditions.client.gui.containers.ContainerCoolingTower;
import buildcraftAdditions.client.gui.containers.ContainerFluidicCompressor;
import buildcraftAdditions.client.gui.containers.ContainerHeatedFurnace;
import buildcraftAdditions.client.gui.containers.ContainerKEB;
import buildcraftAdditions.client.gui.containers.ContainerKineticTool;
import buildcraftAdditions.client.gui.containers.ContainerMachineConfigurator;
import buildcraftAdditions.client.gui.containers.ContainerRefinery;
import buildcraftAdditions.items.Tools.ItemKineticTool;
import buildcraftAdditions.items.Tools.ItemPoweredBase;
import buildcraftAdditions.reference.Variables;
import buildcraftAdditions.tileEntities.Bases.TileKineticEnergyBufferBase;
import buildcraftAdditions.tileEntities.TileBasicCoil;
import buildcraftAdditions.tileEntities.TileChargingStation;
import buildcraftAdditions.tileEntities.TileCoolingTower;
import buildcraftAdditions.tileEntities.TileFluidicCompressor;
import buildcraftAdditions.tileEntities.TileHeatedFurnace;
import buildcraftAdditions.tileEntities.TileRefinery;
import buildcraftAdditions.utils.IConfigurableOutput;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {


		TileEntity tile = world.getTileEntity(x, y, z);

		switch (ID) {
			case Variables.Gui.FLUIDIC_COMPRESSOR:
				if (tile instanceof TileFluidicCompressor)
					return new GuiFluidicCompressor(player.inventory, (TileFluidicCompressor) tile);
			case Variables.Gui.CHARGING_STATION:
				if (tile instanceof TileChargingStation)
					return new GuiChargingStation(player.inventory, (TileChargingStation) tile);
			case Variables.Gui.KINETIC_TOOL:
				if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemKineticTool) {
					ItemKineticTool tool = (ItemKineticTool) player.getCurrentEquippedItem().getItem();
					return new GuiKineticTool(player.inventory, tool, ItemPoweredBase.getInventory(player), player.getCurrentEquippedItem(), player);
				}
			case Variables.Gui.HEATED_FURNACE:
				if (tile instanceof TileHeatedFurnace)
					return new GuiHeatedFurnace(player.inventory, (TileHeatedFurnace) tile);
			case Variables.Gui.BASIC_COIL:
				if (tile instanceof TileBasicCoil)
					return new GuiBasicCoil(player.inventory, (TileBasicCoil) tile);
			case Variables.Gui.KEB:
				if (tile instanceof TileKineticEnergyBufferBase)
					return new GuiKEB((TileKineticEnergyBufferBase) tile, player);
			case Variables.Gui.MACHINE_CONFIGURATOR:
				if (tile instanceof IConfigurableOutput)
					return new GuiMachineConfigurator((IConfigurableOutput) tile);
			case Variables.Gui.REFINERY:
				if (tile instanceof TileRefinery)
					return new GuiRefinery((TileRefinery) tile);
			case Variables.Gui.COOLING_TOWER:
				if (tile instanceof TileCoolingTower)
					return new GuiCoolingTower((TileCoolingTower) tile);
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
									  int x, int y, int z) {

		TileEntity tile = world.getTileEntity(x, y, z);

		switch (ID) {
			case Variables.Gui.FLUIDIC_COMPRESSOR:
				if (tile instanceof TileFluidicCompressor)
					return new ContainerFluidicCompressor(player.inventory, (TileFluidicCompressor) tile);
			case Variables.Gui.CHARGING_STATION:
				if (tile instanceof TileChargingStation)
					return new ContainerChargingStation(player.inventory, (TileChargingStation) tile);
			case Variables.Gui.KINETIC_TOOL:
				if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemKineticTool) {
					ItemKineticTool tool = (ItemKineticTool) player.getCurrentEquippedItem().getItem();
					return new ContainerKineticTool(player.inventory, tool, ItemPoweredBase.getInventory(player), player.getCurrentEquippedItem(), player);
				}
			case Variables.Gui.HEATED_FURNACE:
				if (tile instanceof TileHeatedFurnace)
					return new ContainerHeatedFurnace(player.inventory, (TileHeatedFurnace) tile);
			case Variables.Gui.BASIC_COIL:
				if (tile instanceof TileBasicCoil)
					return new ContainerBasicCoil(player.inventory, (TileBasicCoil) tile);
			case Variables.Gui.KEB:
				if (tile instanceof TileKineticEnergyBufferBase)
					return new ContainerKEB((TileKineticEnergyBufferBase) tile, player);
			case Variables.Gui.MACHINE_CONFIGURATOR:
				return new ContainerMachineConfigurator();
			case Variables.Gui.REFINERY:
				return new ContainerRefinery();
			case Variables.Gui.COOLING_TOWER:
				return new ContainerCoolingTower();
		}
		return null;
	}

}
