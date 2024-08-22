package gisellevonbingen.mmp.common.crafting;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.RepresentationUtils;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.basic.BasicInjectingRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.registries.MekanismItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class InjectingTaggedOutputRecipe extends BasicInjectingRecipe implements ITaggedOutputRecipe
{
	protected final ItemStackIngredient output;

	private int revision;
	private ItemStack cachedResult;

	public InjectingTaggedOutputRecipe(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStackIngredient output, boolean perTickUsage)
	{
		super(itemInput, chemicalInput, MekanismItems.ATOMIC_ALLOY.getItemStack(), perTickUsage);
		this.output = output;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipeSerializer<BasicInjectingRecipe> getSerializer()
	{
		return (RecipeSerializer<BasicInjectingRecipe>) MMPRecipeSerializers.TAG_INJECTING.get();
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
	public ItemStack getOutput(ItemStack inputItem, ChemicalStack inputChemical)
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
