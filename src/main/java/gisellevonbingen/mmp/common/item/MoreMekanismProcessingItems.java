package gisellevonbingen.mmp.common.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MoreMekanismProcessingItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MoreMekanismProcessing.MODID);
	public static final Map<MaterialType, Map<MaterialState, RegistryObject<Item>>> PROCESSING_ITEMS = new HashMap<>();

	public static ResourceLocation getProcessingItemName(MaterialType materialType, MaterialState materialState)
	{
		ResourceLocation presetName = materialType.getPresetItem(materialState);

		if (presetName != null)
		{
			return presetName;
		}
		else
		{
			Map<MaterialState, RegistryObject<Item>> map = PROCESSING_ITEMS.get(materialType);

			if (map == null)
			{
				return null;
			}

			RegistryObject<Item> registryObject = map.get(materialState);
			return registryObject != null ? registryObject.getId() : null;
		}

	}

	@SuppressWarnings("deprecation")
	public static Item getProcessingItem(MaterialType materialType, MaterialState materialState)
	{
		ResourceLocation presetName = materialType.getPresetItem(materialState);

		if (presetName != null)
		{
			return Registry.ITEM.get(presetName);
		}
		else
		{
			Map<MaterialState, RegistryObject<Item>> map = PROCESSING_ITEMS.get(materialType);

			if (map == null)
			{
				return null;
			}

			RegistryObject<Item> registryObject = map.get(materialState);
			return registryObject != null ? (ItemStatedMaterial) registryObject.get() : null;
		}

	}

	public static List<ItemStatedMaterial> getProcessingItems(MaterialState materialState)
	{
		List<ItemStatedMaterial> list = new ArrayList<>();

		for (Entry<MaterialType, Map<MaterialState, RegistryObject<Item>>> entry : PROCESSING_ITEMS.entrySet())
		{
			RegistryObject<Item> registryObject = entry.getValue().get(materialState);

			if (registryObject != null)
			{
				ItemStatedMaterial item = (ItemStatedMaterial) registryObject.get();
				list.add(item);
			}

		}

		return list;
	}

	public static void register(IEventBus eventBus)
	{
		register(eventBus, REGISTER);
	}

	public static void register(IEventBus eventBus, DeferredRegister<Item> register)
	{
		register.register(eventBus);

		for (MaterialType materialType : MaterialType.values())
		{
			registerOreType(register, materialType);
		}

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

	public static void registerOreType(DeferredRegister<Item> registry, MaterialType materialType)
	{
		Map<MaterialState, RegistryObject<Item>> map2 = new HashMap<>();
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
				RegistryObject<Item> registryObject = registry.register(materialState.getItemNamePath(materialType), () -> new ItemStatedMaterial(materialType, materialState));
				map2.put(materialState, registryObject);
			}

		}

	}

}
