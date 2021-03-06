package com.projectreddog.tsrts.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.projectreddog.tsrts.TSRTS;
import com.projectreddog.tsrts.client.gui.widget.ResearchButton;
import com.projectreddog.tsrts.containers.ResearchContainer;
import com.projectreddog.tsrts.init.ModNetwork;
import com.projectreddog.tsrts.init.ModResearch;
import com.projectreddog.tsrts.network.ResearchButtonClickPacketToServer;
import com.projectreddog.tsrts.reference.Reference;
import com.projectreddog.tsrts.utilities.TeamEnum;
import com.projectreddog.tsrts.utilities.data.Research;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ResearchScreen extends ContainerScreen<ResearchContainer> {

	PlayerEntity player;
	private ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/barracks_gui.png");
	private ResourceLocation TEXTURE_WIDGETS = new ResourceLocation(Reference.MODID, "textures/gui/guiwidgets.png");

	int ytextOffset = 20;
	int xtextOffset = 5;
	int xTextWidth = 25;
	String teamName = "";

	double mouseClickStartX = 0;
	double mouseClickStartY = 0;
	double currentScrollAmountX = -20;// set for astetics the left edge is on screen
	double PrevcurrentScrollAmountX = 0;
	double currentScrollAmountY = 0;
	double PrevcurrentScrollAmountY = 0;
	float totalScrollUnitsX = 1000;
	float totalScrollUnitsY = 1000;

	public ResearchScreen(ResearchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		player = inv.player;
		// TABS Code set the width , height and left top!

		this.xSize = 196;
		this.ySize = 167;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		int left = (this.xSize - 152) / 2 - 28;
		int top = 10;
		int yOffset = 10;
		int bottom = top + 150;
		int right = left + 150;

		for (Widget button : this.buttons) {
			if (button instanceof ResearchButton) {
				ResearchButton rb = (ResearchButton) button;

				if (player.getTeam() != null && rb.parentKey == null) {
					if (ModResearch.getResearch(rb.key).isUnlocked(player.getTeam().getName())) {
						rb.setButtonState(ResearchButton.ButtonState.RESEARCHED);
						rb.active = false;

					}
				}

				if (rb.parentKey != null) {

					if (player.getTeam() != null) {

						if (ModResearch.getResearch(rb.key).isUnlocked(player.getTeam().getName())) {
							rb.setButtonState(ResearchButton.ButtonState.RESEARCHED);
							rb.active = false;
						} else {
							if (!(ModResearch.getResearch(rb.parentKey).isUnlocked(player.getTeam().getName()))) {
								rb.setButtonState(ResearchButton.ButtonState.LOCKED);

								rb.active = false;

							} else {

								if (TSRTS.teamInfoArray[TeamEnum.getIDFromName(player.getTeam().getName())].getCurrenResearchKey().equals(rb.key)) {
									rb.setButtonState(ResearchButton.ButtonState.RESEARCH_IN_PROGRESS);
									rb.active = false;
								} else {
									rb.setButtonState(ResearchButton.ButtonState.NORMAL);
									rb.active = true;
								}

							}

						}
					}

					int start = (int) ((left - currentScrollAmountX) + rb.offsetX);
					int end = (int) ((left - currentScrollAmountX) + rb.parentOffsetX) + 20;
					int distance = (start - end) / 2;
					int startY = (int) ((yOffset - currentScrollAmountY) + rb.offsetY) + 10;
					int endY = (int) ((yOffset - currentScrollAmountY) + rb.parentOffsetY) + 10;

					if (end < left + 14) {
						end = left + 14;
						distance = (start - end) / 2;
					}
					if (start > right + 28 + 14) {
						start = right + 28 + 14;
						distance = (start - end) / 2;
					}
					if (distance > 0) {
						if (startY > top) {

							// draw first 1/2 of the H line (closets to child)
							if (startY < bottom) {
								// not to far down
								this.hLine(start, start - distance, startY, -1);
							}
						}
						if (endY > top) {
							// draw 2nd 1/2 of the H line closest to the parent ?
							if (endY < bottom) {
// not too far down
								this.hLine(end, end + distance, endY, -1);
							}
						}
					}
					if (startY < top) {
						startY = top;
					}

					if (endY < top) {
						endY = top;
					}

					if (startY > bottom) {
						startY = bottom;
					}

					if (endY > bottom) {
						endY = bottom;
					}

					if ((endY == top && startY == top) || startY == bottom && endY == bottom) {

					} else {
						// only draw if both points are below the top (10=top in this case)
						if (end + distance > left + 14) {
							// only draw if within the left edge
							if (end + distance < right + 28 + 14) {
								// only draw if within the right edge
								this.vLine(end + distance, startY, endY, -1);
							}
						}
					}
				}

			}

		}
// 2nd loop so the tool tip can be "over" all the lines
		for (Widget button : this.buttons) {
			if (button instanceof ResearchButton) {
				ResearchButton rb = (ResearchButton) button;
				if (rb.visible) {
					/// try to render the tooltip so its over the lines

					if (mouseX > rb.x && mouseX < rb.x + rb.getWidth()) {

						if (mouseY > rb.y && mouseY < rb.y + rb.getHeight()) {
							TranslationTextComponent ttc = new TranslationTextComponent(rb.getMessage());

							List<String> text = new ArrayList<String>();
							text.add(ttc.getUnformattedComponentText());
							if (player.getTeam() != null) {
								List<String> rvList = rb.getCosts().getToolTipText(player.getTeam().getName());
								for (int i = 0; i < rvList.size(); i++) {
									text.add(rvList.get(i));
								}
							}
							ttc = new TranslationTextComponent(rb.getDescription());
							String[] lines = ttc.getUnformattedComponentText().split("\n");
							for (int i = 0; i < lines.length; i++) {
								if (i == 0) {
									text.add(TextFormatting.BLUE.toString() + lines[i].replace("\r", ""));
								} else {
									text.add(TextFormatting.WHITE.toString() + lines[i].replace("\r", ""));
								}
							}
							rb.renderTooltip(text, mouseX - this.guiLeft, mouseY - this.guiTop);
						}
					}
				}
			}
		}

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		/// TABS Code:
		GuiUtil.RenderTabsBackground(this);

		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.blit(x, y, 0, 0, this.xSize, this.ySize);

		/// TABS Code:
		GuiUtil.RenderTabsSelected(this, 4);

		// DRAW THe scroll bar bits
		this.minecraft.getTextureManager().bindTexture(TEXTURE_WIDGETS);

		int xOffset = (this.xSize - 152) / 2;

		int yOffset = 10;
		// scroll trench
		// this.blit(this.guiLeft + xOffset - 1, this.guiTop + this.ySize - 13 - 6, 0, 24, 152, 14);

		// Scroll indicator
		// this.blit(this.guiLeft + xOffset + (int) currentScrollAmount, this.guiTop + this.ySize - 13 - 5, 0, 0, 15, 12);

		// research area
		this.blit(this.guiLeft + xOffset + -1 - 14, this.guiTop + yOffset, 0, 38, 152 + 28, 152);

		for (Widget button : this.buttons) {
			if (button instanceof ResearchButton) {
				ResearchButton rb = (ResearchButton) button;

				rb.x = (int) (this.guiLeft + xOffset - currentScrollAmountX) + rb.offsetX - 28;

				rb.y = (int) (this.guiTop + yOffset - currentScrollAmountY) + rb.offsetY;

				rb.Cull(this.guiLeft + xOffset - 14, this.guiTop + yOffset, this.guiLeft + xOffset + 150 + 14, this.guiTop + yOffset + 151);

			}
		}

	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollAmount) {

//		currentScrollAmountX = (float) (currentScrollAmountX - scrollAmount);
//		currentScrollAmountX = MathHelper.clamp(currentScrollAmountX, 0, totalScrollUnits);

		return true;
	}

	/// TABS Code:
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		GuiUtil.MouseClick(this, (int) mouseX, (int) mouseY);

		if (GuiUtil.getTabIndexFromXY(this, (int) mouseX, (int) mouseY) >= 0) {
			return super.mouseClicked(mouseX, mouseY, button);
		}
		mouseClickStartX = mouseX;
		mouseClickStartY = mouseY;
		// moveScrollIndicatorToMouse(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);

	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		PrevcurrentScrollAmountX = currentScrollAmountX;
		PrevcurrentScrollAmountY = currentScrollAmountY;

		return super.mouseReleased(mouseX, mouseY, button);
	}

	public boolean mouseDragged(double mouseX, double mouseY, int button, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		// moveScrollIndicatorToMouse(mouseX, mouseY, button);

		if (GuiUtil.getTabIndexFromXY(this, (int) mouseX, (int) mouseY) >= 0) {
			return super.mouseDragged(mouseX, mouseY, button, p_mouseDragged_6_, p_mouseDragged_8_);
		}
		currentScrollAmountX = mouseClickStartX - mouseX + PrevcurrentScrollAmountX;
		currentScrollAmountX = MathHelper.clamp(currentScrollAmountX, -totalScrollUnitsX, totalScrollUnitsX);

		currentScrollAmountY = mouseClickStartY - mouseY + PrevcurrentScrollAmountY;
		currentScrollAmountY = MathHelper.clamp(currentScrollAmountY, -totalScrollUnitsY, totalScrollUnitsY);
		return super.mouseDragged(mouseX, mouseY, button, p_mouseDragged_6_, p_mouseDragged_8_);

	}

	// TABS Code
	@Override
	public void render(int mouseX, int mouseY, float p_render_3_) {
		super.render(mouseX, mouseY, p_render_3_);
		GuiUtil.renderHoveredToolTip(this, mouseX, mouseY);
	}

	@Override
	protected void init() {
		super.init();
		teamName = Minecraft.getInstance().player.getTeam().getName();

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		y = this.guiTop + GuiUtil.TOP_BUTTON_OFFSET;
		int width = 80;
		// x = 0;// x + (width / 2);

		int height = 20;

		// ModResearch.updateAllCalcs();

//		addButton(new ResearchButton(this.guiLeft + GuiUtil.LEFT_BUTTON_OFFSET, y, 20, 18, GuiUtil.GetXStartForButtonImageXYIndex(0), GuiUtil.GetYStartForButtonImageXYIndex(3), 19, GuiUtil.BUTTON_TEXTURE, (button) -> {
//			ModNetwork.SendToServer(new TownHallButtonClickedPacketToServer(Reference.GUI_BUTTON_BUY_ARCHER));
//		}, "gui.units.archer", this, GuiUtil.LEFT_BUTTON_OFFSET, y - this.guiTop));
//		y = y + 20;
//		
		double minY = 0;
		for (Map.Entry<String, Research> entry : ModResearch.research_topics.entrySet()) {
			Research r = entry.getValue();
			addButton(new ResearchButton((int) this.guiLeft + (int) r.getCurrentX(), (int) this.guiTop + (int) r.getCurrentY(), 20, 18, GuiUtil.GetXStartForButtonImageXYIndex(r.getButtonIndexX()), GuiUtil.GetYStartForButtonImageXYIndex(r.getButtonIndexY()), 19, GuiUtil.BUTTON_TEXTURE, (button) -> {
				ModNetwork.SendToServer(new ResearchButtonClickPacketToServer(r.getKey()));
			}, r.getNameTranslationKey(), r.getDescrptionTranslationKey(), this, (int) r.getCurrentX(), (int) r.getCurrentY(), r.getKey(), r.getParentKey(), (int) r.getParentX(), (int) r.getParentY(), r.getRv()));
			if (minY > r.getCurrentY()) {
				minY = r.getCurrentY();
			}
		}

		currentScrollAmountY = minY;
	}
}
