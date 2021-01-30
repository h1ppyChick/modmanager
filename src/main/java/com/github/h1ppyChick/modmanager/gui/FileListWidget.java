package com.github.h1ppyChick.modmanager.gui;

import java.nio.file.Path;
import java.util.List;

import com.github.h1ppyChick.modmanager.ModManager;
import com.github.h1ppyChick.modmanager.util.Log;
import com.github.h1ppyChick.modmanager.util.Stencil;

import io.github.prospector.modmenu.gui.ModMenuTexturedButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
/**
 * 
 * @author H1ppyChick
 * @since 08/11/2020
 * 
 */
public class FileListWidget extends TwoStringListsWidget {
	/***************************************************
	 *              INSTANCE VARIABLES
	 **************************************************/
	private Log LOG = new Log("FileListWidget");
	protected final FileListWidget.MoveDirectoryUpAction onMoveDirectoryUp;
	private ButtonWidget moveDirUpBtn;
	private boolean isRootDir = false;
	private int moveDirUpBtnX = 0;
	private int topBtnY = 0;
	private LiteralText directoryPathText = null;
	/***************************************************
	 *              CONSTRUCTORS
	 **************************************************/
	public FileListWidget(MinecraftClient client, int left, int width, int height, 
			int y1, int y2, int entryHeight, List<String> widgetList, 
			ScreenBase parent, Text title, LoadListAction onLoadList, 
			ClickEntryAction onClickEntry, MoveDirectoryUpAction onMoveDirectoryUp,
			String selectedEntry) {
		super(client, left, width, height,y1, y2, entryHeight, widgetList, 
				parent,title, onLoadList,onClickEntry, selectedEntry);
		this.method_31322(false);
		this.setLeftPos(left);
	    this.centerListVertically = false;
		this.onMoveDirectoryUp = onMoveDirectoryUp;
		drawButtons();
	}

	/***************************************************
	 *              METHODS
	 **************************************************/	

	/***************************************************
	 *              BUTTONS
	 **************************************************/
	protected void drawButtons()
	{
		topBtnY = listInputY - 2;
		drawMoveDirectoryUpButton();
	}
	/***************************************************
	 *              MOVE DIRECTORY UP BUTTON
	 **************************************************/
	/**
	 * Draws the Move Directory Up Button at the correct position on the screen.
	 */
	private void drawMoveDirectoryUpButton()
	{
		if (moveDirUpBtn == null)
		{
			moveDirUpBtnX =  listInputX + listInputWidth;
			moveDirUpBtn = new ModMenuTexturedButtonWidget(moveDirUpBtnX, topBtnY, this.height, ModManager.TOP_BTN_HEIGHT, 0, 0, ModManager.UP_BUTTON_LOCATION, this.height, 42, 
					button -> {this.onMoveDirectoryUp(this);},
					ModManager.TEXT_DIR_UP_TOOLTIP, 
					(buttonWidget, matrices, mouseX, mouseY) -> {
						ModMenuTexturedButtonWidget button = (ModMenuTexturedButtonWidget) buttonWidget;
						if (button.isJustHovered()) {
							parentScreen.renderTooltip(matrices, ModManager.TEXT_DIR_UP_TOOLTIP, mouseX, mouseY);
						} else if (button.isFocusedButNotHovered()) {
							parentScreen.renderTooltip(matrices, ModManager.TEXT_DIR_UP_TOOLTIP, button.x, button.y);
						}
				}) {
			};
			
		}
		moveDirUpBtn.visible = !isRootDir;
		if (moveDirUpBtn.visible)
		{
			parentScreen.addButton(moveDirUpBtn);
		}
	}
	
	public interface MoveDirectoryUpAction {
		void onMoveDirectoryUp(FileListWidget widget);
	}
	
	public void onMoveDirectoryUp(FileListWidget widget) {
		isRootDir = !isRootDir;
		moveDirUpBtn.visible = !isRootDir;
		if (moveDirUpBtn.visible)
		{
			parentScreen.addButton(moveDirUpBtn);
		}
		else
		{
			this.onMoveDirectoryUp.onMoveDirectoryUp(widget);
		}
	}
	
	public boolean isRootDir()
	{
		return isRootDir;
	}
	
	/***************************************************
	 *              RENDERING
	 **************************************************/
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		LiteralText text = getDirectoryPathText();
		int textLeft = ModManager.LEFT_PANE_X + 4;
		int textTop = parentScreen.getTopRowY() + 5;
		int boxBottom = textTop + 15;
		int boxRight = client.textRenderer.getWidth(text) + 9;
		Stencil.setColorBlack();
		Stencil.rectangle(matrices, textLeft - 3, boxRight, textTop - 3, boxBottom);
		DrawableHelper.drawTextWithShadow(matrices, client.textRenderer, text, textLeft, textTop, 16777215);
		drawButtons();
	}

	public LiteralText getDirectoryPathText() {
		return directoryPathText;
	}

	public void setDirectoryPathText(LiteralText directoryPathText) {
		this.directoryPathText = directoryPathText;
	}
	
	public void setDirectoryPath(Path directoryPath) {
		this.directoryPathText = new LiteralText(directoryPath.toString());
	}
}
