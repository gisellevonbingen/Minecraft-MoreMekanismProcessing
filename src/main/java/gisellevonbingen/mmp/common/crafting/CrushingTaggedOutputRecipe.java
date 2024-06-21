package gisellevonbingen.mmp.common.crafting;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.RepresentationUtils;
import mekanism.api.recipes.basic.BasicCrushingRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.registries.MekanismItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CrushingTaggedOutputRecipe extends BasicCrushingRecipe implements ITaggedOutputRecipe
{
	protected final ItemStackIngredient output;

	private int revision;
	private ItemStack cachedResult;

	public CrushingTaggedOutputRecipe(ItemStackIngredient input, ItemStackIngredient output)
	{
		super(input, MekanismItems.ATOMIC_ALLOY.getItemStack());
		this.output = output;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipeSerializer<BasicCrushingRecipe> getSerializer()
	{
		return (RecipeSerializer<BasicCrushingRecipe>) MMPRecipeSerializers.TAG_CRUSHING.get();
	}

	@Override
	public ItemStackIngredient getTaggedResult()
	{
		return this.output;
	}

	@Override
	public ItemStack getItemStackResult()
	{
		var revision = MoreMekanismProcessing.TAGS_UPDATED_REVISION;

		if (this.revision != revision || this.cachedResult == null)
		{
			this.revision = revision;
			this.cachedResult = RepresentationUtils.getRepresentation(this.output);
		}

		return this.cachedResult;
	}

	@Override
	public ItemStack getOutput(ItemStack input)
	{
		return this.getItemStackResult().copy();
	}

	@Override
	public ItemStack getResultItem(@NotNull Provider provider)
	{
		return this.getItemStackResult().copy();
	}

	@Override
	public List<ItemStack> getOutputDefinition()
	{
		return Collections.singletonList(this.getItemStackResult());
	}

	@Override
	public ItemStack getOutputRaw()
	{
		return this.getItemStackResult();
	}

}
