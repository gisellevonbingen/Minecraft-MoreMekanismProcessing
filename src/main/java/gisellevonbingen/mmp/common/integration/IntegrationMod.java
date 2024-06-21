package gisellevonbingen.mmp.common.integration;

import gisellevonbingen.mmp.common.datagen.BlockTagsGenerator;
import gisellevonbingen.mmp.common.datagen.ItemTagsGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public abstract class IntegrationMod
{
	public IntegrationMod()
	{

	}

	public abstract void initialize();

	public abstract String getModId();

	public ResourceLocation getResourceLocation(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(this.getModId(), path);
	}

	public void addDataGenerator(GatherDataEvent event)
	{

	}

	public void addBlockTags(BlockTagsGenerator generator)
	{

	}

	public void addItemTags(ItemTagsGenerator generator)
	{

	}

}
