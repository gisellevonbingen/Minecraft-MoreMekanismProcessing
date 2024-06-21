package gisellevonbingen.mmp.common.integration.voluminousenergy;

import gisellevonbingen.mmp.common.datagen.BlockTagsGenerator;
import gisellevonbingen.mmp.common.datagen.ItemTagsGenerator;
import gisellevonbingen.mmp.common.integration.IntegrationMod;
import gisellevonbingen.mmp.common.integration.IntegrationTags;
import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import net.minecraft.resources.ResourceLocation;

public class VoluminousEnergyMod extends IntegrationMod
{
	public static final String MODID = "voluminousenergy";

	public static final ResourceLocation BAUXITE_ORE = rl("bauxiteore");
	public static final ResourceLocation BAUXITE_DEEPSLATE_ORE = rl("deepslate_bauxite_ore");
	public static final ResourceLocation BAUXITE_RAW_ITEM = rl("raw_bauxite");
	public static final ResourceLocation BAUXITE_RAW_STORAGE_BLOCK = rl("raw_bauxite_block");

	public static final ResourceLocation RUTILE_ORE = rl("rutileore");
	public static final ResourceLocation RUTILE_DEEPSLATE_ORE = rl("deepslate_rutile_ore");
	public static final ResourceLocation RUTILE_RAW_ITEM = rl("raw_rutile");
	public static final ResourceLocation RUTILE_RAW_STORAGE_BLOCK = rl("raw_rutile_block");

	public VoluminousEnergyMod()
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

		generator.tagOresOptional(IntegrationTags.Blocks.ORES_ALUMINUM, BAUXITE_ORE);
		generator.tagOresOptional(IntegrationTags.Blocks.ORES_ALUMINUM, BAUXITE_DEEPSLATE_ORE);
		generator.tagOptional(MaterialState.RAW_STORAGE_BLOCKS.getStateBlockTag(MaterialType.ALUMINUM), BAUXITE_RAW_STORAGE_BLOCK);

		generator.tagOresOptional(IntegrationTags.Blocks.ORES_TITANIUM, RUTILE_ORE);
		generator.tagOresOptional(IntegrationTags.Blocks.ORES_TITANIUM, RUTILE_DEEPSLATE_ORE);
		generator.tagOptional(MaterialState.RAW_STORAGE_BLOCKS.getStateBlockTag(MaterialType.TITANIUM), RUTILE_RAW_STORAGE_BLOCK);
	}

	@Override
	public void addItemTags(ItemTagsGenerator generator)
	{
		super.addItemTags(generator);

		generator.tagOptional(MaterialState.RAW_ITEM.getStateTag(MaterialType.ALUMINUM), BAUXITE_RAW_ITEM);

		generator.tagOptional(MaterialState.RAW_ITEM.getStateTag(MaterialType.TITANIUM), RUTILE_RAW_ITEM);
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
