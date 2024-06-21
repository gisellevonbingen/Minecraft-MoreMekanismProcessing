package gisellevonbingen.mmp.common.integration.biggerreactors;

import gisellevonbingen.mmp.common.datagen.BlockTagsGenerator;
import gisellevonbingen.mmp.common.integration.IntegrationMod;
import gisellevonbingen.mmp.common.integration.IntegrationTags;
import net.minecraft.resources.ResourceLocation;

public class BiggerReactorsMod extends IntegrationMod
{
	public static final String MODID = "biggerreactors";

	public static final ResourceLocation YELLORITE_ORE = rl("yellorite_ore");

	public BiggerReactorsMod()
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
