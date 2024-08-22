package gisellevonbingen.mmp.common.datagen.recipe.builder;

import gisellevonbingen.mmp.common.crafting.InjectingTaggedOutputRecipe;
import gisellevonbingen.mmp.common.crafting.PurifyingTaggedOutputRecipe;
import mekanism.api.datagen.recipe.MekanismRecipeBuilder;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;

public class ItemStackChemicalToTaggedOutputRecipeBuilder extends MekanismRecipeBuilder<ItemStackChemicalToTaggedOutputRecipeBuilder>
{
	private final ItemStackChemicalToTaggedOutputRecipeBuilder.Factory factory;
	private final ItemStackIngredient itemInput;
	private final ChemicalStackIngredient chemicalInput;
	private final ItemStackIngredient output;
	private final boolean perTickUsage;

	protected ItemStackChemicalToTaggedOutputRecipeBuilder(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStackIngredient output, boolean perTickUsage, ItemStackChemicalToTaggedOutputRecipeBuilder.Factory factory)
	{
		this.itemInput = itemInput;
		this.chemicalInput = chemicalInput;
		this.output = output;
		this.factory = factory;
		this.perTickUsage = perTickUsage;
	}

	public static ItemStackChemicalToTaggedOutputRecipeBuilder purifying(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStackIngredient output, boolean perTickUsage)
	{
		return new ItemStackChemicalToTaggedOutputRecipeBuilder(itemInput, chemicalInput, output, perTickUsage, PurifyingTaggedOutputRecipe::new);
	}

	public static ItemStackChemicalToTaggedOutputRecipeBuilder injecting(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStackIngredient output, boolean perTickUsage)
	{
		return new ItemStackChemicalToTaggedOutputRecipeBuilder(itemInput, chemicalInput, output, perTickUsage, InjectingTaggedOutputRecipe::new);
	}

	@Override
	protected ItemStackChemicalToItemStackRecipe asRecipe()
	{
		return this.factory.create(this.itemInput, this.chemicalInput, this.output, this.perTickUsage);
	}

	@FunctionalInterface
	public interface Factory
	{
		ItemStackChemicalToItemStackRecipe create(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStackIngredient output, boolean perTickUsage);
	}

}
