package gisellevonbingen.mmp.common.crafting;

import java.util.function.BiFunction;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import mekanism.api.SerializationConstants;
import mekanism.api.recipes.basic.BasicChemicalCrystallizerRecipe;
import mekanism.api.recipes.basic.BasicCrushingRecipe;
import mekanism.api.recipes.basic.BasicEnrichingRecipe;
import mekanism.api.recipes.basic.BasicInjectingRecipe;
import mekanism.api.recipes.basic.BasicItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.basic.BasicItemStackToItemStackRecipe;
import mekanism.api.recipes.basic.BasicPurifyingRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.recipe.serializer.MekanismRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MMPRecipeSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MoreMekanismProcessing.MODID);

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<? extends SmeltingRecipe>> TAG_SMELTING = RECIPE_SERIALIZERS.register("tag_smelting", () -> new CookingTaggedOutputSerializer<>(SmeltingTaggedOutputRecipe::new, 200));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<? extends BlastingRecipe>> TAG_BLASTING = RECIPE_SERIALIZERS.register("tag_blasting", () -> new CookingTaggedOutputSerializer<>(BlastingTaggedOutputRecipe::new, 100));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<? extends BasicEnrichingRecipe>> TAG_ENRICHING = RECIPE_SERIALIZERS.register("tag_enriching", () -> itemToTaggedOutput(EnrichingTaggedOutputRecipe::new));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<? extends BasicCrushingRecipe>> TAG_CRUSHING = RECIPE_SERIALIZERS.register("tag_crushing", () -> itemToTaggedOutput(CrushingTaggedOutputRecipe::new));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<? extends BasicPurifyingRecipe>> TAG_PURIFYING = RECIPE_SERIALIZERS.register("tag_purifying", () -> itemChemicalToTaggedOutput(PurifyingTaggedOutputRecipe::new));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<? extends BasicInjectingRecipe>> TAG_INJECTING = RECIPE_SERIALIZERS.register("tag_injecting", () -> itemChemicalToTaggedOutput(InjectingTaggedOutputRecipe::new));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<? extends BasicChemicalCrystallizerRecipe>> TAG_CRYSTALLIZING = RECIPE_SERIALIZERS.register("tag_crystallizing", () -> new ChemicalCrystallizerTaggedOutputRecipe.Serializer(ChemicalCrystallizerTaggedOutputRecipe::new));

	public static <RECIPE extends BasicItemStackToItemStackRecipe & ITaggedOutputRecipe> MekanismRecipeSerializer<RECIPE> itemToTaggedOutput(BiFunction<ItemStackIngredient, ItemStackIngredient, RECIPE> factory)
	{
		return new MekanismRecipeSerializer<>(RecordCodecBuilder.mapCodec(instance -> instance.group(//
				ItemStackIngredient.CODEC.fieldOf(SerializationConstants.INPUT).forGetter(RECIPE::getInput), //
				ItemStackIngredient.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(RECIPE::getTaggedResult)//
		).apply(instance, factory)), StreamCodec.composite(//
				ItemStackIngredient.STREAM_CODEC, RECIPE::getInput, //
				ItemStackIngredient.STREAM_CODEC, RECIPE::getTaggedResult, //
				factory)//
		);
	}

	public static <RECIPE extends BasicItemStackChemicalToItemStackRecipe & ITaggedOutputRecipe> MekanismRecipeSerializer<RECIPE> itemChemicalToTaggedOutput(Function4<ItemStackIngredient, ChemicalStackIngredient, ItemStackIngredient, Boolean, RECIPE> factory)
	{
		return new MekanismRecipeSerializer<>(RecordCodecBuilder.mapCodec(instance -> instance.group(//
				ItemStackIngredient.CODEC.fieldOf(SerializationConstants.ITEM_INPUT).forGetter(RECIPE::getItemInput), //
				IngredientCreatorAccess.chemicalStack().codec().fieldOf(SerializationConstants.CHEMICAL_INPUT).forGetter(RECIPE::getChemicalInput), ItemStackIngredient.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(RECIPE::getTaggedResult), //
				Codec.BOOL.fieldOf(SerializationConstants.PER_TICK_USAGE).forGetter(RECIPE::perTickUsage)//
		).apply(instance, factory)), StreamCodec.composite(//
				ItemStackIngredient.STREAM_CODEC, RECIPE::getItemInput, //
				IngredientCreatorAccess.chemicalStack().streamCodec(), RECIPE::getChemicalInput, //
				ItemStackIngredient.STREAM_CODEC, RECIPE::getTaggedResult, //
				ByteBufCodecs.BOOL, RECIPE::perTickUsage, //
				factory)//
		);
	}

}
