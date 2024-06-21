package gisellevonbingen.mmp.common.datagen;

import java.util.concurrent.CompletableFuture;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.data.EmptyExistingFileHelper;
import gisellevonbingen.mmp.common.integration.MMPIntagrations;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		MoreMekanismProcessing.IS_DATA_GEN = true;

		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		BlockTagsGenerator blockTagsGenerator = new BlockTagsGenerator(output, lookupProvider, existingFileHelper);
		generator.addProvider(event.includeServer(), blockTagsGenerator);
		generator.addProvider(event.includeServer(), new ItemTagsGenerator(output, lookupProvider, blockTagsGenerator, existingFileHelper));
		generator.addProvider(event.includeServer(), new SlurryTagGenerator(output, lookupProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new RecipesGenerator(output, lookupProvider));
		generator.addProvider(event.includeServer(), new LanguagesGenerator(output));

		generator.addProvider(event.includeClient(), new ItemModelGenerator(output, new EmptyExistingFileHelper()));

		MMPIntagrations.getMods().forEach(m -> m.addDataGenerator(event));
	}

}
