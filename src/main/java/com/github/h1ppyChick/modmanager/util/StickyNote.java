package com.github.h1ppyChick.modmanager.util;

import com.github.h1ppyChick.modmanager.ModManager;
import com.github.h1ppyChick.modmanager.gui.ModToast;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
/******************************************************************************************
 *              STICKY NOTE
 * ---------------------------------------------------------------------------------------
 * Author:	h1ppychick
 * Since:	01/29/2021
 * Purpose:	This class makes adding messages to the UI easier.  Using the 
 * 			ModToast class to make sure all messages are visible to user.
 ****************************************************************************************/
public class StickyNote {
	/***************************************************
	 *              INSTANCE VARIABLES
	 **************************************************/
	private static Log LOG = new Log("ChildModsScreen");
	private static ModToast userMsg = null;
	/***************************************************
	 *              METHODS
	 **************************************************/
	public static void addErrorMsg(MinecraftClient client, String key,Object... args )
	{
		addClientMsg(client, ModToast.Type.ERROR, key, args);
	}
	
	public static void addSuccessMsg(MinecraftClient client, String key,Object... args )
	{
		addClientMsg(client, ModToast.Type.SUCCESS, key, args);
	}
	
	public static void addImportMsg(MinecraftClient client, String key,Object... args )
	{
		addClientMsg(client, ModToast.Type.IMPORT, key, args);
	}
	
	public static void addClientMsg(MinecraftClient client, ModToast.Type type, String key,Object... args )
	{
		
		Text msg = new TranslatableText(key, args);
		Text title = null;
		switch(type)
		{
			case IMPORT:
				title = ModManager.TEXT_IMPORT_TOOLTIP;
				break;
			case SUCCESS:
				title = ModManager.TEXT_SUCCESS;
				break;
			default:
				title = ModManager.TEXT_ERROR;
				break;
		}
		if (userMsg == null)
		{
			userMsg = ModToast.create(client, type, title, msg);
		}
		else
		{
			userMsg.addTextToList(client, msg);
		}
	}
	
	public static void showClientMsg(MinecraftClient client)
	{
		if (userMsg != null)
		{
			ToastManager toastManager = client.getToastManager();
			toastManager.add(userMsg);
			userMsg = null;
		}
	}
	
}
