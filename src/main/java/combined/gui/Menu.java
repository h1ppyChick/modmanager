package combined.gui;

import combined.ManyMods;
import combined.util.Conditional;
import io.github.prospector.modmenu.ModMenu;
import io.github.prospector.modmenu.gui.ModListEntry;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModEnvironment;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class Menu {

	// Find the Many Mods mod in the parent map
	public static ModContainer getThisMod()
	{
		for( ModContainer key : ModMenu.PARENT_MAP.keySet())
		{
			String thisModId = key.getMetadata().getId();
			if (thisModId.equals(ManyMods.MOD_ID)) {
				return key;
			}
			
		}
		return null;
	}

	public static void addChild(ModContainer mod)
	{
		ModContainer thisMod = getThisMod();
		if (thisMod == null) return;
		ModMetadata metadata = mod.getMetadata();
		String id = metadata.getId();
		boolean isLibrary = metadata.containsCustomValue("modmenu:api") && metadata.getCustomValue("modmenu:api").getAsBoolean();
		if (isLibrary) {
			ModMenu.addLibraryMod(id);
		}
		boolean hasClientValue = metadata.containsCustomValue("modmenu:clientsideOnly");
		boolean clientEnvironmentOnly = metadata.getEnvironment() == ModEnvironment.CLIENT;
		boolean clientsideOnlyValue = hasClientValue && metadata.getCustomValue("modmenu:clientsideOnly").getAsBoolean();
		if (clientEnvironmentOnly && !hasClientValue || hasClientValue && clientsideOnlyValue) {
			Conditional.add(ModMenu.CLIENTSIDE_MODS, id);
		}
		if (metadata.containsCustomValue("modmenu:deprecated") && metadata.getCustomValue("modmenu:deprecated").getAsBoolean()) {
			Conditional.add(ModMenu.DEPRECATED_MODS,id);
		}
		if (metadata.containsCustomValue("patchwork:source") && metadata.getCustomValue("patchwork:source").getAsObject() != null) {
			CustomValue.CvObject object = metadata.getCustomValue("patchwork:source").getAsObject();
			if ("forge".equals(object.get("loader").getAsString())) {
				Conditional.add(ModMenu.PATCHWORK_FORGE_MODS,id);
			}
		}
		Conditional.add(ModMenu.PARENT_MAP,thisMod, mod);
		if (isLibrary) {
			Conditional.add(ModMenu.CHILD_LIBRARIES,id);
		} else {
			Conditional.add(ModMenu.CHILD_NONLIB_MODS,id);
			Conditional.add(ModMenu.ALL_NONLIB_MODS,id);
		}
	}

	public static void removeChildEntry(ModListEntry modEntry)
	{
		ModContainer manyModsMod = getThisMod();
		ModContainer modToRemove = null;
		for (ModContainer mod : ModMenu.PARENT_MAP.get(manyModsMod))
		{
			String thisModId = mod.getMetadata().getId();
			String modEntryId = modEntry.getMetadata().getId();
			if (thisModId.equals(modEntryId))
			{
				modToRemove = mod;
				break;
			}
		}
		
		if (modToRemove != null)
		{
			String modId = modToRemove.getMetadata().getId();
			ModMenu.LIBRARY_MODS.remove(modId);
			ModMenu.ROOT_LIBRARIES.remove(modId);
			ModMenu.CHILD_LIBRARIES.remove(modId);
			ModMenu.ALL_NONLIB_MODS.remove(modId);
			ModMenu.ROOT_NONLIB_MODS.remove(modId);
			ModMenu.CHILD_NONLIB_MODS.remove(modId);
			ModMenu.CLIENTSIDE_MODS.remove(modId);
			ModMenu.DEPRECATED_MODS.remove(modId);
			ModMenu.PATCHWORK_FORGE_MODS.remove(modId);	
			ModMenu.PARENT_MAP.remove(manyModsMod, modToRemove);
		}
		
	}

}