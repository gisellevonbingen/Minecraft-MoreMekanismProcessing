package gisellevonbingen.mmp.client;

import java.util.Map;
import java.util.Map.Entry;

import gisellevonbingen.mmp.client.renderer.color.CommonItemColor;
import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.item.MMPItems;
import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = MoreMekanismProcessing.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ClientHandler
{
	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event)
	{
		for (Entry<MaterialType, Map<MaterialState, ItemRegistryObject<Item>>> entry : MMPItems.PROCESSING_ITEMS.entrySet())
		{
			CommonItemColor itemColor = new CommonItemColor(entry.getKey());

			for (ItemRegistryObject<Item> item : entry.getValue().values())
			{
				event.register(itemColor, item.get());
			}

		}

	}

}
