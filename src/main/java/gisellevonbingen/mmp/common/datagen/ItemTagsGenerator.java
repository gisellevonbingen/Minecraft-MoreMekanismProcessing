package gisellevonbingen.mmp.common.datagen;

import java.util.concurrent.CompletableFuture;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.integration.MMPIntagrations;
import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemTagsGenerator extends ItemTagsProvider
{
	protected final BlockTagsGenerator blockTagGenerator;

	public ItemTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsGenerator blockTagsGenerator, ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, blockTagsGenerator.contentsGetter(), MoreMekanismProcessing.MODID, existingFileHelper);
		this.blockTagGenerator = blockTagsGenerator;
	}

	@Override
	protected void addTags(HolderLookup.Provider lookupProvider)
	{
		this.buildStatedMaterials();

		MMPIntagrations.getMods().forEach(m -> m.addItemTags(this));
		this.copyTags();
	}

	protected void buildStatedMaterials()
	{
		for (MaterialType materialType : MaterialType.values())
		{
			for (MaterialState materialState : materialType.getResultShape().getProcessableStates())
			{
				TagAppender<Item> builderCategory = this.tag(materialState.getCategoryTag());
				TagAppender<Item> builderState = this.tag(materialState.getStateTag(materialType));

				if (materialState.hasOwnItem() == true)
				{
					ResourceLocation itemName = materialState.getItemName(materialType);
					builderCategory.addOptional(itemName);
					builderState.addOptional(itemName);
				}

			}

		}

	}

	protected void copyTags()
	{
		this.blockTagGenerator.streamTags().forEach(blockTag ->
		{
			TagKey<Item> itemTag = ItemTags.create(blockTag.location());
			this.copy(blockTag, itemTag);
		});

	}

	public void tagOptional(TagKey<Item> tag, ResourceLocation blockName)
	{
		this.tag(tag).addOptional(blockName);
	}
}
