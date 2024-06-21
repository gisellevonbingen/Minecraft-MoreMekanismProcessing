package gisellevonbingen.mmp.common.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.config.MMPConfigs;
import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

public class MMPItems
{
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(MoreMekanismProcessing.MODID);
	public static final Map<MaterialType, Map<MaterialState, ItemRegistryObject<Item>>> PROCESSING_ITEMS = new HashMap<>();

	static
	{
		for (MaterialType materialType : MaterialType.values())
		{
			registerOreType(ITEMS, materialType);
		}

	}

	public static ResourceLocation getProcessingItemName(MaterialType materialType, MaterialState materialState)
	{
		ResourceLocation presetName = materialType.getPresetItem(materialState);

		if (presetName != null)
		{
			return presetName;
		}
		else
		{
			Map<MaterialState, ItemRegistryObject<Item>> map = PROCESSING_ITEMS.get(materialType);

			if (map == null)
			{
				return null;
			}

			ItemRegistryObject<Item> registryObject = map.get(materialState);
			return registryObject != null ? registryObject.getRegistryName() : null;
		}

	}

	public static Item getProcessingItem(MaterialType materialType, MaterialState materialState)
	{
		ResourceLocation presetName = materialType.getPresetItem(materialState);

		if (presetName != null)
		{
			return BuiltInRegistries.ITEM.get(presetName);
		}
		else
		{
			Map<MaterialState, ItemRegistryObject<Item>> map = PROCESSING_ITEMS.get(materialType);

			if (map == null)
			{
				return null;
			}

			ItemRegistryObject<Item> registryObject = map.get(materialState);
			return registryObject != null ? (ItemStatedMaterial) registryObject.get() : null;
		}

	}

	public static List<ItemStatedMaterial> getProcessingItems(MaterialState materialState)
	{
		List<ItemStatedMaterial> list = new ArrayList<>();

		for (Entry<MaterialType, Map<MaterialState, ItemRegistryObject<Item>>> entry : PROCESSING_ITEMS.entrySet())
		{
			ItemRegistryObject<Item> registryObject = entry.getValue().get(materialState);

			if (registryObject != null)
			{
				ItemStatedMaterial item = (ItemStatedMaterial) registryObject.get();
				list.add(item);
			}

		}

		return list;
	}

	public static int getProcessingLevel(MaterialState materialState)
	{
		if (materialState == MaterialState.CRYSTAL)
		{
			return 5;
		}
		else if (materialState == MaterialState.SHARD)
		{
			return 4;
		}
		else if (materialState == MaterialState.DIRTY_DUST || materialState == MaterialState.CLUMP)
		{
			return 3;
		}

		return 2;
	}

	public static boolean testProcessingLevel(MaterialType materialType, MaterialState materialState)
	{
		if (MoreMekanismProcessing.IS_DATA_GEN == true)
		{
			return true;
		}

		ConfigValue<Integer> configValue = MMPConfigs.COMMON.processingLevels.get(materialType);

		if (configValue == null)
		{
			return true;
		}

		int processingLevel = configValue.get();
		int requireLevel = getProcessingLevel(materialState);

		return processingLevel >= requireLevel;
	}

	public static void registerOreType(ItemDeferredRegister registry, MaterialType materialType)
	{
		Map<MaterialState, ItemRegistryObject<Item>> map2 = new HashMap<>();
		PROCESSING_ITEMS.put(materialType, map2);

		for (MaterialState materialState : materialType.getResultShape().getProcessableStates())
		{
			ResourceLocation presetName = materialType.getPresetItem(materialState);

			if (presetName != null)
			{
				continue;
			}
			else if (materialState.hasOwnItem() == true)
			{
				ItemRegistryObject<Item> registryObject = registry.register(materialState.getItemNamePath(materialType), () -> new ItemStatedMaterial(materialType, materialState));
				map2.put(materialState, registryObject);
			}

		}

	}

}
