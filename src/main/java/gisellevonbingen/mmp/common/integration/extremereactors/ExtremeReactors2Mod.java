package gisellevonbingen.mmp.common.integration.extremereactors;

import gisellevonbingen.mmp.common.datagen.BlockTagsGenerator;
import gisellevonbingen.mmp.common.integration.IntegrationMod;
import gisellevonbingen.mmp.common.integration.IntegrationTags;
import net.minecraft.resources.ResourceLocation;

public class ExtremeReactors2Mod extends IntegrationMod
{
	public static final String MODID = "bigreactors";

	public static final ResourceLocation YELLORITE_ORE = rl("yellorite_ore");

	public ExtremeReactors2Mod()
	{

	}

	@Override
	public void initialize()
	{

	}

	@Override
	public void addBlockTags(BlockTagsGenerator generator)
	{
		super.addBlockTags(generator);

		generator.tagOresOptional(IntegrationTags.Blocks.ORES_YELLORITE, YELLORITE_ORE);
		generator.tagOresOptional(IntegrationTags.Blocks.ORES_URANIUM, YELLORITE_ORE);
	}

	@Override
	public String getModId()
	{
		return MODID;
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

}
