package gisellevonbingen.mmp.common.datagen.recipe.builder;

import gisellevonbingen.mmp.common.crafting.InjectingTaggedOutputRecipe;
import gisellevonbingen.mmp.common.crafting.PurifyingTaggedOutputRecipe;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.datagen.recipe.MekanismRecipeBuilder;
import mekanism.api.recipes.chemical.ItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.GasStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;

public class ItemStackChemicalToTaggedOutputRecipeBuilder<CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>, INGREDIENT extends ChemicalStackIngredient<CHEMICAL, STACK, ?>> extends MekanismRecipeBuilder<ItemStackChemicalToTaggedOutputRecipeBuilder<CHEMICAL, STACK, INGREDIENT>>
{
	private final ItemStackChemicalToTaggedOutputRecipeBuilder.Factory<CHEMICAL, STACK, INGREDIENT> factory;
	private final ItemStackIngredient itemInput;
	private final INGREDIENT chemicalInput;
	private final ItemStackIngredient output;

	protected ItemStackChemicalToTaggedOutputRecipeBuilder(ItemStackIngredient itemInput, INGREDIENT chemicalInput, ItemStackIngredient output, ItemStackChemicalToTaggedOutputRecipeBuilder.Factory<CHEMICAL, STACK, INGREDIENT> factory)
	{
		this.itemInput = itemInput;
		this.chemicalInput = chemicalInput;
		this.output = output;
		this.factory = factory;
	}

	public static ItemStackChemicalToTaggedOutputRecipeBuilder<Gas, GasStack, GasStackIngredient> purifying(ItemStackIngredient itemInput, GasStackIngredient gasInput, ItemStackIngredient output)
	{
		return new ItemStackChemicalToTaggedOutputRecipeBuilder<>(itemInput, gasInput, output, PurifyingTaggedOutputRecipe::new);
	}

	public static ItemStackChemicalToTaggedOutputRecipeBuilder<Gas, GasStack, GasStackIngredient> injecting(ItemStackIngredient itemInput, GasStackIngredient gasInput, ItemStackIngredient output)
	{
		return new ItemStackChemicalToTaggedOutputRecipeBuilder<>(itemInput, gasInput, output, InjectingTaggedOutputRecipe::new);
	}

	@Override
	protected ItemStackChemicalToItemStackRecipe<CHEMICAL, STACK, INGREDIENT> asRecipe()
	{
		return this.factory.create(this.itemInput, this.chemicalInput, this.output);
	}

	@FunctionalInterface
	public interface Factory<CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>, INGREDIENT extends ChemicalStackIngredient<CHEMICAL, STACK, ?>>
	{
		ItemStackChemicalToItemStackRecipe<CHEMICAL, STACK, INGREDIENT> create(ItemStackIngredient itemInput, INGREDIENT chemicalInput, ItemStackIngredient output);
	}

}
