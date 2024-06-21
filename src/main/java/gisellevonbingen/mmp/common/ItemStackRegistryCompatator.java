package gisellevonbingen.mmp.common;

import java.util.Comparator;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ItemStackRegistryCompatator(RegistryComparator<Item> itemComparator) implements Comparator<ItemStack>
{
	@Override
	public int compare(ItemStack o1, ItemStack o2)
	{
		return this.itemComparator.compare(o1.getItem(), o2.getItem());
	}

}
