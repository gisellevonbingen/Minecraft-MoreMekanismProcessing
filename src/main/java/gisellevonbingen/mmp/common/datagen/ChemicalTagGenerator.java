package gisellevonbingen.mmp.common.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.chemical.MMPChemicals;
import gisellevonbingen.mmp.common.material.MaterialType;
import mekanism.api.MekanismAPITags;
import mekanism.api.chemical.Chemical;
import mekanism.api.datagen.tag.ChemicalTagsProvider;
import mekanism.common.registration.impl.SlurryRegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ChemicalTagGenerator extends ChemicalTagsProvider
{
	protected ChemicalTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, MoreMekanismProcessing.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider lookupProvider)
	{
		TagAppender<Chemical> dirty = this.tag(MekanismAPITags.Chemicals.DIRTY);
		TagAppender<Chemical> clean = this.tag(MekanismAPITags.Chemicals.CLEAN);

		for (MaterialType materialType : MaterialType.values())
		{
			SlurryRegistryObject<Chemical, Chemical> registry = MMPChemicals.getSlurryRegistry(materialType);

			if (registry != null)
			{
				dirty.addOptional(registry.getDirtySlurry().getRegistryName());
				clean.addOptional(registry.getCleanSlurry().getRegistryName());
			}

		}

	}

}
