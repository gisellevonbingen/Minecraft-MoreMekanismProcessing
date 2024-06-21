package gisellevonbingen.mmp.common.datagen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.crafting.BlastingTaggedOutputRecipe;
import gisellevonbingen.mmp.common.crafting.SmeltingTaggedOutputRecipe;
import gisellevonbingen.mmp.common.crafting.conditions.ProcessingLevelCondition;
import gisellevonbingen.mmp.common.datagen.recipe.builder.CrystallizerTaggedOutputRecipeBuilder;
import gisellevonbingen.mmp.common.datagen.recipe.builder.ItemStackChemicalToTaggedOutputRecipeBuilder;
import gisellevonbingen.mmp.common.datagen.recipe.builder.ItemStackToTaggedOutputRecipeBuilder;
import gisellevonbingen.mmp.common.material.MaterialResultShape;
import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import gisellevonbingen.mmp.common.slurry.MMPSlurries;
import gisellevonbingen.mmp.common.slurry.MMPSlurry;
import gisellevonbingen.mmp.common.slurry.MMPSlurryBuilder;
import gisellevonbingen.mmp.common.util.ThreeFunction;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.datagen.recipe.builder.ChemicalDissolutionRecipeBuilder;
import mekanism.api.datagen.recipe.builder.FluidSlurryToSlurryRecipeBuilder;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.GasStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.SlurryStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registration.impl.SlurryRegistryObject;
import mekanism.common.registries.MekanismGases;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;

public class RecipesGenerator extends RecipeProvider
{
	public RecipesGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput output)
	{
		for (MaterialType materialType : MaterialType.values())
		{
			new OreRecipesGenerator(materialType, output).build();
		}

	}

	public ResourceLocation getRecipeName(String output, String name)
	{
		return MoreMekanismProcessing.rl((output + "/" + name).toLowerCase());
	}

	public static class OreRecipesGenerator
	{
		private MaterialType materialType;
		private RecipeOutput output;
		private List<ICondition> conditions;

		public OreRecipesGenerator(MaterialType materialType, RecipeOutput output)
		{
			this.materialType = materialType;
			this.output = output;
			this.conditions = new ArrayList<>();
		}

		public ICondition createConditionHasState(MaterialState state)
		{
			return new NotCondition(new TagEmptyCondition(state.getStateTagName(this.materialType)));
		}

		public void applyProcssingLevelCondition(int processingLevel, Runnable runnable)
		{
			this.applyCondition(new ProcessingLevelCondition(this.materialType.getBaseName(), processingLevel), runnable);
		}

		public void applyCondition(ICondition condition, Runnable runnable)
		{
			try
			{
				this.conditions.add(condition);
				runnable.run();
			}
			finally
			{
				this.conditions.remove(condition);
			}

		}

		public ICondition[] collect(Consumer<Consumer<ICondition>> consumer)
		{
			List<ICondition> list = new ArrayList<>();
			consumer.accept(list::add);
			return list.toArray(ICondition[]::new);
		}

		public void applyConditionWithState(Consumer<ICondition> consumer, MaterialState state)
		{
			if (state.hasOwnItem() == false)
			{
				consumer.accept(this.createConditionHasState(state));
			}

			this.applyCondition(consumer);
		}

		public void applyCondition(Consumer<ICondition> consumer)
		{
			for (ICondition condition : this.conditions)
			{
				consumer.accept(condition);
			}

		}

		public void build()
		{
			this.applyProcssingLevelCondition(5, this::buildProcessingLevel5);
			this.applyProcssingLevelCondition(4, this::buildProcessingLevel4);
			this.applyProcssingLevelCondition(3, this::buildProcessingLevel3);
			this.applyProcssingLevelCondition(2, this::buildProcessingLevel2);

			this.buildOthers();
		}

		public void buildProcessingLevel5()
		{
			if (this.canProcess(MaterialState.CRYSTAL) == true)
			{
				SlurryRegistryObject<Slurry, Slurry> slurryRegistry = MMPSlurries.getSlurryRegistry(this.materialType);
				Slurry dirtySlurry = slurryRegistry.getDirtySlurry();
				Slurry cleanSlurry = slurryRegistry.getCleanSlurry();
				FluidStackIngredient water = IngredientCreatorAccess.fluid().from(Fluids.WATER, 5);

				if (this.canProcess(MaterialState.ORE) == true)
				{
					GasStackIngredient sulfuricAcid1 = IngredientCreatorAccess.gasStack().from(MekanismGases.SULFURIC_ACID.get(), 1);
					this.buildChemicalDissolution(MaterialState.ORE, 1, dirtySlurry, 1000, sulfuricAcid1);
				}

				if (this.canProcess(MaterialState.RAW_ITEM) == true)
				{
					GasStackIngredient sulfuricAcid1 = IngredientCreatorAccess.gasStack().from(MekanismGases.SULFURIC_ACID.get(), 1);
					this.buildChemicalDissolution(MaterialState.RAW_ITEM, 3, dirtySlurry, 2000, sulfuricAcid1);

					GasStackIngredient sulfuricAcid2 = IngredientCreatorAccess.gasStack().from(MekanismGases.SULFURIC_ACID.get(), 2);
					this.buildChemicalDissolution(MaterialState.RAW_STORAGE_BLOCKS, 1, dirtySlurry, 6000, sulfuricAcid2);
				}

				this.buildChemicalWashing(water, dirtySlurry, cleanSlurry);
				this.buildChemicalCrystallizing(IngredientCreatorAccess.slurryStack().from(cleanSlurry, 200), MaterialState.CRYSTAL, 1);

				if (this.canProcess(MaterialState.SHARD) == true)
				{
					GasStackIngredient hydrogenChloride = IngredientCreatorAccess.gasStack().from(MekanismGases.HYDROGEN_CHLORIDE.get(), 1);
					this.buildItemStackGasToItemStack(MaterialState.CRYSTAL, 1, MaterialState.SHARD, 1, hydrogenChloride, ItemStackChemicalToTaggedOutputRecipeBuilder::injecting);
				}

			}

		}

		public void buildProcessingLevel4()
		{
			if (this.canProcess(MaterialState.SHARD) == true)
			{
				if (this.canProcess(MaterialState.ORE) == true)
				{
					GasStackIngredient hydrogenChloride = IngredientCreatorAccess.gasStack().from(MekanismGases.HYDROGEN_CHLORIDE.get(), 1);
					this.buildItemStackGasToItemStack(MaterialState.ORE, 1, MaterialState.SHARD, 4, hydrogenChloride, ItemStackChemicalToTaggedOutputRecipeBuilder::injecting);
				}

				if (this.canProcess(MaterialState.RAW_ITEM) == true)
				{
					GasStackIngredient hydrogenChloride1 = IngredientCreatorAccess.gasStack().from(MekanismGases.HYDROGEN_CHLORIDE.get(), 1);
					this.buildItemStackGasToItemStack(MaterialState.RAW_ITEM, 3, MaterialState.SHARD, 8, hydrogenChloride1, ItemStackChemicalToTaggedOutputRecipeBuilder::injecting);

					GasStackIngredient hydrogenChloride2 = IngredientCreatorAccess.gasStack().from(MekanismGases.HYDROGEN_CHLORIDE.get(), 2);
					this.buildItemStackGasToItemStack(MaterialState.RAW_STORAGE_BLOCKS, 1, MaterialState.SHARD, 24, hydrogenChloride2, ItemStackChemicalToTaggedOutputRecipeBuilder::injecting);
				}

				if (this.canProcess(MaterialState.CLUMP) == true)
				{
					GasStackIngredient oxygen = IngredientCreatorAccess.gasStack().from(MekanismGases.OXYGEN.get(), 1);
					this.buildItemStackGasToItemStack(MaterialState.SHARD, 1, MaterialState.CLUMP, 1, oxygen, ItemStackChemicalToTaggedOutputRecipeBuilder::purifying);
				}

			}

		}

		public void buildProcessingLevel3()
		{
			if (this.canProcess(MaterialState.CLUMP) == true)
			{
				if (this.canProcess(MaterialState.ORE) == true)
				{
					GasStackIngredient oxygen = IngredientCreatorAccess.gasStack().from(MekanismGases.OXYGEN.get(), 1);
					this.buildItemStackGasToItemStack(MaterialState.ORE, 1, MaterialState.CLUMP, 3, oxygen, ItemStackChemicalToTaggedOutputRecipeBuilder::purifying);
				}

				if (this.canProcess(MaterialState.RAW_ITEM) == true)
				{
					GasStackIngredient oxygen1 = IngredientCreatorAccess.gasStack().from(MekanismGases.OXYGEN.get(), 1);
					this.buildItemStackGasToItemStack(MaterialState.RAW_ITEM, 1, MaterialState.CLUMP, 2, oxygen1, ItemStackChemicalToTaggedOutputRecipeBuilder::purifying);

					GasStackIngredient oxygen2 = IngredientCreatorAccess.gasStack().from(MekanismGases.OXYGEN.get(), 2);
					this.buildItemStackGasToItemStack(MaterialState.RAW_STORAGE_BLOCKS, 1, MaterialState.CLUMP, 18, oxygen2, ItemStackChemicalToTaggedOutputRecipeBuilder::purifying);
				}

				if (this.canProcess(MaterialState.DIRTY_DUST) == true)
				{
					this.buildItemToItemStack(MaterialState.CLUMP, 1, MaterialState.DIRTY_DUST, 1, ItemStackToTaggedOutputRecipeBuilder::crushing);

					if (this.canProcess(MaterialState.DUST) == true)
					{
						this.buildItemToItemStack(MaterialState.DIRTY_DUST, 1, MaterialState.DUST, 1, ItemStackToTaggedOutputRecipeBuilder::enriching);
					}

				}

			}

		}

		public void buildProcessingLevel2()
		{
			if (this.materialType.isRespectMekanism() == true)
			{
				return;
			}

			if (this.canProcess(MaterialState.ORE) == true)
			{
				if (this.materialType.getResultShape() == MaterialResultShape.GEM)
				{
					if (this.canProcess(MaterialState.GEM) == true)
					{
						this.buildItemToItemStack(MaterialState.ORE, 1, MaterialState.GEM, 2, ItemStackToTaggedOutputRecipeBuilder::enriching);
					}

				}
				else
				{
					if (this.canProcess(MaterialState.DUST) == true)
					{
						this.buildItemToItemStack(MaterialState.ORE, 1, MaterialState.DUST, 2, ItemStackToTaggedOutputRecipeBuilder::enriching);
					}

				}

			}

			if (this.canProcess(MaterialState.RAW_ITEM) == true)
			{
				this.buildItemToItemStack(MaterialState.RAW_ITEM, 3, MaterialState.DUST, 4, ItemStackToTaggedOutputRecipeBuilder::enriching);
			}

			if (this.canProcess(MaterialState.RAW_STORAGE_BLOCKS) == true)
			{
				this.buildItemToItemStack(MaterialState.RAW_STORAGE_BLOCKS, 1, MaterialState.DUST, 12, ItemStackToTaggedOutputRecipeBuilder::enriching);
			}

			if (this.canProcess(MaterialState.DUST) == true)
			{
				if (this.canProcess(MaterialState.INGOT) == true)
				{
					this.buildCook(MaterialState.DUST, MaterialState.INGOT);
					this.buildItemToItemStack(MaterialState.INGOT, 1, MaterialState.DUST, 1, ItemStackToTaggedOutputRecipeBuilder::crushing);
				}

				if (this.canProcess(MaterialState.GEM) == true)
				{
					this.buildItemToItemStack(MaterialState.DUST, 1, MaterialState.GEM, 1, ItemStackToTaggedOutputRecipeBuilder::enriching);
					this.buildItemToItemStack(MaterialState.GEM, 1, MaterialState.DUST, 1, ItemStackToTaggedOutputRecipeBuilder::crushing);
				}

			}

		}

		public void buildOthers()
		{
			if (this.materialType.isRespectMekanism() == true)
			{
				return;
			}

			if (this.canProcess(MaterialState.INGOT, MaterialState.NUGGET) == true)
			{
				this.buildNuggetFromIngot();
				this.buildIngotFromNugget();
			}

		}

		public boolean canProcess(MaterialState... states)
		{
			return this.materialType.getResultShape().canProcess(Arrays.asList(states));
		}

		public String from(String name)
		{
			return "from_" + name;
		}

		public String from(String name, String method)
		{
			return this.from(name) + "_" + method;
		}

		public String from(MaterialState materialState)
		{
			return this.from(materialState.getBaseName());
		}

		public String from(MaterialState materialState, String method)
		{
			return this.from(materialState.getBaseName(), method);
		}

		public void buildChemicalCrystallizing(SlurryStackIngredient slurryInput, MaterialState stateOutput, int outputCount)
		{
			ItemStackIngredient output = this.getTaggedItemStackIngredient(stateOutput);
			CrystallizerTaggedOutputRecipeBuilder builder = CrystallizerTaggedOutputRecipeBuilder.crystallizing(slurryInput, output);

			this.applyCondition(builder::addCondition);
			builder.build(this.output, this.getRecipeName(stateOutput, this.from(MMPSlurry.SLURRY)));
		}

		public void buildChemicalWashing(FluidStackIngredient fluidInput, Slurry slurryInput, Slurry slurryOutput)
		{
			SlurryStackIngredient slurryStackInput = IngredientCreatorAccess.slurryStack().from(slurryInput, 1);
			SlurryStack slurryStackOutput = new SlurryStack(slurryOutput, 1);
			FluidSlurryToSlurryRecipeBuilder builder = FluidSlurryToSlurryRecipeBuilder.washing(fluidInput, slurryStackInput, slurryStackOutput);

			this.applyCondition(builder::addCondition);
			builder.build(this.output, this.getRecipeName(MMPSlurry.SLURRY, MMPSlurryBuilder.CLEAN));
		}

		public void buildChemicalDissolution(MaterialState stateInput, int inputCount, Slurry slurryOutput, int outputAmount, GasStackIngredient gasInput)
		{
			ItemStackIngredient itemInput = this.getTaggedItemStackIngredient(stateInput, inputCount);
			SlurryStack slurryStackOutput = new SlurryStack(slurryOutput, outputAmount);
			ChemicalDissolutionRecipeBuilder builder = ChemicalDissolutionRecipeBuilder.dissolution(itemInput, gasInput, slurryStackOutput);

			this.applyConditionWithState(builder::addCondition, stateInput);
			builder.build(this.output, this.getRecipeName(MMPSlurry.SLURRY, MMPSlurryBuilder.DIRTY + "/" + stateInput.getBaseName()));
		}

		public void buildItemStackGasToItemStack(MaterialState stateInput, int inputCount, MaterialState stateOutput, int outputCount, GasStackIngredient gasInput, ThreeFunction<ItemStackIngredient, GasStackIngredient, ItemStackIngredient, ItemStackChemicalToTaggedOutputRecipeBuilder<Gas, GasStack, GasStackIngredient>> function)
		{
			ItemStackIngredient itemInput = this.getTaggedItemStackIngredient(stateInput, inputCount);
			ItemStackIngredient output = this.getTaggedItemStackIngredient(stateOutput, outputCount);
			ItemStackChemicalToTaggedOutputRecipeBuilder<Gas, GasStack, GasStackIngredient> builder = function.apply(itemInput, gasInput, output);

			this.applyConditionWithState(builder::addCondition, stateInput);
			builder.build(this.output, this.getRecipeName(stateOutput, this.from(stateInput)));
		}

		public void buildItemToItemStack(MaterialState stateInput, int inputCount, MaterialState stateOutput, int outputCount, BiFunction<ItemStackIngredient, ItemStackIngredient, ItemStackToTaggedOutputRecipeBuilder> function)
		{
			ItemStackIngredient input = this.getTaggedItemStackIngredient(stateInput, inputCount);
			ItemStackIngredient output = this.getTaggedItemStackIngredient(stateOutput, outputCount);
			ItemStackToTaggedOutputRecipeBuilder builder = function.apply(input, output);

			this.applyConditionWithState(builder::addCondition, stateInput);
			builder.build(this.output, this.getRecipeName(stateOutput, this.from(stateInput)));
		}

		public void buildCook(MaterialState stateInput, MaterialState stateOutput)
		{
			Ingredient itemInput = this.getTaggedIngredient(stateInput);
			String group = this.getGroup(stateOutput);
			ItemStackIngredient output = this.getTaggedItemStackIngredient(stateOutput);
			float experience = 0.35F;

			List<Tuple<String, AbstractCookingRecipe>> list = new ArrayList<>();
			list.add(new Tuple<>("smelting", new SmeltingTaggedOutputRecipe(group, CookingBookCategory.MISC, itemInput, output, experience, 200)));
			list.add(new Tuple<>("blasting", new BlastingTaggedOutputRecipe(group, CookingBookCategory.MISC, itemInput, output, experience, 100)));

			for (Tuple<String, AbstractCookingRecipe> tuple : list)
			{
				ResourceLocation recipeName = this.getRecipeName(stateOutput, this.from(stateInput, tuple.getA()));
				this.output.accept(recipeName, tuple.getB(), null, this.collect(t -> this.applyConditionWithState(t, stateInput)));
			}

		}

		public void buildIngotFromNugget()
		{
			MaterialState stateInput = MaterialState.NUGGET;
			MaterialState stateOutput = MaterialState.INGOT;
			ItemStack output = new ItemStack(stateOutput.getItem(this.materialType));

			ShapedRecipePattern pattern = ShapedRecipePattern.of(Map.of('#', this.getTaggedIngredient(stateInput), //
					'*', this.getExcatIngredient(stateInput)), //
					"###", "#*#", "###");

			this.output.accept(this.getRecipeName(stateOutput, this.from(stateInput)), new ShapedRecipe(//
					this.getGroup(stateOutput), //
					CraftingBookCategory.MISC, //
					pattern, //
					output), null, this.collect(t -> this.applyConditionWithState(t, stateInput)));
		}

		public void buildNuggetFromIngot()
		{
			MaterialState stateInput = MaterialState.INGOT;
			MaterialState stateOutput = MaterialState.NUGGET;
			ItemStack output = new ItemStack(stateOutput.getItem(this.materialType), 9);

			List<Ingredient> ingredients = new ArrayList<>();
			ingredients.add(this.getExcatIngredient(stateInput));

			this.output.accept(this.getRecipeName(stateOutput, this.from(stateInput)), new ShapelessRecipe(//
					this.getGroup(stateOutput), //
					CraftingBookCategory.MISC, //
					output, //
					NonNullList.copyOf(ingredients)), null, this.collect(t -> this.applyConditionWithState(t, stateInput)));
		}

		public ResourceLocation getRecipeName(MaterialState stateOutput, String name)
		{
			return this.getRecipeName(stateOutput.getBaseName(), name);
		}

		public ResourceLocation getRecipeName(String stateOutput, String name)
		{
			return MoreMekanismProcessing.rl(("processing/" + this.materialType.getBaseName() + "/" + stateOutput + "/" + name).toLowerCase());
		}

		public Ingredient getExcatIngredient(MaterialState materialState)
		{
			return Ingredient.of(materialState.getItem(this.materialType));
		}

		public Ingredient getTaggedIngredient(MaterialState materialState)
		{
			return Ingredient.of(this.getTag(materialState));
		}

		public ItemStackIngredient getTaggedItemStackIngredient(MaterialState materialState)
		{
			TagKey<Item> tag = this.getTag(materialState);
			return this.getTaggedItemStackIngredient(tag);
		}

		public ItemStackIngredient getTaggedItemStackIngredient(MaterialState materialState, int amount)
		{
			TagKey<Item> tag = this.getTag(materialState);
			return this.getTaggedItemStackIngredient(tag, amount);
		}

		public ItemStackIngredient getTaggedItemStackIngredient(TagKey<Item> tag)
		{
			return this.getTaggedItemStackIngredient(tag, 1);
		}

		public ItemStackIngredient getTaggedItemStackIngredient(TagKey<Item> tag, int amount)
		{
			return IngredientCreatorAccess.item().from(tag, amount);
		}

		public TagKey<Item> getTag(MaterialState materialState)
		{
			return ItemTags.create(materialState.getStateTagName(this.materialType));
		}

		public String getGroup(MaterialState stateOutput)
		{
			return BuiltInRegistries.ITEM.getKey(stateOutput.getItem(this.materialType)).toString();
		}

		public MaterialType getMaterialType()
		{
			return this.materialType;
		}

		public RecipeOutput getConsumer()
		{
			return this.output;
		}

	}

}
