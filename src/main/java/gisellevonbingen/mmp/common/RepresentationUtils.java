package gisellevonbingen.mmp.common;

import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public class RepresentationUtils
{
	public static final ItemStackRegistryCompatator ITEM_STACK_COMPARATOR = new ItemStackRegistryCompatator(new RegistryComparator<>(BuiltInRegistries.ITEM));

	public static ItemStack getRepresentation(ItemStackIngredient ingredient)
	{
		return ingredient.getRepresentations().stream().min(ITEM_STACK_COMPARATOR).orElse(ItemStack.EMPTY);
	}

	private RepresentationUtils()
	{

	}

}
