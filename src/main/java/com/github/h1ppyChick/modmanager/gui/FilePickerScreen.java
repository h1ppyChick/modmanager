package com.github.h1ppyChick.modmanager.gui;

import java.util.List;

import com.github.h1ppyChick.modmanager.ModManager;
import com.github.h1ppyChick.modmanager.config.Props;
import com.github.h1ppyChick.modmanager.gui.StringListWidget.LoadListAction;
import com.github.h1ppyChick.modmanager.util.Log;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
/**
 * 
 * @author H1ppyChick
 * @since 08/11/2020
 * 
 */
public class FilePickerScreen extends TwoStringListsScreen {
	/***************************************************
	 *              CONSTANTS
	 **************************************************/
	private final static String TITLE_ID = ModManager.MOD_ID + ".filepicker.title";

	/***************************************************
	 *              INSTANCE VARIABLES
	 **************************************************/
	private Log LOG = new Log("FilePickerScreen");
	private ClickDoneButtonAction onClickDoneButton;
	private FileListWidget availFileListWidget = null;	
	/***************************************************
	 *              CONSTRUCTORS
	 **************************************************/
	protected FilePickerScreen(Text title) {
		super(title);
	}
	
	public FilePickerScreen(ScreenBase previousScreen, LoadListAction onLoadAvailList, 
			LoadListAction onLoadSelectedList,
			ClickDoneButtonAction onClickDoneButton) {
		super(previousScreen, TITLE_ID, onLoadAvailList, 
				"",
				onLoadSelectedList,
				"");
		this.onClickDoneButton = onClickDoneButton;
		
	}
	
	/***************************************************
	 *              METHODS
	 **************************************************/
	@Override
	public void init() {
		LOG.enter("init");
		super.init();
		drawDoneButton();
		this.addChild(availableList);
		this.addChild(selectedList);
		availFileListWidget = new FileListWidget(this.client,  
				ModManager.LEFT_PANE_X, 
				previousScreen.paneWidth, 
				this.height + getY2Offset(), 
				previousScreen.paneY + getY1Offset(), 
				this.height + getY2Offset(), 
				ModManager.TOP_ENTRY_HEIGHT, 
				availableList.getValueList(), 
				previousScreen, 
				availableTitle, 
				availableList.onLoadList, 
				(StringListWidget.ClickEntryAction) entry -> availableList.onClickEntry(entry), 
				(FileListWidget.MoveDirectoryUpAction) widget -> onMoveDirectoryUp(widget), 
				"");
		availFileListWidget.setDirectoryPath(Props.getModsDirPath());
		this.addChild(availFileListWidget);
		LOG.exit("init");
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		availFileListWidget.render(matrices, mouseX, mouseY, delta);
		
	}
	
	public interface ClickDoneButtonAction {
		void onClickDoneButton(List<String> selectedList);
	}
	
	@Override
	protected void doneButtonClick()
	{
		this.onClickDoneButton.onClickDoneButton(selectedList.getAddedList());
		this.onClose();
	}
	
	public void onMoveDirectoryUp(FileListWidget widget) {
		
	}
}
