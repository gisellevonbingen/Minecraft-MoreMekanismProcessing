package gisellevonbingen.mmp.common.crafting;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.RepresentationUtils;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public class BlastingTaggedOutputRecipe extends BlastingRecipe implements ITaggedOutputRecipe, ISingleIngredientRecipe
{
	protected final ItemStackIngredient output;

	private int revision;
	private ItemStack cachedResult;

	public BlastingTaggedOutputRecipe(String pGroup, CookingBookCategory pCategory, Ingredient pIngredient, ItemStackIngredient output, float pExperience, int pCookingTime)
	{
		super(pGroup, pCategory, pIngredient, ItemStack.EMPTY, pExperience, pCookingTime);

		this.output = output;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MMPRecipeSerializers.TAG_BLASTING.get();
	}

	@Override
	public Ingredient getIngredient()
	{
		return this.ingredient;
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
	public ItemStack assemble(SingleRecipeInput p_344838_, Provider p_336115_)
	{
		return this.getItemStackResult().copy();
	}

	@Override
	public ItemStack getResultItem(Provider pRegistries)
	{
		return this.getItemStackResult();
	}

	@Override
	public ItemStackIngredient getTaggedResult()
	{
		return this.output;
	}

}
