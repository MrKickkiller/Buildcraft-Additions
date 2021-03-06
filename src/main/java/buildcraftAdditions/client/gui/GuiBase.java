package buildcraftAdditions.client.gui;

import buildcraftAdditions.client.gui.widgets.WidgetBase;
import eureka.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2014, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Buildcraft Additions is distributed under the terms of GNU GPL v3.0
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */
public abstract class GuiBase extends GuiContainer {

	public static final int TEXT_COLOR = 0x404040;
	public static final ResourceLocation MC_BLOCK_SHEET = TextureMap.locationBlocksTexture;
	public static final ResourceLocation MC_ITEM_SHEET = TextureMap.locationItemsTexture;
	public static final ResourceLocation PLAYER_INV_TEXTURE = new ResourceLocation("bcadditions:textures/gui/guiPlayerInv.png");

	public final ResourceLocation texture;
	public boolean drawPlayerInv = true;
	public List<WidgetBase> widgets = new ArrayList<WidgetBase>();
	public int xSizePlayerInv = 175;
	public int ySizePlayerInv = 99;

	public GuiBase(Container container) {
		super(container);
		this.texture = texture();
		this.xSize = getXSize();
		this.ySize = getYSize();
	}

	public GuiBase setDrawPlayerInv(boolean draw) {
		this.drawPlayerInv = draw;
		return this;
	}

	public abstract ResourceLocation texture();

	public abstract int getXSize();

	public abstract int getYSize();

	public abstract String getInventoryName();

	public abstract void initialize();

	public TextureManager textureManager() {
		return Minecraft.getMinecraft().getTextureManager();
	}

	public SoundHandler soundHandler() {
		return Minecraft.getMinecraft().getSoundHandler();
	}

	public void bindTexture(ResourceLocation texture) {
		textureManager().bindTexture(texture);
	}

	public void drawString(String text, int x, int y) {
		fontRendererObj.drawString(text, x, y, TEXT_COLOR);
	}

	public void widgetActionPerformed(WidgetBase widget) {

	}

	public void addWidget(WidgetBase widget) {
		widgets.add(widget);
	}

	@Override
	public void initGui() {
		super.initGui();
		if (drawPlayerInv)
			this.guiTop = (this.height - (this.ySize + ySizePlayerInv)) / 2;
		initialize();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindTexture(texture());
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if (drawPlayerInv) {
			bindTexture(PLAYER_INV_TEXTURE);
			drawTexturedModalRect(guiLeft, guiTop + ySize, 0, 0, xSizePlayerInv, ySizePlayerInv);
		}

		for (WidgetBase widget : widgets)
			widget.render();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		drawString(StatCollector.translateToLocal("container.inventory"), 5, ySize + 6);
		drawString(Utils.localize(String.format("gui.%s.name", getInventoryName())), 5, 6);
	}

	@Override
	public void setWorldAndResolution(Minecraft minecraft, int width, int height) {
		widgets.clear();
		super.setWorldAndResolution(minecraft, width, height);
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		for (WidgetBase widget : widgets) {
			if (widget.getBounds().contains(x, y) && widget.enabled)
				widget.onWidgetClicked(x, y, button);
		}
	}

	public void redraw() {
		widgets.clear();
		buttonList.clear();
		initialize();
	}
}
