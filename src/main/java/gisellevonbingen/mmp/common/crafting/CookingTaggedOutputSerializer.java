package gisellevonbingen.mmp.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CookingTaggedOutputSerializer<T extends AbstractCookingRecipe & ITaggedOutputRecipe & ISingleIngredientRecipe> implements RecipeSerializer<T>
{
	private final CookingTaggedOutputSerializer.Factory<T> factory;
	private final MapCodec<T> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

	public CookingTaggedOutputSerializer(CookingTaggedOutputSerializer.Factory<T> pFactory, int pCookingTime)
	{
		this.factory = pFactory;
		this.codec = RecordCodecBuilder.mapCodec(p_300831_ -> p_300831_.group(//
				Codec.STRING.optionalFieldOf("group", "").forGetter(T::getGroup), //
				CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC).forGetter(T::category), //
				Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(T::getIngredient), //
				ItemStackIngredient.CODEC.fieldOf("result").forGetter(T::getTaggedResult), //
				Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(T::getExperience), //
				Codec.INT.fieldOf("cookingtime").orElse(pCookingTime).forGetter(T::getCookingTime)).apply(p_300831_, pFactory::create));
		this.streamCodec = StreamCodec.of(this::toNetwork, this::fromNetwork);
	}

	@Override
	public MapCodec<T> codec()
	{
		return this.codec;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec()
	{
		return this.streamCodec;
	}

	private T fromNetwork(RegistryFriendlyByteBuf buffer)
	{
		var group = buffer.readUtf();
		var category = buffer.readEnum(CookingBookCategory.class);
		var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
		var result = ItemStackIngredient.STREAM_CODEC.decode(buffer);
		var experience = buffer.readFloat();
		var cookingTime = buffer.readVarInt();
		return this.factory.create(group, category, ingredient, result, experience, cookingTime);
	}

	private void toNetwork(RegistryFriendlyByteBuf buffer, T recipe)
	{
		buffer.writeUtf(recipe.getGroup());
		buffer.writeEnum(recipe.category());
		Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getIngredient());
		ItemStackIngredient.STREAM_CODEC.encode(buffer, recipe.getTaggedResult());
		buffer.writeFloat(recipe.getExperience());
		buffer.writeVarInt(recipe.getCookingTime());
	}

	public static interface Factory<T>
	{
		T create(String group, CookingBookCategory category, Ingredient ingredient, ItemStackIngredient result, float experience, int cookingTime);
	}

	public AbstractCookingRecipe create(String group, CookingBookCategory category, Ingredient ingredient, ItemStackIngredient result, float experience, int cookingTime)
	{
		return this.factory.create(group, category, ingredient, result, experience, cookingTime);
	}

}
