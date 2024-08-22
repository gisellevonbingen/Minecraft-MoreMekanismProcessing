package gisellevonbingen.mmp.common.crafting;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import org.jetbrains.annotations.NotNull;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.RepresentationUtils;
import mekanism.api.SerializationConstants;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.basic.BasicChemicalCrystallizerRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ChemicalCrystallizerTaggedOutputRecipe extends BasicChemicalCrystallizerRecipe implements ITaggedOutputRecipe
{
	protected final ItemStackIngredient output;

	private int revision;
	private ItemStack cachedResult;

	public ChemicalCrystallizerTaggedOutputRecipe(ChemicalStackIngredient input, ItemStackIngredient output)
	{
		super(input, MekanismItems.ATOMIC_ALLOY.getItemStack());
		this.output = output;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipeSerializer<BasicChemicalCrystallizerRecipe> getSerializer()
	{
		return (RecipeSerializer<BasicChemicalCrystallizerRecipe>) MMPRecipeSerializers.TAG_CRYSTALLIZING.get();
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
	public ItemStack getOutput(ChemicalStack input)
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

	public static class Serializer implements RecipeSerializer<ChemicalCrystallizerTaggedOutputRecipe>
	{
		private final StreamCodec<RegistryFriendlyByteBuf, ChemicalCrystallizerTaggedOutputRecipe> streamCodec;
		private final MapCodec<ChemicalCrystallizerTaggedOutputRecipe> codec;

		public Serializer(BiFunction<ChemicalStackIngredient, ItemStackIngredient, ChemicalCrystallizerTaggedOutputRecipe> factory)
		{
			this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(//
					IngredientCreatorAccess.chemicalStack().codec().fieldOf(SerializationConstants.INPUT).forGetter(BasicChemicalCrystallizerRecipe::getInput), //
					ItemStackIngredient.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(ChemicalCrystallizerTaggedOutputRecipe::getTaggedResult)//
			).apply(instance, factory));
			this.streamCodec = StreamCodec.composite(//
					IngredientCreatorAccess.chemicalStack().streamCodec(), BasicChemicalCrystallizerRecipe::getInput, //
					ItemStackIngredient.STREAM_CODEC, ChemicalCrystallizerTaggedOutputRecipe::getTaggedResult, //
					factory);
		}

		@NotNull
		@Override
		public MapCodec<ChemicalCrystallizerTaggedOutputRecipe> codec()
		{
			return this.codec;
		}

		@NotNull
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ChemicalCrystallizerTaggedOutputRecipe> streamCodec()
		{
			return this.streamCodec;
		}

	}

}
