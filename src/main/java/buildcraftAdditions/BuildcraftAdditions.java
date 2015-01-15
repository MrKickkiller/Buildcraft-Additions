package buildcraftAdditions;

import com.google.common.collect.ImmutableList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

import buildcraftAdditions.ModIntegration.ModIntegration;
import buildcraftAdditions.api.item.BCAItemManager;
import buildcraftAdditions.api.item.dust.IDust;
import buildcraftAdditions.api.recipe.BCARecipeManager;
import buildcraftAdditions.client.gui.gui.GuiHandler;
import buildcraftAdditions.config.ConfigurationHandler;
import buildcraftAdditions.core.EventListener;
import buildcraftAdditions.core.Logger;
import buildcraftAdditions.creative.TabBCAdditions;
import buildcraftAdditions.creative.TabCanisters;
import buildcraftAdditions.creative.TabDusts;
import buildcraftAdditions.items.dust.DustManager;
import buildcraftAdditions.items.dust.DustTypes;
import buildcraftAdditions.networking.PacketHandler;
import buildcraftAdditions.proxy.CommonProxy;
import buildcraftAdditions.recipe.duster.DusterRecipeManager;
import buildcraftAdditions.reference.ItemsAndBlocks;
import buildcraftAdditions.reference.Variables;
import buildcraftAdditions.utils.SpecialListMananger;

/**
 * Copyright (c) 2014, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Buildcraft Additions is distributed under the terms of GNU GPL v3.0
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */
@Mod(modid = Variables.MOD.ID, name = Variables.MOD.NAME, version = "@MODVERSION@", guiFactory = "buildcraftAdditions.config.GuiFactory", dependencies = "after:BuildCraft|Energy;required-after:eureka;required-after:Forge@[10.13.2.1230,)", acceptedMinecraftVersions = "1.7.10")
public class BuildcraftAdditions {

	@Mod.Instance(Variables.MOD.ID)
	public static BuildcraftAdditions instance;

	@SidedProxy(clientSide = "buildcraftAdditions.proxy.ClientProxy", serverSide = "buildcraftAdditions.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static CreativeTabs bcadditions = new TabBCAdditions();
	public static CreativeTabs bcaCannisters = new TabCanisters();
	public static CreativeTabs bcaDusts = new TabDusts();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Logger.initiallize();
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
		PacketHandler.init();
		ItemsAndBlocks.init();
		SpecialListMananger.init();

		BCARecipeManager.duster = new DusterRecipeManager();
		BCAItemManager.dusts = new DustManager();
	}

	@Mod.EventHandler
	public void doneLoading(FMLLoadCompleteEvent event) {
		ItemsAndBlocks.addRecipes();

		int meta = 1;
		BCAItemManager.dusts.addDust(meta++, "Iron", 0xD2CEC9, DustTypes.METAL_DUST);
		BCAItemManager.dusts.addDust(meta++, "Gold", 0xF8DF17, DustTypes.METAL_DUST);
		BCAItemManager.dusts.addDust(meta++, "Diamond", 0x13ECFC, DustTypes.GEM_DUST);

		BCARecipeManager.duster.addRecipe("oreRedstone", new ItemStack(Items.redstone, 6));
		BCARecipeManager.duster.addRecipe("oreCoal", new ItemStack(Items.coal, 2));
		BCARecipeManager.duster.addRecipe("oreLapis", new ItemStack(Items.dye, 6, 4));
		BCARecipeManager.duster.addRecipe("oreQuartz", new ItemStack(Items.quartz, 2));
		BCARecipeManager.duster.addRecipe("stone", new ItemStack(Blocks.gravel));
		BCARecipeManager.duster.addRecipe("cobblestone", new ItemStack(Blocks.sand));
		BCARecipeManager.duster.addRecipe("oreDiamond", new ItemStack(Items.diamond, 2));
		BCARecipeManager.duster.addRecipe("oreEmerald", new ItemStack(Items.emerald, 2));
		BCARecipeManager.duster.addRecipe(new ItemStack(Items.blaze_rod), new ItemStack(Items.blaze_powder, 4));

		ModIntegration.integrate();

		for (IDust dust : BCAItemManager.dusts.getDusts()) {
			if (dust != null) {
				dust.getDustType().register(dust.getMeta(), dust.getName(), dust.getDustStack());
			}
		}

		processIMC(FMLInterModComms.fetchRuntimeMessages(this));
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		FMLCommonHandler.instance().bus().register(new EventListener.FML());
		MinecraftForge.EVENT_BUS.register(new EventListener.Forge());
	}

	public void onIMC(FMLInterModComms.IMCEvent event) {
		processIMC(event.getMessages());
	}

	private void processIMC(ImmutableList<FMLInterModComms.IMCMessage> messages) {
		for (FMLInterModComms.IMCMessage message : messages) {
			if ("addDusting".equalsIgnoreCase(message.key) && message.isNBTMessage() && message.getNBTValue().hasKey("output", Constants.NBT.TAG_COMPOUND)) {
				NBTTagCompound nbt = message.getNBTValue().getCompoundTag("output");
				if (nbt != null) {
					ItemStack output = ItemStack.loadItemStackFromNBT(nbt);
					if (output != null) {
						if (message.getNBTValue().hasKey("input", Constants.NBT.TAG_COMPOUND)) {
							nbt = message.getNBTValue().getCompoundTag("input");
							if (nbt != null) {
								ItemStack input = ItemStack.loadItemStackFromNBT(nbt);
								if (input != null) {
									BCARecipeManager.duster.addRecipe(input, output);
									continue;
								}
							}
						}
						if (message.getNBTValue().hasKey("oreInput", Constants.NBT.TAG_STRING)) {
							String oreInput = nbt.getString("oreInput");
							if (!StringUtils.isNullOrEmpty(oreInput)) {
								BCARecipeManager.duster.addRecipe(oreInput, output);
								continue;
							}
						}
					}
				}
			}
			Logger.error("The mod '" + message.getSender() + "' send an invalid IMC message (" + message.key + ") ! Skipping.");
		}
	}
}
