package gisellevonbingen.mmp.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gisellevonbingen.mmp.common.config.MMPConfigs;
import gisellevonbingen.mmp.common.crafting.MMPRecipeSerializers;
import gisellevonbingen.mmp.common.crafting.conditions.MMPCraftingConditions;
import gisellevonbingen.mmp.common.integration.MMPIntagrations;
import gisellevonbingen.mmp.common.item.MMPCreativeModeTabs;
import gisellevonbingen.mmp.common.item.MMPItems;
import gisellevonbingen.mmp.common.slurry.MMPSlurries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

@Mod(MoreMekanismProcessing.MODID)
public class MoreMekanismProcessing
{
	public static final String MODID = "moremekanismprocessing";
	public static final String MODANME = "More Mekanism Processing";
	public static final Logger LOGGER = LogManager.getLogger();

	public static boolean IS_DATA_GEN = false;
	public static int TAGS_UPDATED_REVISION = 0;

	public MoreMekanismProcessing()
	{
		ModLoadingContext modLoadingContext = ModLoadingContext.get();
		MMPConfigs.register(modLoadingContext);
		IEventBus modEventBus = modLoadingContext.getActiveContainer().getEventBus();

		MMPItems.ITEMS.register(modEventBus);
		MMPSlurries.SLURRIES.register(modEventBus);
		MMPCreativeModeTabs.TABS.register(modEventBus);
		MMPCraftingConditions.CONDITIONS.register(modEventBus);
		MMPRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
		MMPIntagrations.initialize();
		
		IEventBus gameEventBus = NeoForge.EVENT_BUS;
		gameEventBus.addListener(this::onTagsUpdated);
	}

	private void onTagsUpdated(TagsUpdatedEvent event)
	{
		TAGS_UPDATED_REVISION++;
	}
	
	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

}
