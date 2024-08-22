package gisellevonbingen.mmp.common.datagen;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.chemical.MMPChemicals;
import gisellevonbingen.mmp.common.chemical.MMPSlurry;
import gisellevonbingen.mmp.common.chemical.MMPSlurryBuilder;
import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class LanguagesGenerator extends LanguageProvider
{
	public LanguagesGenerator(PackOutput output)
	{
		super(output, MoreMekanismProcessing.MODID, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		String statedCommentPrefix = MaterialState.makeDescriptionId("_comment");
		int statedCommentLine = 0;
		this.add(statedCommentPrefix + (statedCommentLine++), "");
		this.add(statedCommentPrefix + (statedCommentLine++), "===== Rule =====");
		this.add(statedCommentPrefix + (statedCommentLine++), "");
		this.add(statedCommentPrefix + (statedCommentLine++), "%s is material type (e.g. Cobalt, Silver)");
		this.add(statedCommentPrefix + (statedCommentLine++), "    from materialType.xxxxx");
		this.add(statedCommentPrefix + (statedCommentLine++), "");
		this.add(statedCommentPrefix + (statedCommentLine++), "Items can override this rule thought by declare that item's translation");
		this.add(statedCommentPrefix + (statedCommentLine++), "    e.g.) '" + MaterialState.INGOT.getItem(MaterialType.COBALT).getDescriptionId() + "' : 'Blue Metal'");
		this.add(statedCommentPrefix + (statedCommentLine++), "So is slurry");
		this.add(statedCommentPrefix + (statedCommentLine++), "    e.g.) '" + MMPChemicals.getSlurryRegistry(MaterialType.COBALT).getCleanSlurry().getTranslationKey() + "' : 'Clean Blue Metal Slurry'");

		this.add(MaterialState.ORE.getStatedDescriptionId(), "%s Ore");
		this.add(MaterialState.RAW_ITEM.getStatedDescriptionId(), "Raw %s");
		this.add(MaterialState.DUST.getStatedDescriptionId(), "%s Dust");
		this.add(MaterialState.DIRTY_DUST.getStatedDescriptionId(), "Dirty %s Dust");
		this.add(MaterialState.CLUMP.getStatedDescriptionId(), "%s Clump");
		this.add(MaterialState.SHARD.getStatedDescriptionId(), "%s Shard");
		this.add(MaterialState.CRYSTAL.getStatedDescriptionId(), "%s Crystal");
		this.add(MaterialState.INGOT.getStatedDescriptionId(), "%s Ingot");
		this.add(MaterialState.NUGGET.getStatedDescriptionId(), "%s Nugget");
		this.add(MaterialState.GEM.getStatedDescriptionId(), "%s Gem");
		this.add(MMPSlurry.makeDescriptionId(MMPSlurryBuilder.DIRTY), "Dirty %s Slurry");
		this.add(MMPSlurry.makeDescriptionId(MMPSlurryBuilder.CLEAN), "Clean %s Slurry");

		String materialTypeCommentPrefix = MaterialType.makeDescriptionId("_comment");
		int materialTypeCommentLine = 0;
		this.add(materialTypeCommentPrefix + (materialTypeCommentLine++), "");
		this.add(materialTypeCommentPrefix + (materialTypeCommentLine++), "===== Material Type =====");
		this.add(materialTypeCommentPrefix + (materialTypeCommentLine++), "");

		for (MaterialType materialType : MaterialType.values())
		{
			this.add(materialType.getDescriptionId(), materialType.getDisplayName());
		}

	}

}
