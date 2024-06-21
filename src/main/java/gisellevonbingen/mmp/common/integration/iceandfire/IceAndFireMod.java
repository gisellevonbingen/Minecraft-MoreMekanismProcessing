package gisellevonbingen.mmp.common.integration.iceandfire;

import gisellevonbingen.mmp.common.datagen.BlockTagsGenerator;
import gisellevonbingen.mmp.common.integration.IntegrationMod;
import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import net.minecraft.resources.ResourceLocation;

public class IceAndFireMod extends IntegrationMod
{
	public static final String MODID = "iceandfire";

	public static final ResourceLocation AMYTHEST_ORE = rl("amythest_ore");
	public static final ResourceLocation SAPPHIRE_ORE = rl("sapphire_ore");

	public IceAndFireMod()
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

		generator.tagOresOptional(MaterialState.ORE.getStateBlockTag(MaterialType.SAPPHIRE), SAPPHIRE_ORE);
		generator.tagOresOptional(MaterialState.ORE.getStateBlockTag(MaterialType.AMETHYST), AMYTHEST_ORE);
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
