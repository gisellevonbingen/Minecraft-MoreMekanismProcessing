package gisellevonbingen.mmp.common.item;

import java.util.List;

import mekanism.common.registries.MekanismBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MMPCreativeModeTab extends CreativeModeTab
{
	private List<? extends Item> items;
	private int iconIndex = 0;
	private long lastMillis = 0L;
	private ItemStack icon = null;
	private ItemStack fallbackIcon = null;

	public MMPCreativeModeTab(Builder builder)
	{
		super(builder);
	}

	public ItemStack makeIcon()
	{
		if (this.items == null)
		{
			this.items = MMPItems.ITEMS.getEntries().stream().map(i -> i.get()).toList();
		}

		int size = this.items.size();

		if (size > 0)
		{
			this.iconIndex = this.iconIndex % size;
			return items.get(this.iconIndex).getDefaultInstance();
		}
		else if (this.fallbackIcon == null)
		{
			this.fallbackIcon = new ItemStack(MekanismBlocks.CHEMICAL_DISSOLUTION_CHAMBER);
		}

		return this.fallbackIcon;
	}

	@Override
	public ItemStack getIconItem()
	{
		long millis = System.currentTimeMillis();

		if (this.icon == null || this.lastMillis == 0 || (millis - this.lastMillis) > 2000)
		{
			this.icon = this.makeIcon();
			this.lastMillis = millis;
			this.iconIndex++;
		}

		return this.icon;
	}

}
