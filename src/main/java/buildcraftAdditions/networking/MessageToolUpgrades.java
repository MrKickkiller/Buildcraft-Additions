package buildcraftAdditions.networking;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.server.FMLServerHandler;

import buildcraftAdditions.items.Tools.ItemKineticTool;


import io.netty.buffer.ByteBuf;

/**
 * Copyright (c) 2014, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Buildcraft Additions is distributed under the terms of GNU GPL v3.0
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */
public class MessageToolUpgrades implements IMessage, IMessageHandler<MessageToolUpgrades, IMessage> {
	public boolean chainsaw, digger, drill, hoe, chainsawEnabled, diggerEnabled, drillEnabled, hoeEnabled;
	public long identificatie;

	public MessageToolUpgrades() {
	}

	public MessageToolUpgrades(ItemKineticTool tool) {
		this.chainsaw = tool.chainsaw;
		this.digger = tool.digger;
		this.drill = tool.drill;
		this.hoe = tool.hoe;

	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.chainsaw = buffer.readBoolean();
		this.digger = buffer.readBoolean();
		this.drill = buffer.readBoolean();
		this.hoe = buffer.readBoolean();
		this.chainsawEnabled = buffer.readBoolean();
		this.diggerEnabled = buffer.readBoolean();
		this.drillEnabled = buffer.readBoolean();
		this.hoeEnabled = buffer.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeBoolean(chainsaw);
		buffer.writeBoolean(digger);
		buffer.writeBoolean(drill);
		buffer.writeBoolean(hoe);
	}

	@Override
	public IMessage onMessage(MessageToolUpgrades message, MessageContext ctx) {
		String player = FMLClientHandler.instance().getClientPlayerEntity().getDisplayName();
		IInventory inventory = FMLServerHandler.instance().getServer().getEntityWorld().getPlayerEntityByName(player).inventory;
		ItemStack stack = null;
		ItemKineticTool tool = null;
		for (int teller = 0; teller < inventory.getSizeInventory(); teller++) {
			stack = inventory.getStackInSlot(teller);
			if (stack != null && stack.getItem() instanceof ItemKineticTool)
				if (stack.stackTagCompound.getLong("identificatie") == message.identificatie) {
					tool = (ItemKineticTool) stack.getItem();
					break;
				}
		}
		if (tool != null) {
			stack.stackTagCompound.setBoolean("chainsaw", message.chainsaw);
			stack.stackTagCompound.setBoolean("digger", message.digger);
			stack.stackTagCompound.setBoolean("drill", message.drill);
			stack.stackTagCompound.setBoolean("hoe", message.hoe);
			tool.readUpgrades(stack);
		}
		return null;
	}
}
